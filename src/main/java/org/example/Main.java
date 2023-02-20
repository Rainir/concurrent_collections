package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public final static int TEXT_LENGTH = 100_000;
    public final static int NUMBER_ROWS = 10_000;
    public final static String LETTERS = "abc";
    public static BlockingQueue<String> textsToA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> textsToB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> textsToC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {

        Thread rowGenerate = new Thread(() -> {
            for (int i = 0; i < NUMBER_ROWS; i++) {
                try {
                    String text = generateText(LETTERS, TEXT_LENGTH);
                    textsToA.put(text);
                    textsToB.put(text);
                    textsToC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        rowGenerate.start();

        Thread searchA = new Thread(() -> {
            for (int i = 0; i < NUMBER_ROWS; i++) {
                try {
                    maxSizeSearch(textsToA.take(), 'a');
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        searchA.start();

        Thread searchB = new Thread(() -> {
            for (int i = 0; i < NUMBER_ROWS; i++) {
                try {
                    maxSizeSearch(textsToB.take(), 'b');
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        searchB.start();

        Thread searchC = new Thread(() -> {
            for (int i = 0; i < NUMBER_ROWS; i++) {
                try {
                    maxSizeSearch(textsToC.take(), 'c');
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        searchC.start();

        rowGenerate.join();
        searchA.join();
        searchB.join();
        searchC.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void maxSizeSearch(String row, char letter) {
        int maxSize = 0;
        for (int i = 0; i < row.length(); i++) {
            for (int j = 0; j < row.length(); j++) {
                if (i >= j) {
                    continue;
                }
                boolean bFound = false;
                for (int k = i; k < j; k++) {
                    if (row.charAt(k) == letter) {
                        bFound = true;
                        break;
                    }
                }
                if (!bFound && maxSize < j - i) {
                    maxSize = j - i;
                }
            }
        }
        System.out.println(row.substring(0, 100) + " -> " + maxSize + " " + letter);
    }
}