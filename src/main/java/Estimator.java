import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.sound.midi.Soundbank;

public class Estimator extends ApplicationFrame {
    XYDataset inputData;
    JFreeChart chart;

    public static void main(String[] args) throws IOException {
        Estimator demo = new Estimator("data.csv");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

        // Draw the regression line on the chart
        demo.drawRegressionLine();

        // If we have an input parameter, predict the price and draw the new point
        if (args.length >= 1 && args[0] != null) {
            // Estimate the linear function given the input data
            double regressionParameters[] = Regression.getOLSRegression(
                    demo.inputData, 0);
            double x = Double.parseDouble(args[0]);

            // Prepare a line function using the found parameters
            LineFunction2D linefunction2d = new LineFunction2D(
                    regressionParameters[0], regressionParameters[1]);
            // This is the estimated price
            double y = linefunction2d.getValue(x);

            demo.drawInputPoint(x, y);
        }
    }

    public Estimator(String inputFileName) throws IOException {
        super("Technobium - Linear Regression");

        // Read sample data from prices.txt file
        inputData = createDatasetFromFile(inputFileName);

        // Create the chart using the sample data
        chart = createChart(inputData);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public XYDataset createDatasetFromFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        Scanner scanner = new Scanner(file);
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Real estate item");

        // Skip headers
        scanner.next();
        while (scanner.hasNext()) {
            try {
                String[] res = scanner.next().split(",");
                long km = Long.parseLong(res[0]);
                long price = Long.parseLong(res[1]);
                series.add(km, price);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Parsing file error!");
                System.exit(1);
            }
        }
        scanner.close();
        dataset.addSeries(series);

        return dataset;
    }

    private JFreeChart createChart(XYDataset inputData) throws IOException {
        // Create the chart using the data read from the prices.txt file
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Price for auto with km", "Price", "Km", inputData,
                PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = chart.getXYPlot();
        plot.getRenderer().setSeriesPaint(0, Color.blue);
        return chart;
    }

    private void drawRegressionLine() {
        // Get the parameters 'a' and 'b' for an equation y = a + b * x,
        // fitted to the inputData using ordinary least squares regression.
        // a - regressionParameters[0], b - regressionParameters[1]
        double regressionParameters[] = Regression.getOLSRegression(inputData,
                0);

        // Prepare a line function using the found parameters
        LineFunction2D linefunction2d = new LineFunction2D(
                regressionParameters[0], regressionParameters[1]);

        // Creates a dataset by taking sample values from the line function
        XYDataset dataset = DatasetUtilities.sampleFunction2D(linefunction2d,
                0D, 300, 100, "Fitted Regression Line");

        // Draw the line dataset
        XYPlot xyplot = chart.getXYPlot();
        xyplot.setDataset(1, dataset);
        XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(
                true, false);
        xylineandshaperenderer.setSeriesPaint(0, Color.YELLOW);
        xyplot.setRenderer(1, xylineandshaperenderer);
    }

    private void drawInputPoint(double x, double y) {
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
