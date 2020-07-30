
// RGB histogram manipulation
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;


public class histogram_equalization {

    private static final int BINS = 256;
    private final BufferedImage image = getImage();
    private HistogramDataset dataset;
    private XYBarRenderer renderer;

    private BufferedImage getImage() {
        File file = null;
        try {
            file = new File("/home/gasha/AI_1st_sem/biometrics/Last_hope/noNoisy.png");
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    private ChartPanel createChartPanel() {
        // dataset
        dataset = new HistogramDataset();
        Raster raster = image.getRaster();
        final int w = image.getWidth();
        final int h = image.getHeight();
        double[] r = new double[w * h];
        r = raster.getSamples(0, 0, w, h, 0, r);
        dataset.addSeries("Red", r, BINS);
        r = raster.getSamples(0, 0, w, h, 1, r);
        dataset.addSeries("Green", r, BINS);
        r = raster.getSamples(0, 0, w, h, 2, r);
        dataset.addSeries("Blue", r, BINS);
        // chart
        JFreeChart chart = ChartFactory.createHistogram("Histogram", "Value",
                "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());
        // translucent red, green & blue
        Paint[] paintArray = {
                new Color(0x80ff0000, true),
                new Color(0x8000ff00, true),
                new Color(0x800000ff, true)
        };
        plot.setDrawingSupplier(new DefaultDrawingSupplier(
                paintArray,
                DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.add(new JCheckBox(new VisibleAction(0)));
        panel.add(new JCheckBox(new VisibleAction(1)));
        panel.add(new JCheckBox(new VisibleAction(2)));
        return panel;
    }

    private class VisibleAction extends AbstractAction {

        private final int i;

        public VisibleAction(int i) {
            this.i = i;
            this.putValue(NAME, (String) dataset.getSeriesKey(i));
            this.putValue(SELECTED_KEY, true);
            renderer.setSeriesVisible(i, true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            renderer.setSeriesVisible(i, !renderer.getSeriesVisible(i));
        }
    }

    private void display() {
        JFrame f = new JFrame("Histogram");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(createChartPanel());
        f.add(createControlPanel(), BorderLayout.SOUTH);
        f.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new histogram_equalization ().display();
        });
    }
}


/*
import java.util.*;

import java.awt.*;

import java.awt.event.*;

import java.awt.image.*;

import java.awt.geom.*;

import java.io.*;

import javax.imageio.*;

// Main class

public class histogram_equalization extends Frame implements ActionListener {

    BufferedImage input;

    int width, height;

    TextField texRad, texThres;

    ImageCanvas source, target;

    PlotCanvas plot;

    // Constructor

    public histogram_equalization(String name) {

        super("Image Histogram");

        // load image

        try {

            input =

                    ImageIO.read(newFile("/home/gasha/AI_1st_sem/biometrics/eye.jpg"));

        }

        catch ( Exception ex ) {

            ex.printStackTrace();

        }

        width = input.getWidth();

        height = input.getHeight();

        // prepare the panel for image canvas.

        Panel main = new Panel();

        source = new ImageCanvas(input);

        plot = new PlotCanvas();

        target = new ImageCanvas(input);

        main.setLayout(new GridLayout(1, 3, 10, 10));

        main.add(source);

        main.add(plot);

        main.add(target);

        // prepare the panel for buttons.

        Panel controls = new Panel();

        Button button = new Button("Display Histogram");

        button.addActionListener(this);

        controls.add(button);

        button = new Button("Histogram Stretch");

        button.addActionListener(this);

        controls.add(button);

        controls.add(new Label("Cutoff fraction:"));

        texThres = new TextField("10", 2);

        controls.add(texThres);

        button = new Button("Aggressive Stretch");

        button.addActionListener(this);

        controls.add(button);

        button = new Button("Histogram Equalization");

        button.addActionListener(this);

        controls.add(button);

        // add two panels

        add("Center", main);

        add("South", controls);

        addWindowListener(new ExitListener());

        setSize(width*2+400, height+100);

        setVisible(true);

    }

    class ExitListener extends WindowAdapter {

        public void windowClosing(WindowEvent e) {

            System.exit(0);

        }

    }

    // Action listener for button click events

    public void actionPerformed(ActionEvent e) {

        //compute the average color for the image

        if ( ((Button)e.getSource()).getLabel().equals("Display Histogram") ) {

            float red=0, green=0, blue=0;

            for ( int y=0, i=0 ; y<height ; y++ )

                for ( int x=0 ; x<width ; x++, i++ ) {

                    Color clr = new Color(input.getRGB(x, y));

                    red += clr.getRed();

                    green += clr.getGreen();

                    blue += clr.getBlue();

                }

            red /= width * height;

            green /= width * height;

            blue /= width * height;

            plot.setMeanColor(new Color((int)red,(int)green,(int)blue));

        }



    }

    public static void main(String[] args) {

        new histogram_equalization(args.length==1 ? args[0] : "out.png");

    }

}

*/

/*
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;



public class histogram_equalization {
    public static void main(String args[]) throws IOException {
        BufferedImage img = null;
        File file = null;

        //read image
        try {
            file = new File("/home/gasha/AI_1st_sem/biometrics/eye.jpg");
            img = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println(e);

        }

        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();

        int anzpixel = width * height;
        int[] histogram = new int[255];
        int[] iarray = new int[1];
        int i = 0;


        //read pixel intensities into histogram
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                int valueBefore = img.getRaster().getPixel(x, y, iarray)[0];
                histogram[valueBefore]++;
            }
        }

        int sum = 0;
        // build a Lookup table LUT containing scale factor
        int [] lut = new int[anzpixel];
        int [] d = new int [anzpixel];
        for (i = 0; i < 255; ++i) {
            sum += histogram[i];
            d[i] = sum / anzpixel;
            lut[i] = (d[i] - d[0])/(1-d[0]) * 255;
        }

        // transform image using sum histogram as a Lookup table
        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {
                int valueBefore = img.getRaster().getPixel(x, y, iarray)[0];
                int valueAfter = lut[valueBefore];
                iarray[0] = valueAfter;
                img.getRaster().setPixel(x, y, iarray);
            }
        }

        //System.out.println("The image is successfully to Grayscale");
    }//main() ends here

}
*/