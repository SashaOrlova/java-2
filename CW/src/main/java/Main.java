import ru.java.checksum.MultiThreadChecker;
import ru.java.checksum.OneThreadChecker;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OneThreadChecker oneThreadChecker = new OneThreadChecker();
        MultiThreadChecker multiThreadChecker = new MultiThreadChecker();
        while (true) {
            System.out.println("print filename or directory");
            Scanner in = new Scanner(System.in);
            String st = in.nextLine();
            long startOne = System.currentTimeMillis();
            String res = Arrays.toString(oneThreadChecker.Sum(st));
            long finishOne = System.currentTimeMillis();
            System.out.println("In one thread work in time " + (finishOne - startOne));
            long startMult = System.currentTimeMillis();
            multiThreadChecker.Sum(st);
            long finishMult = System.currentTimeMillis();
            System.out.println("In multi thread work in time " + (finishMult - startMult));
            System.out.println(res);
        }
    }
}
