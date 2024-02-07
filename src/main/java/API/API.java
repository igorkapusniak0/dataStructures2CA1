package API;

import Set.SetNode;
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


    public static int[] method(Image blackAndWhiteImage, int[] pixels){
        if (blackAndWhiteImage != null) {
            int height = (int) blackAndWhiteImage.getHeight();
            int width = (int) blackAndWhiteImage.getWidth();
            Color white = new Color(1,1,1,1);
            PixelReader pixelReader = blackAndWhiteImage.getPixelReader();

            for (int x = 0; x<width; x++){
                for (int y = 0; y<height; y++){
                    Color colour = pixelReader.getColor(x,y);
                    if (colour.equals(white)){
                        pixels[x * width + y] = x * width + y;
                    }
                    else{

                        pixels[x * width + y] = -1;
                    }
                }
            }
            return pixels;
        }else {
            return null;
        }
    }
    public static SetNode<?> find(SetNode<?> node) {
        return node.parent==null ? node : (find(node.parent));
    }
    public static int find(int[] a, int id) {
        return a[id] == id ? id : (a[id] = find(a, a[id]));
    }
        public static void unionBySize(SetNode<?> p, SetNode<?> q) {
        SetNode<?> rootp=find(p);
        SetNode<?> rootq=find(q);
        SetNode<?> biggerRoot=rootp.size>=rootq.size ? rootp : rootq;
        SetNode<?> smallerRoot=biggerRoot==rootp ? rootq : rootp;
        smallerRoot.parent=biggerRoot;
        biggerRoot.size+=smallerRoot.size;
    }
}
