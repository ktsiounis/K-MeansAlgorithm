package com.main;

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

    private static final int M = 3;

    private static ArrayList<com.main.Point> points = new ArrayList<>();
    private static ArrayList<ArrayList<com.main.Point>> clusters = new ArrayList<>();
    private static ArrayList<com.main.Point> centerPoints = new ArrayList<>();
    private static Random randomGenerator = new Random();
    private static boolean flag = true;
    private static ArrayList<ArrayList<Double>> previousDistances = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> pointFromCenterDistances = new ArrayList<>();
    private static ArrayList<Double> totalDispersions = new ArrayList<>();

    public static void main(String[] args) {

        if (args != null && args[0].contains(".txt")) {

            System.out.println(args[0]);
            File file = new File(KMeans.class.getResource(args[0]).getFile());
            readPointsFromFile(file);
            initializeData();

            for (int i = 0; i<5; i++ ) {
                pointFromCenterDistances = new ArrayList<>();
                for (int m = 0; m<M; m++) {
                    ArrayList<Double> distances = new ArrayList<>();
                    pointFromCenterDistances.add(distances);
                }
                previousDistances = pointFromCenterDistances;
                do {
                    for (com.main.Point point : points) {
                        ArrayList<Double> distancesFromCenters = new ArrayList<>();
                        for (com.main.Point centerPoint : centerPoints) {
                            double distanceFromCluster = Math.sqrt(Math.pow((point.getX() - centerPoint.getX()), 2) + Math.pow((point.getY() - centerPoint.getY()), 2));
                            distancesFromCenters.add(distanceFromCluster);
                        }

                        clusters.get(distancesFromCenters.indexOf(Collections.min(distancesFromCenters))).add(point);
                        pointFromCenterDistances.get(distancesFromCenters.indexOf(Collections.min(distancesFromCenters))).add(Collections.min(distancesFromCenters));

                    }

                    for (ArrayList<com.main.Point> cluster : clusters) {
                        System.out.println("\nCluster " + clusters.indexOf(cluster) + ": Center " + centerPoints.get(clusters.indexOf(cluster)).toString());
                        double sumX = 0;
                        double sumY = 0;
                        for (com.main.Point point : cluster) {
                            System.out.print(point.toString() + " ");
                            sumX += point.getX();
                            sumY += point.getY();
                        }
                        System.out.println(sumX);
                        centerPoints.set(clusters.indexOf(cluster), new com.main.Point(sumX / cluster.size(), sumY / cluster.size()));
                        System.out.println("\nNew cluster center: " + centerPoints.get(clusters.indexOf(cluster)).toString());
                    }

                    for (int k=0; k<pointFromCenterDistances.size(); k++) {
                        for (int l=0; l<pointFromCenterDistances.get(k).size(); l++) {
                            if (previousDistances.get(k).get(l).equals(pointFromCenterDistances.get(k).get(l))) {
                                flag = false;
                                break;
                            }
                        }
                        if (!flag) {
                            break;
                        }
                    }

                    if (flag) {
                        previousDistances = pointFromCenterDistances;
                    }

                    if (i != 4) {
                        for (ArrayList<com.main.Point> cluster : clusters) {
                            cluster.clear();
                        }
                    }
                } while (flag);
                flag = true;

                double totalDispersion = 0;
                for (int j=0; j<clusters.size(); j++) {
                    double sum = 0;
                    for (Double clusterDistances : pointFromCenterDistances.get(j)) {
                        sum += clusterDistances;
                    }
                    totalDispersion += sum;
                }
                totalDispersions.add(totalDispersion);
            }

            for (Double totalDispersion : totalDispersions) {
                System.out.println(totalDispersion);
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
                com.main.Point point = new com.main.Point(Double.valueOf(sCurrentLine.split(",")[0]), Double.valueOf(sCurrentLine.split(",")[1]));
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
            ArrayList<com.main.Point> cluster = new ArrayList<>();
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

        for (com.main.Point point : centerPoints) {
            series.add(point.getX(), point.getY());
        }

        dataset.addSeries(series);

        int i=0;
        for (ArrayList<com.main.Point> cluster : clusters) {
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
