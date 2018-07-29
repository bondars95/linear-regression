package com.unit;

import org.jfree.ui.RefineryUtilities;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<DatasetParser.DataSetEntry> res = Collections.emptyList();
        if (args.length != 1) {
            System.out.println("Sorry I expect some files for training");
            System.exit(1);
        }
        try {
            res = new DatasetParser().createDatasetFromFile(args[0]);
        } catch (Exception e) {
            System.out.println("It seems, like I received bad formatted dataSet sorry");
            System.exit(1);
        }
        RegressionModel model = new LinearRegressionModel(
                res.stream().map(DatasetParser.DataSetEntry::getX).mapToDouble(Double::doubleValue).toArray(),
                res.stream().map(DatasetParser.DataSetEntry::getY).mapToDouble(Double::doubleValue).toArray()
        );
        model.compute();
        double[] coefficients = model.getCoefficients();
        System.out.printf("Building regression line for: y = %.4f + %.4fx", coefficients[0], coefficients[1]);
        display(args, res, coefficients);
    }

    private static void display(
            String[] args,
            List<DatasetParser.DataSetEntry> res,
            double[] coefficients
    ) throws IOException {
        ChartViewer chart = new ChartViewer(res);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
        // Draw the regression line on the chart
        chart.drawRegressionLine(coefficients[0], coefficients[1]);
        // todo move to separate program
        // If we have an input parameter, predict the price and draw the new point
//        if (args.length >= 1 && args[0] != null) {
//            // Estimate the linear function given the input data
//            double regressionParameters[] = Regression.getOLSRegression(
//                    chart.inputData, 0);
//            double x = Double.parseDouble(args[0]);
//
//            // Prepare a line function using the found parameters
//            LineFunction2D linefunction2d = new LineFunction2D(
//                    regressionParameters[0], regressionParameters[1]);
//            // This is the estimated price
//            double y = linefunction2d.getValue(x);
//
//            chart.drawInputPoint(x, y);
//        }
    }
}
