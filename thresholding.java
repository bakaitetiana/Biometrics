import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
* @ Thresholding
*/

public class thresholding{
    public static void main(String[] args)
    {
        try
        {
            BufferedImage original = ImageIO.read(new File("/home/gasha/AI_1st_sem/biometrics/lab2/out/eye.jpg"));
            BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
//int red;
            int green;
//int blue;
            int newPixel;
            int threshold = 155;
            for(int i=0; i<original.getWidth(); i++)
            {
                for(int j=0; j<original.getHeight(); j++)
                {
                    int p = original.getRGB(i,j);
                    // Get pixels
                    //red = getR(p);
                    green = getG(p);
                    //int alpha = (p >>> 24);
                    int alpha  = (p << 24) | 0x00ffffff;

                    if(green < threshold)
                    {
                        newPixel = 0;
                    }
                    else
                    {
                        newPixel = 255;
                    }
                    newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                    binarized.setRGB(i, j, newPixel);
                }
            }
            ImageIO.write(binarized, "jpg",new File("/home/gasha/AI_1st_sem/biometrics/thresholding/out/blackwhiteimage.jpg") );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static int getR (int in) {
        return (int) ((in << 8) >> 24) & 0xff;
    }

    private static int getG (int in) {
        return (int) ((in << 16) >> 24) & 0xff;
    }

    private static int getB (int in) {
        return (int) ((in << 24) >> 24) & 0xff;
    }

    private static int toRGB (int red, int green, int blue) {
        return (int) ((((red << 8) | green) << 8) | blue);
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }

}


/*
// Histogram equalization
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

public class ImageProcessor
{
public static BufferedImage convert(Image img)
{
    BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
    Graphics bg = bi.getGraphics();
    bg.drawImage(img, 0, 0, null);
    bg.dispose();
    return bi;
}

public static BufferedImage toGrayScale(Image img)
{
    // Convert image from type Image to BufferedImage
    BufferedImage bufImg = convert(img);

    // Scan through each row of the image
    for(int j=0; j < bufImg.getHeight(); j++)
    {
        // Scan through each columns of the image
        for(int i=0; i < bufImg.getWidth(); i++)
        {
            // Returns an integer pixel in the default RGB color model
            int values=bufImg.getRGB(i,j);
            // Convert the single integer pixel value to RGB color
            Color oldColor = new Color(values);

            int red = oldColor.getRed();        // get red value
            int green = oldColor.getGreen();    // get green value
            int blue = oldColor.getBlue();  // get blue value

            // Convert RGB to gray scale using formula
            // gray = 0.299 * R + 0.587 * G + 0.114 * B
            double grayVal = 0.299*red + 0.587*green + 0.114*blue;

            // Assign each channel of RGB with the same value
            Color newColor = new Color((int)grayVal, (int)grayVal, (int)grayVal);

            // Get back the integer representation of RGB color
            // and assign it back to the original position
            bufImg.setRGB(i, j, newColor.getRGB());
        }
    }
    // return back the resulting image in BufferedImage type
    return bufImg;
}

public static BufferedImage histEqualization(Image img)
{
    //Convert image to BufferedImage
    img = ImageProcessor.toGrayScale(img);
    BufferedImage bufImg = convert(img);


    //Getting information of each pixel;
    int[][] intensity = new int[bufImg.getWidth()][ bufImg.getHeight()];
    int[] counter = new int[256];
    for(int j=0; j < bufImg.getHeight();j++)
        for(int i=0; i < bufImg.getWidth();i++)
        {
            int values=bufImg.getRGB(i,j);
            Color oldColor = new Color(values);
            intensity[i][j] = oldColor.getBlue();
            counter[intensity[i][j]]++;
        }

    //BEGIN OF Histogram Equalization

    //find out how many rows the table have
    int row=0;

    for(int i=0;i<256;i++)
        if(counter[i]!=0)
            row++;

    //Find out the v column of the table
    //table[row][0] = v column
    //table[row][1] = c column
    int temp=0;
    int[][] table = new int[row][2];


    for(int i=0;i<256;i++)
        if(counter[i]!=0)
        {
            table[temp][0] = i;
            temp++;
        }

    //Find out the c column of the table
    for(int i=0;i<row;i++)
        table[i][1] = counter[table[i][0]];

    //C-> CS

    int sum = 0;

    for(int i=0;i<row;i++)
    {
        sum += table[i][1];
        table[i][1] = sum;
    }

    //CS->NCS
    int min = table[0][1], max = table[row-1][1];

    for(int i=0;i<row;i++)
        table[i][1] = Math.round((table[i][1]-min)/(max-min));

    //Mapping
    for(int j=0;j<bufImg.getHeight();j++)
        for(int i=0;i<bufImg.getWidth();i++)
        {
            for(int k=0;k<row;k++)
                if(intensity[i][j]==table[k][0])
                    intensity[i][j] = table[k][1];

            Color newColor = new Color(intensity[i][j], intensity[i][j], intensity[i][j]);

            bufImg.setRGB(i, j, newColor.getRGB());
        }


    return bufImg;
}

}

//return new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY) ;
 BufferedImage bufImg = convert(img);
 byte[] bufferbyte = ((DataBufferByte) bufImg.getRaster().getDataBuffer()).getData() ;
 for(int j=0, pos=0; j < bufImg.getHeight(); j++)
      for(int i=0; i < bufImg.getWidth(); i++, pos++)
           {
           int red   = bufImg().getSample(i, j, 0) ;
           int green = bufImg().getSample(i, j, 1) ;
           int blue  = bufImg().getSample(i, j, 2) ;
           // gray = 0.299 * R + 0.587 * G + 0.114 * B
           bufferbyte[pos] = (byte)(int)(0.299*red + 0.587*green + 0.114*blue) ;
           }
return bufImg ;
 */





