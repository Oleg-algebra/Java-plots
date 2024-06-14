import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DynamicBarChart extends JPanel {

    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private final String row1Name = "categories";
    private final ArrayList<String> months = new ArrayList<>();
    private final Random random = new Random();
    private JFreeChart chart;
    private int counter;
    private ArrayList<BufferedImage> images = new ArrayList<>();
    private int frameNumber = 1;

    public DynamicBarChart(int frameNumber) {
        this.frameNumber = frameNumber;
        setLayout(new BorderLayout());
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");

        initializeDatasSet();

        chart = ChartFactory.createBarChart(
                "Dynamic Bar Chart",
                "Month",
                "Revenue Value, $",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel);
    }

    private void initializeDatasSet() {
        for(String month : months) {
            dataset.setValue(0,row1Name,month);
        }
    }

    private void updateDataSet() {
        int updatedColumnsNumber = random.nextInt(1,months.size());
        for (int i = 0; i < updatedColumnsNumber; i++) {
            String month = months.get(random.nextInt(months.size()));
            double oldValue = (double) dataset.getValue(row1Name,month);
            double newValue = oldValue + random.nextDouble(1000);
            dataset.setValue(newValue,row1Name,month);
        }

    }

    private void updateChartTitle(){
        chart.setTitle("Dynamic Bar Chart t= "+counter);
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
            addActionListener(this);
        }
        /**
         * Adds a new free/total memory reading to the dataset.
         *
         * @param event the action event.
         */
        public void actionPerformed(ActionEvent event) {

            updateDataSet();
            updateChartTitle();
            try {
//
                // retrieve image
                BufferedImage bi = chart.createBufferedImage(1000,500);
                images.add(bi);
//                File outputfile = new File("images/saved"+counter+".png");
//                ImageIO.write(bi, "png", outputfile);
                counter++;      //update frame counter
                if(counter > frameNumber){
                    createVideo();
                    System.out.println("Video created");
                    System.exit(0);
                }
            } catch (Exception e) {
                System.out.println("Error creating image");
                System.exit(0);
            }

        }
    }

    private void createVideo(){
        int fps = 10;
        try {
            AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(
                    new File("images/video"+(counter-1)+".mp4"), fps); // 25 fps
            for (BufferedImage image : images) {
                encoder.encodeImage(image);
            }
            encoder.finish();
        }catch (IOException e){
            e.printStackTrace();
        }

    }




    public static void main(String[] args) {
        int interval = 50;
        int frameNumber = 120;
        if(args.length != 0){
            frameNumber = Integer.parseInt(args[0]);
        }
        JFrame frame = new JFrame("DynamicBarChart");
        DynamicBarChart panel = new DynamicBarChart(frameNumber);
        panel.new DataGenerator(interval).start();
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setBounds(new Rectangle(200,200,1000,500));

        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
