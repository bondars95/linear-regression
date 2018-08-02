package com.unit;

import org.jfree.ui.RefineryUtilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Trainer {
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

        // writing to coef file
        BufferedWriter writer = new BufferedWriter(new FileWriter("coefficients.txt", false));
        writer.append(coefficients[0] + " " + coefficients[1]);
        writer.close();

        System.out.printf("Building regression line for: y = %.4f + %.4fx\n", coefficients[0], coefficients[1]);
        System.out.printf("MeanAbsolutePercentageError(MAPE): %.2f %%", model.getAccuracy() * 100);
        display(res, coefficients);
    }

    private static void display(List<DatasetParser.DataSetEntry> res, double[] coefficients) throws IOException {
        ChartViewer chart = new ChartViewer(res);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
        // Draw the regression line on the chart
        chart.drawRegressionLine(coefficients[0], coefficients[1]);
    }
}
