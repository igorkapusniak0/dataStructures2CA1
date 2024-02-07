package API;

import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class API {
    public static Image detectColour(Image image, Slider redIntensitySlider, Slider greenIntensitySlider, Slider blueIntensitySlider){
        if (image != null){
            int height = (int) image.getHeight();
            int width = (int) image.getWidth();

            double redIntensity = redIntensitySlider.getValue();
            double greenIntensity = greenIntensitySlider.getValue();
            double blueIntensity = blueIntensitySlider.getValue();

            WritableImage writableImage = new WritableImage(width,height);
            PixelReader pixelReader = image.getPixelReader();
            PixelWriter pixelWriter = writableImage.getPixelWriter();

            for (int x = 0; x<width; x++){
                for (int y = 0; y<height; y++){
                    Color colour = pixelReader.getColor(x,y);


                    if (colour.getRed()>redIntensity && colour.getGreen()>greenIntensity && colour.getBlue()>blueIntensity){
                        pixelWriter.setColor(x,y,Color.rgb(255,255,255));
                    }else{
                        pixelWriter.setColor(x,y,Color.rgb(0,0,0));
                    }
                }
            }
            return writableImage;
        }
        else {
            return null;
        }
    }

    public static void unionFind(Image blackAndWhiteImage){
        if (blackAndWhiteImage != null) {
            int height = (int) blackAndWhiteImage.getHeight();
            int width = (int) blackAndWhiteImage.getWidth();
            ArrayList coordinates = new ArrayList<Integer[]>();
            PixelReader pixelReader = blackAndWhiteImage.getPixelReader();

            for (int x = 0; x<width; x++){
                for (int y = 0; y<height; y++){
                    Color colour = pixelReader.getColor(x,y);
                    if (colour.getRed()==1 && colour.getGreen()==1 && colour.getBlue()==1){
                        int[] pixel = new int[2];
                        pixel[0] = x;
                        pixel[1] = y;
                        coordinates.add(pixel);
                    }
                    else{
                    System.out.println(x+","+y+"Black");}
                }
                }
        }
    }
    public static int find(int[] a,int id){
        while (a[id]==id){
            a[id]=a[a[id]];
            id=a[id];
        }
        return id;
    }
}
