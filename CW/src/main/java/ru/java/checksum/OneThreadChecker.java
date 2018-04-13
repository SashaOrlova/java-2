package ru.java.checksum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Class for check sum in one thread
 */
public class OneThreadChecker {
    private MessageDigest md;
    private byte[] buf = new byte[100000];

    /**
     * Create new OneThreadChecker
     */
    public OneThreadChecker() {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error in computation algorithm");
        }
    }

    /**
     * method for check md5 sum in directory
     *
     * @param root name of file or directory
     * @return result of md5
     */
    public byte[] Sum(String root) {
        File file = new File(root);
        if (file.isDirectory()) {
            StringBuilder s = new StringBuilder();
            File[] folderEntries = file.listFiles();
            if (folderEntries != null) {
                for (File childrenFiles : folderEntries) {
                    s.append(Arrays.toString(Sum(childrenFiles.getAbsolutePath())));
                }
                md.update((file.getName() + s).getBytes());
                return md.digest();
            }
        } else {
            if (file.isFile()) {
                try {
                    InputStream fileStream = new FileInputStream(file);
                    DigestInputStream st = new DigestInputStream(fileStream, md);
                    int len = 100;
                    while (st.read(buf, 0, len) > 0) {
                    }
                    return md.digest();
                } catch (IOException e) {
                    System.out.println("Error in work with file" + file.getName());
                }
            }
        }
        return null;
    }
}
