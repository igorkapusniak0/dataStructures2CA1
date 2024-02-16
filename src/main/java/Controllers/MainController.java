package Controllers;

import API.API;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;

public class MainController {

    private HashSet<Integer> roots = new HashSet<>();
    private Image image;
    private Image processedImage;
    private Image blackAndWhiteImage;
    private Image rebuild;
    private Image finalImage;
    private int x;
    private int y;
    private Rectangle rectangle;
    Color selectedColour;
    @FXML
    Button findButton = new Button();
    @FXML
    ImageView imageView = new ImageView();
    @FXML
    Slider redIntensitySlider = new Slider();
    @FXML
    Slider greenIntensitySlider = new Slider();
    @FXML
    Slider blueIntensitySlider = new Slider();
    @FXML
    ImageView littleImageView1 = new ImageView();
    @FXML
    ImageView littleImageView2 = new ImageView();
    @FXML
    ImageView littleImageView3 = new ImageView();
    @FXML
    ImageView littleImageView4 = new ImageView();
    @FXML
    ImageView littleImageView5 = new ImageView();

    @FXML
    public void initialize() {
        redIntensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (image != null) {
                processedImage = API.processedImage(image, selectedColour, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
                imageView.setImage(processedImage);
                littleImageView2.setImage(processedImage);

            }
        });
        greenIntensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (image != null) {
                processedImage = API.processedImage(image, selectedColour, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
                imageView.setImage(processedImage);
                littleImageView2.setImage(processedImage);
            }
        });
        blueIntensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (image != null) {
                processedImage = API.processedImage(image, selectedColour, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
                imageView.setImage(processedImage);
                littleImageView2.setImage(processedImage);
            }
        });
        littleImageView1.setOnMouseClicked(mouseEvent -> {
            imageView.setImage(image);
        });
        littleImageView2.setOnMouseClicked(mouseEvent -> {
            imageView.setImage(processedImage);
        });
        littleImageView3.setOnMouseClicked(mouseEvent -> {
            imageView.setImage(blackAndWhiteImage);
        });
        littleImageView4.setOnMouseClicked(mouseEvent -> {
            imageView.setImage(rebuild);
        });
        littleImageView5.setOnMouseClicked(mouseEvent -> {
            imageView.setImage(finalImage);
        });

        clickToGetColour();

    }

    @FXML
    protected void openImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            image = new Image(selectedFile.toURI().toString(),500,500,false,false);

        }
        littleImageView1.setImage(image);
        imageView.setImage(image);
    }

    public void method(){
        blackAndWhiteImage = API.convertToBlackAndWhite(processedImage,selectedColour,0.1);
        imageView.setImage(blackAndWhiteImage);
        littleImageView3.setImage(blackAndWhiteImage);
    }




    @FXML
    protected void nextImage() throws IOException {
        processedImage = API.processedImage(image, selectedColour, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
        imageView.setImage(processedImage);
        littleImageView2.setImage(processedImage);
        int[] pixels = API.findWhite(blackAndWhiteImage);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output1.txt"))) {

            for (int i = 0; i < pixels.length; i++) {

                writer.write((i +", " + pixels[i]));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions or errors here
        }

        pixels = API.unionFind(blackAndWhiteImage, pixels);

        try (BufferedWriter writer2 = new BufferedWriter(new FileWriter("output2.txt"))) {
            for (int i = 0; i < pixels.length; i++) {
                writer2.write((i +", " + pixels[i]));
                writer2.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions or errors here
        }


        pixels = API.noiseFilter(pixels,40);

/*
        try (BufferedWriter writer3 = new BufferedWriter(new FileWriter("output3.txt"))) {
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i]!=pixels.length+1){
                    writer3.write(Integer.toString(pixels[API.find(pixels,i)]));
                    writer3.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions or errors here
        }*/

        rebuild = API.rebuildImage(pixels);
        littleImageView4.setImage(rebuild);
        imageView.setImage(rebuild);
        finalImage = API.colorSets(rebuild,pixels);
        littleImageView5.setImage(finalImage);
        System.out.println(API.countUniqueSets(pixels));
        //drawLocatingRectangles(pixels);
    }




    @FXML
    protected void previousImage(){
        imageView.setImage(image);

    }


    private void clickToGetColour(){
        imageView.setOnMouseClicked(mouseEvent ->  {
                x = (int) mouseEvent.getX();
                y = (int) mouseEvent.getY();
                System.out.println("X= " + x + ", Y= " + y);
                selectedColour = image.getPixelReader().getColor(x,y);

                if (rectangle != null) {
                    ((Pane) imageView.getParent()).getChildren().remove(rectangle);
                }
                rectangle = API.selectRectangle(imageView, x-5,y-5,x+5,y+5);
                ((Pane) imageView.getParent()).getChildren().add(rectangle);

                System.out.println(selectedColour);

            processedImage = API.processedImage(image,selectedColour, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
            littleImageView2.setImage(processedImage);
            imageView.setImage(processedImage);

        });
    }

    private void drawLocatingRectangles(int[] pixels){
        ((Pane)imageView.getParent()).getChildren().removeIf(r->r instanceof Rectangle);
        ((Pane)imageView.getParent()).getChildren().removeIf(t->t instanceof Text);

        for (int i =0; i<pixels.length; i++){
            if (pixels[i] < 0){
                roots.add(i);
            }
        }

        for (int roots: roots){
            int minX = (int) imageView.getImage().getWidth();
            int minY = (int) imageView.getImage().getHeight();
            int maxX = 0;
            int maxY = 0;

            for (int i = 0; i<pixels.length;i++){
                if (pixels[i]<0){
                    int rootX = i % (int) image.getWidth();
                    int rootY = (int) Math.floor(i / (int) image.getWidth());

                    if (rootX < minX) minX = rootX;
                    if (rootY < minY) minY = rootY;
                    if (rootX > maxX) maxX = rootX;
                    if (rootY > maxY) maxY = rootY;
                }
            }
            Rectangle rec =  API.locatingRectangle(imageView,minX, minY, maxX, maxY);
            ((Pane) imageView.getParent()).getChildren().add(rec);
        }
    }

}