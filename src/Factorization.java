import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Factorization extends JPanel {

    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private final String row1Name = "factors";
    private JFreeChart chart;
    private int counter = 2;
    private ArrayList<BufferedImage> images = new ArrayList<>();
    private long maxNumber = 1000;
    private ArrayList<Long> primeNumbers = new ArrayList<>();
    private HashMap<Long,Long> factorsHash = new HashMap<>();


    public Factorization() {
        setLayout(new BorderLayout());


        chart = ChartFactory.createBarChart(
                "Factorization",
                "Factors",
                "Factors count",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel);
    }

    private boolean isPrime(long number) {

        int nRoot = (int) Math.sqrt(number);
        for (int i = 2; i <= nRoot; i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    private long getFactor(long number) {
        long factor = 2;
        for(long prime : primeNumbers) {
            factor = prime;
            if (number % prime == 0) {
                return factor;
            }
        }
        for (long f = factor; f < (int) Math.sqrt(number)+1; f++) {
            if(isPrime(f)){
                primeNumbers.add(f);
                if(number % f == 0) {
                    factor = f;
                }
            }
        }
        return factor;
    }

    private ArrayList<Long> factors(long number) {
        ArrayList<Long> factors = new ArrayList<>();
        while(number > 1){
            if(isPrime(number)){
                primeNumbers.add(number);
                factors.add(number);
                number = 1;
            }else{
                long factor = getFactor(number);
                factors.add(factor);
                number /= factor;
            }

        }
        return factors;
    }

    private void clearData(){
        for(long factor : factorsHash.keySet()){
            factorsHash.put(factor,0L);
            dataset.setValue(0,row1Name,String.valueOf(factor));
        }

    }

    private void updateDataSet() {
        ArrayList<Long> factors = factors(counter);
        clearData();
        for(long factor : factors) {
            if(!factorsHash.containsKey(factor)) {
                factorsHash.put(factor, 1L);
            }else{
                factorsHash.compute(factor, (k,v) -> v+1);
            }
        }
        Collections.sort(factors);

        for(long factor : factors) {
            dataset.addValue((double)factorsHash.get(factor), row1Name, String.valueOf(factor));
        }

    }

    private void updateChartTitle(){
        chart.setTitle("Factorization N= "+counter);
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
                if(counter > maxNumber){
                    createVideo();
                    System.out.println("Video created");
                    System.exit(0);
                }
                // retrieve image
                BufferedImage bi = chart.createBufferedImage(1000,500);
                images.add(bi);

                counter++;      //update frame counter

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
                    new File("images/video"+counter+".mp4"), fps); // 25 fps
            for (BufferedImage image : images) {
                encoder.encodeImage(image);
            }
            encoder.finish();
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public static void startAnimation(){
        int interval = 1000;
        JFrame frame = new JFrame("Factorization animation");
        Factorization panel = new Factorization();
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

    public static void testFactorization(){
        System.out.println("Enter your number to factor:");
        long number = new Scanner(System.in).nextLong();
        ArrayList<Long> factors = new Factorization().factors(number);
        System.out.printf("factors of %d are: ", number);
        System.out.println(factors);
    }

    public static void main(String[] args) {
//        testFactorization();
        startAnimation();
    }

}
