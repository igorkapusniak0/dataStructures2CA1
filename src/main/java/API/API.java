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
    public static Image detectColour(Image image, Slider hueSlider, Slider saturationSlider, Slider brightnessSlider, double hueTolerance, double saturationTolerance, double brightnessTolerance) {
        if (image == null) return null;

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        double hueValue = hueSlider.getValue();
        double saturationValue = saturationSlider.getValue();
        double brightnessValue = brightnessSlider.getValue();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);

                boolean matchesHue = color.getHue() >= hueValue - hueTolerance && color.getHue() <= hueValue + hueTolerance;
                boolean matchesSaturation = color.getSaturation() >= saturationValue - saturationTolerance && color.getSaturation() <= saturationValue + saturationTolerance;
                boolean matchesBrightness = color.getBrightness() >= brightnessValue - brightnessTolerance && color.getBrightness() <= brightnessValue + brightnessTolerance;

                if (matchesHue && matchesSaturation && matchesBrightness) {
                    pixelWriter.setColor(x, y, Color.rgb(255, 255, 255));
                } else {
                    pixelWriter.setColor(x, y, Color.rgb(0, 0, 0));
                }
            }
        }

        return writableImage;
    }


    public static int[] findWhite(Image blackAndWhiteImage){
        if (blackAndWhiteImage != null) {
            int height = (int) blackAndWhiteImage.getHeight();
            int width = (int) blackAndWhiteImage.getWidth();
            Color white = new Color(1,1,1,1);
            int[] pixels = new int[height*width];
            PixelReader pixelReader = blackAndWhiteImage.getPixelReader();

            for (int y = 0; y<width; y++){
                for (int x = 0; x<height; x++){
                    Color colour = pixelReader.getColor(x,y);
                    if (colour.equals(white)){
                        pixels[x+y] = x;
                    }else{
                        pixels[x+y] = -1;
                    }
                }
            }
            return pixels;
        }else {
            return null;
        }
    }
    public static int find(int[] a, int id) {
        return a[id] == id ? id : (a[id] = find(a, a[id]));
    }
    public static void unionByHeight(int[] a, int p, int q) {
        int rootp=find(a,p);
        int rootq=find(a,q);
        int deeperRoot=a[rootp]<a[rootq] ? rootp : rootq;
        int shallowerRoot=deeperRoot==rootp ? rootq : rootp;
        int temp=a[shallowerRoot];
        a[shallowerRoot]=deeperRoot;
        if(a[deeperRoot]==temp) a[deeperRoot]--;
    }
    public static void unionBySize(int[] a, int p, int q) {
        int rootp=find(a,p);
        int rootq=find(a,q);
        int biggerRoot=a[rootp]<a[rootq] ? rootp : rootq;
        int smallerRoot=biggerRoot==rootp ? rootq : rootp;
        int smallSize=a[smallerRoot];
        a[smallerRoot]=biggerRoot;
        a[biggerRoot]+=smallSize;
    }

}
