package Controllers;

import API.API;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController {
    private Image image = null;
    private Image blackAndWhite = null;
    @FXML
    ImageView imageView = new ImageView();
    @FXML
    Slider redIntensitySlider = new Slider();
    @FXML
    Slider greenIntensitySlider = new Slider();
    @FXML
    Slider blueIntensitySlider = new Slider();
    @FXML
    Slider hueIntensitySlider = new Slider();
    @FXML
    Slider saturationIntensitySlider = new Slider();
    @FXML
    Slider brightnessIntensitySlider = new Slider();

    int hueTolerance = 50;
    double saturationTolerance = .2;
    double brightnessTolerance = .2;
    @FXML
    public void initialize() {
        redIntensitySlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            blackAndWhite = API.detectColour(image, redIntensitySlider,greenIntensitySlider,blueIntensitySlider,hueIntensitySlider.getValue(),saturationIntensitySlider.getValue(),brightnessIntensitySlider.getValue());
            imageView.setImage(blackAndWhite);
        });
        greenIntensitySlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            blackAndWhite = API.detectColour(image, redIntensitySlider,greenIntensitySlider,blueIntensitySlider,hueIntensitySlider.getValue(),saturationIntensitySlider.getValue(),brightnessIntensitySlider.getValue());
            imageView.setImage(blackAndWhite);
        });
        blueIntensitySlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            blackAndWhite = API.detectColour(image,redIntensitySlider,greenIntensitySlider,blueIntensitySlider,hueIntensitySlider.getValue(),saturationIntensitySlider.getValue(),brightnessIntensitySlider.getValue());
            imageView.setImage(blackAndWhite);
        });
        hueIntensitySlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            blackAndWhite = API.detectColour(image, redIntensitySlider,greenIntensitySlider,blueIntensitySlider,hueIntensitySlider.getValue(),saturationIntensitySlider.getValue(),brightnessIntensitySlider.getValue());
            imageView.setImage(blackAndWhite);
        });
        saturationIntensitySlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            blackAndWhite = API.detectColour(image, redIntensitySlider,greenIntensitySlider,blueIntensitySlider,hueIntensitySlider.getValue(),saturationIntensitySlider.getValue(),brightnessIntensitySlider.getValue());
            imageView.setImage(blackAndWhite);
        });
        brightnessIntensitySlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            blackAndWhite = API.detectColour(image,redIntensitySlider,greenIntensitySlider,blueIntensitySlider,hueIntensitySlider.getValue(),saturationIntensitySlider.getValue(),brightnessIntensitySlider.getValue());
            imageView.setImage(blackAndWhite);
        });


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
        imageView.setImage(image);
    }

    @FXML
    protected void nextImage(){
        blackAndWhite = API.detectColour(image, redIntensitySlider, greenIntensitySlider, blueIntensitySlider,hueIntensitySlider.getValue(),saturationIntensitySlider.getValue(),brightnessIntensitySlider.getValue());
        imageView.setImage(blackAndWhite);
        int[] pixels = new int[(int) image.getHeight()* (int) image.getWidth()];
        pixels = API.findWhite(blackAndWhite);
        unionFind(pixels);
    }
    @FXML
    protected void previousImage(){
        imageView.setImage(image);
    }

    private void unionFind(int[] pixels){
        for (int i = 0; i<pixels.length; i++){
            int down = i+(int) blackAndWhite.getWidth();
            int right = i+1;
            if (pixels[i]!=-1 && pixels[right]!= -1 && right<pixels.length){
                API.unionByHeight(pixels,i,right);
            }
            if (pixels[i] != -1 && pixels[down]!=-1 && down<=pixels.length-blackAndWhite.getHeight()){
                API.unionByHeight(pixels,i,down);
            }
        }
    }






}