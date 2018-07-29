package com.unit;

import org.jfree.data.function.LineFunction2D;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
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
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                try {
                    System.out.println(scanner.nextDouble() + scanner.nextDouble() * x);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Parsing file error!");
                    System.exit(1);
                }
            }
        }
    }
}
