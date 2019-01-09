package com.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ClusteringDataGenerator {

    private static List<String> points = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        DecimalFormat format = new DecimalFormat("0.00");

        IntStream.range(0, 100).forEach(value -> {
            double xValue = ThreadLocalRandom.current().nextDouble(-0.3, 0.3);
            double yValue = ThreadLocalRandom.current().nextDouble(-0.3, 0.3);
            points.add(format.format(xValue) + "," + format.format(yValue));
        });

        IntStream.range(0, 100).forEach(value -> {
            double xValue = ThreadLocalRandom.current().nextDouble(-1.1, -0.5);
            double yValue = ThreadLocalRandom.current().nextDouble(0.5, 1.1);
            points.add(format.format(xValue) + "," + format.format(yValue));
        });

        IntStream.range(0, 100).forEach(value -> {
            double xValue = ThreadLocalRandom.current().nextDouble(-1.1, -0.5);
            double yValue = ThreadLocalRandom.current().nextDouble(-1.1, -0.5);
            points.add(format.format(xValue) + "," + format.format(yValue));
        });

        IntStream.range(0, 100).forEach(value -> {
            double xValue =  ThreadLocalRandom.current().nextDouble(0.5, 1.1);
            double yValue =  ThreadLocalRandom.current().nextDouble(-1.1, -0.5);
            points.add(format.format(xValue) + "," + format.format(yValue));
        });

        IntStream.range(0, 100).forEach(value -> {
            double xValue =  ThreadLocalRandom.current().nextDouble(0.5, 1.1);
            double yValue =  ThreadLocalRandom.current().nextDouble(0.5, 1.1);
            points.add(format.format(xValue) + "," + format.format(yValue));
        });

        System.out.println(points.size());

        BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(
                new File("clustering_data.txt")));

        Collections.shuffle(points);
        points.forEach(point -> {
            try {
                bufferedWriter.write(point + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bufferedWriter.close();
    }
}

