import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

import java.util.Random;

public class Pie_chart {

    private static final Random random = new Random();
    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {// create a dataset...
        first_pie_chart();
    }

    public static DefaultPieDataset generate_data_set(int number){
        DefaultPieDataset dataset = new DefaultPieDataset();

        for(int i = 0;i<number;i++){
            dataset.setValue("toy_"+(i+1), 10);
        }

        return dataset;
    }

    public static void first_pie_chart() {
        DefaultPieDataset dataset = generate_data_set(random.nextInt(1,15));
// create a chart...
        JFreeChart chart = ChartFactory.createPieChart(
                "Sample Pie Chart",
                dataset,
                true, // legend?
                false, // tooltips?
                true // URLs?
        );
// create and display a frame...
        ChartFrame frame = new ChartFrame("First", chart);
        frame.pack();
        frame.setVisible(true);

    }

}