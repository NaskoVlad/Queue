package ru.netology;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static BlockingQueue<String> textA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> textB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> textC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        int number = 10_000;
        /*
        Поток для генерации текста
         */
        new Thread(() -> {
            for (int i = 0; i < number; i++) {
                String text = generateText("abc", 100_000);
                try {
                    textA.put(text);
                    textB.put(text);
                    textC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        /*
        Поток для чтения строк из очереди для подсчета буквы 'A'
         */
        new Thread(() -> {
            AtomicInteger numberA = new AtomicInteger();
            AtomicInteger variable = new AtomicInteger();
            String text;
            for (int i = 0; i < number; i++) {
                try {
                    text = textA.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == 'a') {
                        variable.getAndIncrement();
                    }
                }
                if (variable.get() > numberA.get()) {
                    numberA.lazySet(variable.get());
                }
                variable.lazySet(0);
            }
            System.out.println("Макисимальное количество символов 'A' - " + numberA);
        }).start();

        /*
        Поток для чтения строк из очереди для подсчета буквы 'B'
         */
        new Thread(() -> {
            AtomicInteger numberB = new AtomicInteger();
            AtomicInteger variable = new AtomicInteger();
            String text;
            for (int i = 0; i < number; i++) {
                try {
                    text = textB.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == 'b') {
                        variable.getAndIncrement();
                    }
                }
                if (variable.get() > numberB.get()) {
                    numberB.lazySet(variable.get());
                }
                variable.lazySet(0);
            }
            System.out.println("Макисимальное количество символов 'B' - " + numberB);
        }).start();

                /*
        Поток для чтения строк из очереди для подсчета буквы 'C'
         */
        new Thread(() -> {
            AtomicInteger numberC = new AtomicInteger();
            AtomicInteger variable = new AtomicInteger();
            String text;
            for (int i = 0; i < number; i++) {
                try {
                    text = textC.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == 'c') {
                        variable.getAndIncrement();
                    }
                }
                if (variable.get() > numberC.get()) {
                    numberC.lazySet(variable.get());
                }
                variable.lazySet(0);
            }
            System.out.println("Макисимальное количество символов 'C' - " + numberC);
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}