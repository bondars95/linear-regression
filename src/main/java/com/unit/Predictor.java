package com.unit;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Predictor {
    public static void main(String[] args) throws FileNotFoundException {
        // If we have an input parameter, predict the price and draw the new point
        if (args.length >= 1 && args[0] != null) {
            // Estimate the linear function given the input data
            double x = 0;
            try {
                x = Double.parseDouble(args[0]);
            } catch (Exception e) {
                System.out.println("Wrong mileauge received");
                System.exit(1);
            }
            File file = new File("coefficients.txt");
            if (!file.exists()) {
                System.out.println(0);
                return;
            }
            Scanner scanner = new Scanner(file);
            double a = 0;
            double b = 0;
            while (scanner.hasNext()) {
                try {
                     a = scanner.nextDouble();
                     b = scanner.nextDouble();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Parsing file error!");
                    System.exit(1);
                }
            }
            System.out.println(a + b * x);
        }
    }
}
