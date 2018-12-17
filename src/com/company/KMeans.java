package com.company;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class KMeans extends JFrame {

    private static final int M = 4;

    private static ArrayList<Point> points = new ArrayList<>();
    private static ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
    private static ArrayList<Point> centerPoints = new ArrayList<>();
    private static Random randomGenerator = new Random();

    public static void main(String[] args) {

        if (args != null && args[0].contains(".txt")) {

            System.out.println(args[0]);
            File file = new File(KMeans.class.getResource(args[0]).getFile());
            readPointsFromFile(file);
            initializeData();

            for (int i = 0; i<5; i++ ) {
                for (Point point : points) {
                    ArrayList<Double> distancesFromCenters = new ArrayList<>();
                    for (Point centerPoint : centerPoints) {
                        double distanceFromCluster = Math.sqrt(Math.pow((point.getX() - centerPoint.getX()), 2) + Math.pow((point.getY() - centerPoint.getY()), 2));
                        distancesFromCenters.add(distanceFromCluster);
                    }

                    clusters.get(distancesFromCenters.indexOf(Collections.min(distancesFromCenters))).add(point);

                }

                for (ArrayList<Point> cluster : clusters) {
                    System.out.println("\nCluster " + clusters.indexOf(cluster) + ": Center " + centerPoints.get(clusters.indexOf(cluster)).toString());
                    double sumX = 0;
                    double sumY = 0;
                    for (Point point : cluster) {
                        System.out.print(point.toString() + " ");
                        sumX += point.getX();
                        sumY += point.getY();
                    }
                    System.out.println(sumX);
                    centerPoints.set(clusters.indexOf(cluster), new Point(Math.round(sumX / cluster.size()), Math.round(sumY / cluster.size())));
                    System.out.println("\nNew cluster center: " + centerPoints.get(clusters.indexOf(cluster)).toString());
                }

                if (i != 4) {
                    for (ArrayList<Point> cluster : clusters) {
                        cluster.clear();
                    }
                }
            }

            KMeans example = new KMeans("Scatter Chart Example");
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);

        } else {
            System.out.println("You need to provide a txt file with points!");
        }

    }

    private static void readPointsFromFile (File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String sCurrentLine;
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                sCurrentLine = sCurrentLine.replace("(", "");
                sCurrentLine = sCurrentLine.replace(")", "");
                Point point = new Point(Double.valueOf(sCurrentLine.split(",")[0]), Double.valueOf(sCurrentLine.split(",")[1]));
                points.add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeData() {
        ArrayList<Integer> indexes = new ArrayList<>();
        int index;
        for (int i = 0; i<M; i++) {
            ArrayList<Point> cluster = new ArrayList<>();
            clusters.add(cluster);
            index = randomGenerator.nextInt(points.size());
            while (indexes.contains(index)) {
                index = randomGenerator.nextInt(points.size());
            }
            indexes.add(index);
            centerPoints.add(points.get(index));
        }
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("Centers");

        for (Point point : centerPoints) {
            series.add(point.getX(), point.getY());
        }

        dataset.addSeries(series);

        int i=0;
        for (ArrayList<Point> cluster : clusters) {
            series = new XYSeries("Cluster " + i);
            for (Point point : cluster) {
                series.add(point.getX(), point.getY());
            }
            i++;
            dataset.addSeries(series);
        }

        return dataset;
    }

    public KMeans(String title) {
        super(title);

        // Create dataset
        XYDataset dataset = createDataset();

        // Create chart
        JFreeChart chart = ChartFactory.createScatterPlot("All the points",
                "X-Axis",
                "Y-Axis",
                dataset, PlotOrientation.HORIZONTAL,
                true, true, false);


        //Changes background color
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,228,196));


        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

}
