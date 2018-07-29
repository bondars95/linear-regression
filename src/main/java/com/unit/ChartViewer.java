package com.unit;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class ChartViewer extends ApplicationFrame {
    XYDataset inputData;
    private JFreeChart chart;
    private double maxX;

    public ChartViewer(List<DatasetParser.DataSetEntry> dataSetEntries) throws IOException {
        super("Linear Regression");
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Real estate item");
        maxX = dataSetEntries.stream().map(DatasetParser.DataSetEntry::getX).max(Double::compare).orElse(0.);
        dataSetEntries.forEach(res -> series.add(res.getX(), res.getY()));
        dataset.addSeries(series);
        inputData = dataset;
        // Create the chart using the sample data
        chart = createChart(inputData);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    private JFreeChart createChart(XYDataset inputData) throws IOException {
        // Create the chart using the data read from the prices.txt file
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Price for auto with mileage km", "Mileage","Price", inputData,
                PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = chart.getXYPlot();
        plot.getRenderer().setSeriesPaint(0, Color.blue);
        return chart;
    }

    void drawRegressionLine(double a, double b) {
        // Prepare a line function using the found parameters
        LineFunction2D linefunction2d = new LineFunction2D(a, b);
        // Creates a dataset by taking sample values from the line function
        XYDataset dataset = DatasetUtilities.sampleFunction2D(
                linefunction2d, 0D, maxX, 100, "Regression Line");

        // Draw the line dataset
        XYPlot xyplot = chart.getXYPlot();
        xyplot.setDataset(1, dataset);
        XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true, false);
        xylineandshaperenderer.setSeriesPaint(0, Color.YELLOW);
        xyplot.setRenderer(1, xylineandshaperenderer);
    }

    void drawInputPoint(double x, double y) {
        // Create a new dataset with only one row
        XYSeriesCollection dataset = new XYSeriesCollection();
        String title = "Input area: " + x + ", Price: " + y;
        XYSeries series = new XYSeries(title);
        series.add(x, y);
        dataset.addSeries(series);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDataset(2, dataset);
        XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
        plot.setRenderer(2, renderer);
    }
}
