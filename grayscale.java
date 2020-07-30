import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/*
 * @ Conversion to a grayscale
 * @ Color inversion
 * @ Contrast modification
 * @ Brightness modification
 */


public class grayscale {
    public static void main(String args[])throws IOException{
        BufferedImage img = null;
        File file = null;

        //read image
        try{
            file = new File("/home/gasha/AI_1st_sem/biometrics/eye.jpg");
            img = ImageIO.read(file);
        }catch(IOException e){
            System.out.println(e);

        }

        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        int factor = 85;

        //convert to greyscale
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);

                /* int a = (p>>24)&0xff; */

/*
                int r = getR(p);
                int g = getG(p);
                int b = getB(p);
*/
                //luminance method of conversion images to a greyscale
                /* int grey = (int) (r * 0.299 + g * 0.587 + b * 0.114); */
                /* int a = toRGB(r, g, b); */


                //replace RGB value with luminance method
                /*p = (a<<24) | (grey<<16) | (grey<<8) | grey; */


                //adding factor to rgb values
                int r = getR(p) - factor;
                int g = getG(p) - factor;
                int b = getB(p) - factor;


                if (r >= 256) {
                    r = 255;
                } else if (r < 0) {
                    r = 0;
                }

                if (g >= 256) {
                    g = 255;
                } else if (g < 0) {
                    g = 0;
                }

                if (b >= 256) {
                    b = 255;
                } else if (b < 0) {
                    b = 0;
                }

                int colorInverse = toRGB(255 - r,
                        255 - g,
                        255 - b);

                img.setRGB(x, y, colorInverse);

            }
        }


        //write image
        try{
            file = new File("/home/gasha/AI_1st_sem/biometrics/Grayscale/out/eye.jpg");
            ImageIO.write(img, "jpeg", file);
        }catch(IOException e){
            System.out.println(e);
        }

        //System.out.println("The image is successfully to Grayscale");
    }//main() ends here

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

}
