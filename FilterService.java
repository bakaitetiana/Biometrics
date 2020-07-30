import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Yousra
 */
public class FilterService {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {



        System.out.println("Please Enter Your Image Path Here ...");
        Scanner myscanner = new Scanner(System.in);
        String path = myscanner.next();
        BufferedImage img = getImage("/home/gasha/AI_1st_sem/biometrics/eye.jpg");
        int filtersize = 3;


        BufferedImage outimg = TDfilter(img, filtersize);
        JFrame frame = new JFrame();
        JLabel image = new JLabel(new ImageIcon("imageName.png"));
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.getContentPane().add(new JLabel(new ImageIcon(outimg)));
        frame.pack();
        frame.setVisible(true);




    }

    public static BufferedImage TDfilter (BufferedImage img, int filtersize){

        int w = img.getWidth();
        int h = img.getHeight();
        WritableRaster cr=img.getRaster();
        WritableRaster wr=img.copyData(null);

        double[][] imgarray = Img2D(img);
        double[][] x = new double[filtersize][filtersize];
        Matrix filter = Matrix.filter(filtersize, filtersize);
        filter.show();
        Matrix imgm = new Matrix(w,h);;
        Matrix result;

        for (int ii = 0; ii < w; ii++)
            for (int jj = 0; jj < h; jj++) {
                for (int i = ii; i < filtersize + ii; i++) {
                    for (int j = jj; j < filtersize + jj; j++) {
                        if (i - filtersize / 2 < 0 || i - filtersize / 2  >= w || j- filtersize / 2  < 0 || j- filtersize / 2  >= h) {
                            x[i-ii][j-jj] = 0;
                            // imgm = new Matrix(x);
                        } else {
                            x[i-ii][j-jj] = imgarray[i - filtersize / 2][j - filtersize / 2];
                        };


                    }

                }
                imgm = new Matrix(x);
                result = imgm.multiply(filter);
                double value = result.average();
                wr.setSample(ii, jj, 0, value);
            }



        BufferedImage img2= new BufferedImage(w, h, img.getType());
        img2.setData(wr);
        return img2;

    }

    public static double [][] Img2D(BufferedImage img) {


        int w = img.getWidth();
        int h = img.getHeight();
        double[][] imgarray = new double[w][h] ;
        Raster raster = img.getData();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                imgarray[i][j] = raster.getSample(i, j, 0);
            }
        }
        return imgarray;
    }
    public static BufferedImage getImage(String imageName) {
        try {
            File input = new File(imageName);
            BufferedImage image = ImageIO.read(input);
            return image;
        } catch (IOException ie) {
            System.out.println("Error:" + ie.getMessage());
        }
        return null;
    }
}

