package API;

import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class API {
    public static Image processedImage(Image image,Color pixelColor, Slider hueSlider, Slider saturationSlider, Slider brightnessSlider) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        double red = pixelColor.getRed();
        double green = pixelColor.getGreen();
        double blue = pixelColor.getBlue();

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                Color color = pixelReader.getColor(x, y);
                if ((color.getRed()>=pixelColor.getRed()-.2 && color.getRed()<=pixelColor.getRed()+.2) && (color.getGreen()>=pixelColor.getGreen()-.2 && color.getGreen()<=pixelColor.getGreen()+.2) && (color.getBlue()>=pixelColor.getBlue()-.2 && color.getBlue()<=pixelColor.getBlue()+.2)){
                    double hue = (color.getHue() + 360 * hueSlider.getValue()) % 360;
                    double saturation = Math.max(0, Math.min(color.getSaturation() * (1 + saturationSlider.getValue()), 1));
                    double brightness = Math.max(0, Math.min(color.getBrightness() * (1 + brightnessSlider.getValue()), 1));

                    pixelWriter.setColor(x, y, Color.hsb(hue, saturation, brightness));
                }
                else{
                    pixelWriter.setColor(x,y, color);
                }
            }
        }
        return writableImage;
    }
    public static Image convertToBlackAndWhite(Image image, Color pixelColour, double tolerance) {
        if (image == null || pixelColour == null) return null;

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                Color color = pixelReader.getColor(x, y);

                boolean hueDifference = (pixelColour.getHue() <= color.getHue()+tolerance) && (pixelColour.getHue() >= color.getHue()-tolerance);
                boolean saturationDifference = (pixelColour.getSaturation() <= color.getSaturation()+tolerance) && (pixelColour.getSaturation() >= color.getSaturation()-tolerance);
                boolean brightnessDifference = (pixelColour.getBrightness() <= color.getBrightness()+tolerance) && (pixelColour.getBrightness() >= color.getBrightness()-tolerance);
                boolean redDifference = (pixelColour.getRed() <= color.getRed()+tolerance) && (pixelColour.getRed() >= color.getRed()-tolerance);
                boolean greenDifference = (pixelColour.getGreen() <= color.getGreen()+tolerance) && (pixelColour.getGreen() >= color.getGreen()-tolerance);
                boolean blueDifference = (pixelColour.getBlue() <= color.getBlue()+tolerance) && (pixelColour.getBlue() >= color.getBlue()-tolerance);



                if ((hueDifference && saturationDifference && brightnessDifference) || (redDifference && greenDifference && blueDifference)) {
                    pixelWriter.setColor(x, y, Color.WHITE);
                } else {
                    pixelWriter.setColor(x, y, Color.BLACK);
                }
            }
        }

        return writableImage;
    }


    public static int[] findWhite(Image blackAndWhiteImage){
        if (blackAndWhiteImage != null) {
            int height = (int) blackAndWhiteImage.getHeight();
            int width = (int) blackAndWhiteImage.getWidth();
            int[] pixels = new int[height*width];
            PixelReader pixelReader = blackAndWhiteImage.getPixelReader();
            int index = 0;

            for (int y = 0; y<width; y++){
                for (int x = 0; x<height; x++){
                    Color colour = pixelReader.getColor(x,y);
                    if (colour.equals(Color.WHITE)){
                        pixels[index] = -1;
                    }else{
                        pixels[index] = pixels.length+1;
                    }
                    index+=1;
                }
            }
            return pixels;
        }else {
            return null;
        }
    }
    public static int find(int[] a, int id) {
        //System.out.println("index:"+id + " number:"+ a[id]);

        if(a[id] == a.length+1){
            return id;
        }
        else if (a[id]<0) {
            return id;
        }else {
            return find(a,a[id]);
        }
    }


    public static void unionBySize(int[] a, int p, int q) {

        int rootp=find(a,p);
        int rootq=find(a,q);

        if (rootp == rootq) return;

        int biggerRoot=a[rootp]<=a[rootq] ? rootp : rootq;
        int smallerRoot=biggerRoot==rootq ? rootp : rootq;

        a[biggerRoot]+=a[smallerRoot];
        a[smallerRoot]=biggerRoot;

    }
    /*public static void unionBySize(int[] parent, int p, int q) {
        int rootP = find(parent,p);
        int rootQ = find(parent,q);

        if (rootP == rootQ) return;

        if (parent[rootP] < parent[rootQ]) {
            parent[rootP] += parent[rootQ];
            parent[rootQ] = rootP;
        } else {
            parent[rootQ] += parent[rootP];
            parent[rootP] = rootQ;
        }
    }*/




    public static Rectangle selectRectangle(ImageView imageView, int minX, int minY, int maxX, int maxY){
        Rectangle rectangle = new Rectangle(minX, minY, maxX-minX, maxY-minY);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);
        rectangle.setLayoutX(imageView.getLayoutX());
        rectangle.setLayoutY(imageView.getLayoutY());
        return rectangle;
    }




    public static Image rebuildImage(int[] pixels){
        WritableImage writableImage = new WritableImage(500,500);
        int counter = 0;
        for (int y = 0; y<(int)Math.sqrt(pixels.length); y++){
            for (int x = 0; x<500; x++){
                if (pixels[counter]!=pixels.length+1){
                    writableImage.getPixelWriter().setColor(x,y,Color.WHITE);
                }
                else{
                    writableImage.getPixelWriter().setColor(x,y,Color.BLACK);

                }
                counter++;
            }
        }
        return writableImage;
    }

    public static Rectangle locatingRectangle(ImageView imageView, int minX, int minY, int maxX, int maxY){
        Rectangle rectangle = new Rectangle(minX, minY, maxX-minX, maxY-minY);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);
        rectangle.setLayoutX(imageView.getLayoutX());
        rectangle.setLayoutY(imageView.getLayoutY());
        return rectangle;
    }

    public static Image colorSets(Image originalImage, int[] pixelRoots) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();
        WritableImage coloredImage = new WritableImage(width, height);
        PixelWriter writer = coloredImage.getPixelWriter();

        // Map to store unique set roots to colors
        Map<Integer, Color> rootColorMap = new HashMap<>();

        // Populate the map with a unique color for each set
        for (int i = 0; i < pixelRoots.length; i++) {
            if (pixelRoots[i] != pixelRoots.length+1){
                int root = find(pixelRoots, i);
                rootColorMap.putIfAbsent(root, generateUniqueColor(root, rootColorMap.size()));
            }
        }

        // Color the pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                if (pixelRoots[index] != pixelRoots.length+1){
                    int root = find(pixelRoots, index);
                    if (pixelRoots[index] != pixelRoots.length + 1) { // Check if not black
                        Color color = rootColorMap.get(root);
                        writer.setColor(x, y, color);
                    } else {
                        writer.setColor(x, y, Color.BLACK); // Or any background color
                    }
                }
            }
        }

        return coloredImage;
    }

    public static int[] noiseFilter(int[] pixels, int tolerance) {
        for (int i = 0; i < pixels.length; i++){
            if (pixels[i]!= pixels.length+1){
                if (pixels[i]>-tolerance && pixels[i]<0) {
                    pixels[i] = pixels.length+1;
                }
            }
        }
        for (int i = 0; i < pixels.length; i++){
            if (pixels[i]!= pixels.length+1){
                if (pixels[find(pixels,i)]==pixels.length+1) {
                    pixels[i] = pixels.length+1;
                }
            }
        }
        return pixels;

    }
    public static int countUniqueSets(int[] pixels) {
        HashSet<Integer> uniqueRoots = new HashSet<>();
        for (int i = 0; i < pixels.length; i++) {
            // Skip elements that have been marked as not part of any set
            if (pixels[i] != pixels.length + 1) {
                int root = find(pixels, i);
                uniqueRoots.add(root);
            }
        }
        return uniqueRoots.size();
    }


    private static Color generateUniqueColor(int root, int count) {
        // Generate a unique color based on the root or count. This is a simplistic approach.
        // You may need a more sophisticated method for generating distinct colors if there are many sets.
        return Color.hsb((float) root / count * 360, 1.0, 1.0);
    }

    public static int[] unionFind(Image image, int[] pixels) {
        int width = (int) image.getWidth();
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] != pixels.length+1) {
                int down = i + width;
                int right = i + 1;

                System.out.println("i:" + i + "right:" + right);
                if (i % width < width - 1 && pixels[right] < pixels.length+1) {
                    API.unionBySize(pixels, i, right);
                }

                System.out.println("i:" + i + "down:" + down);
                if (down < pixels.length && pixels[down] < pixels.length+1) {
                    API.unionBySize(pixels, i, down);
                }
            }
        }
        return pixels;
    }




}
