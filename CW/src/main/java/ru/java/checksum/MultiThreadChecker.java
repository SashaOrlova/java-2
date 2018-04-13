package ru.java.checksum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Class for check sum in many threads
 */
public class MultiThreadChecker {
    private ForkJoinPool commonPool = ForkJoinPool.commonPool();

    /**
     * method for check md5 sum in directory using many threads
     *
     * @param root name of file or directory
     * @return result of md5
     */
    public byte[] Sum(String root) {
        return commonPool.invoke(new CheckerRecursiveAction(root)).bytes;
    }

    private class ByteStorage {
        byte[] bytes;

        ByteStorage(byte[] b) {
            bytes = b;
        }
    }

    private class CheckerRecursiveAction extends RecursiveTask<ByteStorage> {
        String root;
        MessageDigest md;

        public CheckerRecursiveAction(String filename) {
            root = filename;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Error in computation algorithm");
            }
        }

        @Override
        protected ByteStorage compute() {

            byte[] buf = new byte[100000];
            File file = new File(root);
            if (file.isDirectory()) {
                StringBuilder s = new StringBuilder();
                File[] folderEntries = file.listFiles();
                if (folderEntries != null) {
                    ArrayList<CheckerRecursiveAction> subFiles = new ArrayList<>();
                    for (File childrenFiles : folderEntries) {
                        CheckerRecursiveAction task = new CheckerRecursiveAction(childrenFiles.getAbsolutePath());
                        task.join();
                        subFiles.add(task);
                    }
                    for (CheckerRecursiveAction task : subFiles) {
                        s.append(Arrays.toString(task.join().bytes));
                    }
                    md.update((file.getName() + s).getBytes());
                    return new ByteStorage(md.digest());
                }
            } else {
                if (file.isFile()) {
                    try {
                        InputStream fileStream = new FileInputStream(file);
                        DigestInputStream st = new DigestInputStream(fileStream, md);
                        int len = 100;
                        while (st.read(buf, 0, 100) > 0) {
                        }
                        return new ByteStorage(md.digest());
                    } catch (IOException e) {
                        System.out.println("Error in work with file" + file.getName());
                    }
                }
            }
            return null;
        }
    }
}
