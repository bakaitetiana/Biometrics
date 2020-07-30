import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

import java.awt.image.*;
import java.io.*;
import java.util.*;


public class histogram_stretching {

    public static void main(String args[]) {
        System.out.println("hello");

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("/home/gasha/AI_1st_sem/biometrics/eye.jpg"));
            writeColorImageValueToFile(img);
        } catch (Exception e) {
        }
    }

    public static void writeColorImageValueToFile(BufferedImage in) {
        int width = in.getWidth();
        int height = in.getHeight();

        int min = 0;  //stretch min level
        int max = 31; //stretch max level


        System.out.println("width=" + width + " height=" + height);
        try {


            int[] r = new int[width * height];
            int[] g = new int[width * height];
            int[] b = new int[width * height];
            int[] e = new int[width * height];
            int[] data = new int[width * height];
            in.getRGB(0, 0, width, height, data, 0, width);



            int[] old_histogram_r = new int[256];
            int[] old_histogram_g = new int[256];
            int[] old_histogram_b = new int[256];

            int[] new_histogram_r = new int[256];
            int[] new_histogram_g = new int[256];
            int[] new_histogram_b = new int[256];

            for (int i = 0; i < (height * width); i++) {
                r[i] = (int) ((data[i] >> 16) & 0xff);  //shift 3rd byte to first byte location
                g[i] = (int) ((data[i] >> 8) & 0xff);   //shift 2nd byte to first byte location
                b[i] = (int) (data[i] & 0xff);          //it is already at first byte location

                old_histogram_r[r[i]]++;
                old_histogram_g[g[i]]++;
                old_histogram_b[b[i]]++;

                //stretch them to 0 to 255
                r[i] = (int) (1.0*( r[i] - min) / (max - min) * 255);
                g[i] = (int) (1.0*( g[i] - min) / (max - min) * 255);
                b[i] = (int) (1.0*( b[i] - min) / (max - min) * 255);

                if(r[i]> 255) r[i]=255;
                if(g[i]> 255) g[i]=255;
                if(b[i]> 255) b[i]=255;

                if(r[i]<0) r[i]=0;
                if(g[i]<0) g[i]=0;
                if(b[i]<0) b[i]=0;

                new_histogram_r[r[i]]++;
                new_histogram_g[g[i]]++;
                new_histogram_b[b[i]]++;

                //convert it back
                e[i] = (r[i] << 16) | (g[i] << 8) | b[i];

            }
            //convert e back to say jpg
            in.setRGB(0, 0, width, height, e, 0, width);
            ImageIO.write(in, "jpeg" /* "png" "jpeg" ... format desired */,
                    new File("newout.jpg") /* target */);

            PrintHistogram(old_histogram_r, "hist_before_r.txt"); //before stretchig ie original
            PrintHistogram(old_histogram_g, "hist_before_g.txt");
            PrintHistogram(old_histogram_b, "hist_before_b.txt");
            PrintHistogram(new_histogram_r, "new_histogram_r.txt");  //after stretching ie modified ones
            PrintHistogram(new_histogram_g, "new_histogram_g.txt");
            PrintHistogram(new_histogram_b, "new_histogram_b.txt");


        } catch (Exception e) {
            System.err.println("Error: " + e);
            Thread.dumpStack();

        }
    }

    static void PrintHistogram(int[] hist, String file) {
        try {
            FileWriter op = new FileWriter("/home/gasha/AI_1st_sem/"+file);

            for (int i = 0; i < hist.length; ++i) {
                op.write("[" + i + "]=" + hist[i]+"\n");
            }
            op.close();
        } catch (Exception e) {
            System.err.println("Error2: " + e);
            Thread.dumpStack();
        }
    }
}