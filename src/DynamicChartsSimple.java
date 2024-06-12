import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DynamicChartsSimple extends JPanel {

    private TimeSeries timeSeries;
    private final long maxNumber = 100;
    private long step = 0;

    public DynamicChartsSimple(int maxAge) {
        super(new BorderLayout());
        this.timeSeries = new TimeSeries("Test Data");
        this.timeSeries.setMaximumItemAge(maxAge);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(this.timeSeries);

        DateAxis domain = new DateAxis("Time, t");
        domain.setAutoRange(true);
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);
//        domain.setTickLabelsVisible(true);

        NumberAxis range = new NumberAxis("f(t)");
        range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.red);
        renderer.setSeriesStroke(0,new BasicStroke(3f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));

        XYPlot plot = new XYPlot(dataset,domain,range,renderer);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setBackgroundPaint(Color.gray);

        JFreeChart chart = new JFreeChart("Test Plot",plot);
        chart.setBackgroundPaint(Color.lightGray);

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel);

    }
    /**
     * Adds an observation to the ’total memory’ time series.
     *
     * @param y the total memory used.
     */
    private void addObservation(double y) {
        Millisecond t = new Millisecond();
        this.timeSeries.add(t, Math.cos(step*Math.PI*2/maxNumber));
        step  = step + 1 % maxNumber;
    }
    /**
     * The data generator.
     */
    class DataGenerator extends Timer implements ActionListener {
        /**
         * Constructor.
         *
         * @param interval the interval (in milliseconds)
         */
        DataGenerator(int interval) {
            super(interval, null);
//            System.out.println(this.getClass().getName());
            addActionListener(this);
        }
        /**
         * Adds a new free/total memory reading to the dataset.
         *
         * @param event the action event.
         */
        public void actionPerformed(ActionEvent event) {
            long t = Runtime.getRuntime().totalMemory();
            addObservation(t);
        }
    }
    /**
     * Entry point for the sample application.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Memory Usage Demo");
        int maxAge = 10000;
        DynamicChartsSimple panel = new DynamicChartsSimple(maxAge);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setBounds(200, 120, 1000, 500);
        frame.setVisible(true);
        panel.new DataGenerator(100).start();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
