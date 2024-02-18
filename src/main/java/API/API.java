package API;

import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class API {
    public static Image processedImage(Image image, Slider hueSlider, Slider saturationSlider, Slider brightnessSlider) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                Color color = pixelReader.getColor(x, y);
                    double hue = (color.getHue() + 360 * hueSlider.getValue()) % 360;
                    double saturation = Math.max(0, Math.min(color.getSaturation() * (1 + saturationSlider.getValue()), 1));
                    double brightness = Math.max(0, Math.min(color.getBrightness() * (1 + brightnessSlider.getValue()), 1));

                    pixelWriter.setColor(x, y, Color.hsb(hue, saturation, brightness));

            }
        }
        return writableImage;
    }
    public static Image convertToBlackAndWhite(Image image, Color pixelColour1, double tolerance) {
        return convertToBlackAndWhite(image, pixelColour1, null, tolerance,1);
    }

    public static Image convertToBlackAndWhite(Image image, Color pixelColour1, Color pixelColour2, double tolerance1, double tolerance2) {
        if (image == null || (pixelColour1 == null)) return null;

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);

                boolean matchesColour1 = pixelColour1 != null && matchesColour(color, pixelColour1, tolerance1);
                boolean matchesColour2 = pixelColour2 != null && matchesColour(color, pixelColour2, tolerance2);

                if (matchesColour1 || matchesColour2) {
                    pixelWriter.setColor(x, y, Color.WHITE);
                } else {
                    pixelWriter.setColor(x, y, Color.BLACK);
                }
            }
        }

        return writableImage;
    }

    private static boolean matchesColour(Color color, Color targetColour, double tolerance) {
        boolean hueDifference = Math.abs(targetColour.getHue() - color.getHue()) <= tolerance;
        boolean saturationDifference = Math.abs(targetColour.getSaturation() - color.getSaturation()) <= tolerance;
        boolean brightnessDifference = Math.abs(targetColour.getBrightness() - color.getBrightness()) <= tolerance;
        boolean redDifference = Math.abs(targetColour.getRed() - color.getRed()) <= tolerance;
        boolean greenDifference = Math.abs(targetColour.getGreen() - color.getGreen()) <= tolerance;
        boolean blueDifference = Math.abs(targetColour.getBlue() - color.getBlue()) <= tolerance;

        return hueDifference && saturationDifference && brightnessDifference || redDifference && greenDifference && blueDifference;
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
        int number = 0;
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] < 0) {
                number++;
            }
        }
        return number
                ;
    }
    public static HashMap getSets(int[] pixels){
        HashMap<Integer, LinkedList> store = new HashMap();
        for (int i = 0 ; i<pixels.length;i++){
            if (pixels[i] < 0){
                LinkedList<Integer> link = new LinkedList();
                link.add(i);
                store.put(i,link);
            }
        }
        for (int i = 0 ; i<pixels.length;i++){
            if (pixels[i]!= pixels.length+1){
                int root = find(pixels,i);
                LinkedList<Integer> link = store.get(root);
                link.add(i);
            }
        }
        return store;
    }
    public static Image getSubImage(Image image, int startX, int startY, int endX, int endY) {
        int width = endX - startX;
        int height = endY - startY;
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixelReader.getArgb(startX + x, startY + y);
                pixelWriter.setArgb(x, y, pixel);
            }
        }

        return writableImage;
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
