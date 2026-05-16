package ru.itis.dis403.lab2_9.httpclient;

import ru.itis.dis403.lab2_9.httpclient.service.ImageService;
import ru.itis.dis403.lab2_9.httpclient.service.ImageServiceForNats;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Benchmark {

    public static void main(String[] args) throws Exception {

        byte[] image = Files.readAllBytes(Paths.get("lab02_09_inner_service/img.jpg"));

        ImageService httpService = new ImageService();
        ImageServiceForNats natsService = new ImageServiceForNats();
        natsService.init();

        int iterations = 50;

        System.out.println("HTTP TEST");
        List<Long> httpTimes = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            httpService.processImage(image);
            long end = System.nanoTime();

            httpTimes.add((end - start) / 1_000_000);
        }

        printStats("HTTP", httpTimes);

        System.out.println("NATS TEST");
        List<Long> natsTimes = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            natsService.processImage(image);
            long end = System.nanoTime();

            natsTimes.add((end - start) / 1_000_000);
        }

        printStats("NATS", natsTimes);
    }

    private static void printStats(String label, List<Long> times) {
        double avg = times.stream().mapToLong(Long::longValue).average().orElse(0);
        long min = times.stream().mapToLong(Long::longValue).min().orElse(0);
        long max = times.stream().mapToLong(Long::longValue).max().orElse(0);

        System.out.println(label + " AVG: " + avg + " ms");
        System.out.println(label + " MIN: " + min + " ms");
        System.out.println(label + " MAX: " + max + " ms");
    }
}