package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HelloController {
    private Image image = null;
    private Image blackAndWhite = null;
    FileChooser fileChooser = new FileChooser();
    @FXML
    ImageView originalImageView = new ImageView();
    @FXML
    ImageView blackAndWhiteImageView = new ImageView();
    @FXML
    Slider greenIntensitySlider = new Slider();
    @FXML
    Slider redIntensitySlider = new Slider();
    @FXML
    Slider blueIntensitySlider = new Slider();


    @FXML
    protected void openImage() {
        blackAndWhiteImageView.setOpacity(0);
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null){
            image = new Image(selectedFile.toString());
        }
        originalImageView.setImage(image);
    }

    @FXML
    protected void nextImage(){
        originalImageView.setOpacity(0);
        blackAndWhiteImageView.setOpacity(1);
        blackAndWhite = detectColour(redIntensitySlider,greenIntensitySlider,blueIntensitySlider);
        blackAndWhiteImageView.setImage(blackAndWhite);
    }
    @FXML
    protected void previousImage(){
        originalImageView.setOpacity(1);
        blackAndWhiteImageView.setOpacity(0);
    }

    private Image detectColour(Slider redIntensitySlider, Slider greenIntensitySlider, Slider blueIntensitySlider){
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




}