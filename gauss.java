import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.io.File;
import java.awt.image.Kernel;
import java.io.IOException;
import javax.imageio.ImageIO;


public class gauss {
    public static void main(String[] args) {
        File img = new File( "/home/gasha/AI_1st_sem/biometrics/eye.jpg" );
        BufferedImage a = null;
        try{
            a= ImageIO.read( img );
        }
        catch( Exception e ){
            System.out.print( e.toString() );
            return;
        }
        BufferedImage a9 = new BufferedImage( a.getWidth() , a.getHeight() , a.getType() );
        float[] matrix = {
                1/16f, 1/8f, 1/16f,
                1/8f, 1/4f, 1/8f,
                1/16f, 1/8f, 1/16f,
        };
        BufferedImageOp op = new ConvolveOp( new Kernel(3, 3, matrix) );
        a9 = op.filter( a, a9 );
        try{
            ImageIO.write( a9 , "jpg" ,new File( "/home/gasha/AI_1st_sem/biometrics/gauss_blur/eye1.jpg" ));
        }catch( Exception e ){
            System.out.println( e );
        }
    }
}

