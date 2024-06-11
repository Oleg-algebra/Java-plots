/* ----------------
 * BarExample1.java
 * ----------------
 * (C) Copyright 2006, by Object Refinery Limited.
 *
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;

/**
 * A simple demonstration application showing how to create a bar chart.
 */
public class Bar_chart extends ApplicationFrame {
    /**
     * Creates a new demo instance.
     *
     * @param title the frame title.
     */
    public Bar_chart(String title) {
        super(title);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1000, "salary","january" );
        dataset.addValue(900, "salary", "february");
        dataset.addValue(1020, "salary", "march");
        dataset.addValue(2000, "salary", "april");
        dataset.addValue(500, "salary", "may");
        dataset.addValue(788, "salary", "june");
        dataset.addValue(4566,"gift","april");
        dataset.addValue(394,"expenses","february");
        dataset.addValue(567,"expenses","april");
        dataset.addValue(230,"expenses","june");

        JFreeChart chart = ChartFactory.createBarChart(
                "Bar Chart Demo", // chart title
                "Month", // domain axis label
                "Money, $", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                false, // tooltips?
                false // URLs?
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        plot.setBackgroundPaint(Color.lightGray);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.gray);
        renderer.setSeriesPaint(1, Color.orange);
        renderer.setSeriesPaint(2,Color.green);

        renderer.setItemMargin(0.0);

        ChartPanel chartPanel = new ChartPanel(chart, false);
        renderer.setDrawBarOutline(false);plot.setRangeGridlinePaint(Color.white);
        chartPanel.setPreferredSize(new Dimension(1000, 500));
        setContentPane(chartPanel);
    }
    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {
        Bar_chart demo = new Bar_chart("Bar Demo 1");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}