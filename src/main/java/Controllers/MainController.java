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
    ImageView originalImageView = new ImageView();
    @FXML
    ImageView blackAndWhiteImageView = new ImageView();
    @FXML
    Slider redIntensitySlider = new Slider();
    @FXML
    Slider greenIntensitySlider = new Slider();
    @FXML
    Slider blueIntensitySlider = new Slider();
    @FXML
    public void initialize() {
        redIntensitySlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            blackAndWhite = API.detectColour(image, redIntensitySlider,greenIntensitySlider,blueIntensitySlider);
            blackAndWhiteImageView.setImage(blackAndWhite);
        });
        greenIntensitySlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            blackAndWhite = API.detectColour(image, redIntensitySlider,greenIntensitySlider,blueIntensitySlider);
            blackAndWhiteImageView.setImage(blackAndWhite);
        });
        blueIntensitySlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            blackAndWhite = API.detectColour(image,redIntensitySlider,greenIntensitySlider,blueIntensitySlider);
            blackAndWhiteImageView.setImage(blackAndWhite);
        });


    }

    @FXML
    protected void openImage() {
        FileChooser fileChooser = new FileChooser();
        blackAndWhiteImageView.setOpacity(0);
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            image = new Image(selectedFile.toURI().toString());

        }
        originalImageView.setImage(image);
    }

    @FXML
    protected void nextImage(){
        originalImageView.setOpacity(0);
        blackAndWhiteImageView.setOpacity(1);
        blackAndWhite = API.detectColour(image, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
        blackAndWhiteImageView.setImage(blackAndWhite);
        API.unionFind(blackAndWhite);
    }
    @FXML
    protected void previousImage(){
        originalImageView.setOpacity(1);
        blackAndWhiteImageView.setOpacity(0);
    }






}