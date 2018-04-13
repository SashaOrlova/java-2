package ru.java.checksum;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import static org.junit.Assert.assertArrayEquals;

public class OneThreadCheckerTest {
    @Test
    public void simpleTest() throws Exception {
        OneThreadChecker oneThreadChecker = new OneThreadChecker();
        String test = "src/main/resources/test";
        byte[] buf = new byte[100000];
        byte[] ans = oneThreadChecker.Sum(test);
        File file = new File(test);
        InputStream fileStream = new FileInputStream(file);
        MessageDigest md = MessageDigest.getInstance("MD5");
        DigestInputStream st = new DigestInputStream(fileStream, md);
        int len = 100;
        while (st.read(buf, 0, len) > 0) {
        }
        assertArrayEquals(md.digest(), ans);
    }

    @Test
    public void emptyTest() throws Exception {
        OneThreadChecker oneThreadChecker = new OneThreadChecker();
        String test = "src/main/resources/test1";
        byte[] buf = new byte[100000];
        byte[] ans = oneThreadChecker.Sum(test);
        File file = new File(test);
        InputStream fileStream = new FileInputStream(file);
        MessageDigest md = MessageDigest.getInstance("MD5");
        DigestInputStream st = new DigestInputStream(fileStream, md);
        int len = 100;
        while (st.read(buf, 0, len) > 0) {
        }
        assertArrayEquals(md.digest(), ans);
    }

}