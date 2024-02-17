package Controllers;

import API.API;
import Models.Pill;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;

public class MainController {

    private HashMap<Integer, HashMap> pillMap = new HashMap<>();
    private Image image;
    private Image processedImage;
    private Image blackAndWhiteImage;
    private Image rebuild;
    private Image finalImage;
    private int x;
    private int y;
    private int setCount = 0;
    private Rectangle rectangle;
    Color selectedColour;
    @FXML
    TextField nameTextField = new TextField();
    @FXML
    TextField descriptionTextField = new TextField();
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
    Slider toleranceSlider = new Slider();
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
    TabPane tabPane = new TabPane();

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

    public void findColour(){
        blackAndWhiteImage = API.convertToBlackAndWhite(processedImage,selectedColour,0.1);
        imageView.setImage(blackAndWhiteImage);
        littleImageView3.setImage(blackAndWhiteImage);
    }
    public void noiseReduction(){
        int[] pixels = getPixels();
        API.countUniqueSets(pixels);
        rebuild = API.rebuildImage(pixels);
        littleImageView4.setImage(rebuild);
        imageView.setImage(rebuild);
    }
    private int count = 0;
    public void locatePills(){
        HashMap map = API.getSets(getPixels());
        pillMap.putIfAbsent(count,map);
        drawLocatingRectangles(map,image);
        imageView.setImage(image);
    }
    private int[] getPixels(){
        int[] pixels = API.findWhite(blackAndWhiteImage);
        pixels = API.unionFind(blackAndWhiteImage, pixels);
        pixels = API.noiseFilter(pixels, (int) toleranceSlider.getValue());
        return pixels;
    }

    public void addPills(){
        if (pillMap!=null){
            LinkedList pills = pillList(image,pillMap.get(count),nameTextField.getText(), descriptionTextField.getText());
            addTab(String.valueOf(count), pills);
            count+=1;
        }
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
    @FXML
    private void resetImageView(){
        Pane pane = (Pane) imageView.getParent();
        pane.getChildren().removeIf(node -> "pillRectangle".equals(node.getUserData()) || "pillLabel".equals(node.getUserData()));
    }




    private LinkedList<Pill> pillList(Image image, HashMap<Integer,LinkedList<Integer>> hashMap, String name, String description){
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        int totalSize = height * width;
        int count = 0;
        LinkedList<Pill> pills = new LinkedList<>();

        for (Map.Entry<Integer, LinkedList<Integer>> entry : hashMap.entrySet()) {
            LinkedList<Integer> list = entry.getValue();
            if (list == null || list.isEmpty()) continue;

            int minX = width;
            int minY = totalSize;
            int maxX = 0;
            int maxY = 0;

            for (Integer pixelIndex : list) {
                int x = pixelIndex % width;
                int y = pixelIndex / width;

                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }
            Image subImage =API.getSubImage(image,minX,minY,maxX,maxY);
            Pill pill = new Pill(name,description ,subImage, count,list.size());
            pills.add(pill);
        }
        return pills;
    }


    private void drawLocatingRectangles(HashMap<Integer,LinkedList<Integer>> hashMap, Image image){
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        int totalSize = height * width;
        int count = 0;
        Pane pane = (Pane) imageView.getParent();
        pane.getChildren().removeIf(node -> "pillRectangle".equals(node.getUserData()) || "pillLabel".equals(node.getUserData()));

        for (Map.Entry<Integer, LinkedList<Integer>> entry : hashMap.entrySet()) {
            LinkedList<Integer> list = entry.getValue();
            if (list == null || list.isEmpty()) continue;

            int minX = width;
            int minY = totalSize;
            int maxX = 0;
            int maxY = 0;

            for (Integer pixelIndex : list) {
                int x = pixelIndex % width;
                int y = pixelIndex / width;

                if (x < minX) minX = x;
                if (x > maxX) maxX = x;
                if (y < minY) minY = y;
                if (y > maxY) maxY = y;
            }
            count+=1;
            Rectangle rectangle = new Rectangle(minX+5, minY, maxX - minX+2, maxY - minY);
            /*rectangle.setOnMouseEntered(mouseEvent -> {
                x = (int) mouseEvent.getX();
                y = (int) mouseEvent.getY();
                Pane pillPane = new Pane();
                pillPane.setLayoutX(x);
                pillPane.setLayoutY(y);
                pillPane.setMaxSize(50,50);
                Label label = new Label("shalom");
                pillPane.getChildren().add(label);
                ((Pane) imageView.getParent()).getChildren().add(pillPane);
            });*/
            rectangle.setStroke(Color.RED);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setUserData("pillRectangle");
            ((Pane) imageView.getParent()).getChildren().add(rectangle);
            Label label = new Label("Pill: "+ count);
            label.setLayoutX(minX);
            label.setLayoutY(maxY);
            label.setUserData("pillLabel");
            ((Pane) imageView.getParent()).getChildren().add(label);
        }
    }

    private void addTab(String title,LinkedList<Pill> pills){
        Tab tab = new Tab(title);
        Pill pill = pills.get(0);

        VBox vBox = new VBox();
        Label name = new Label("Name: "+pill.getName());
        Label description = new Label("Description: "+pill.getDescription());
        ImageView subImageView = new ImageView();


        subImageView.setImage(pill.getImage());
        subImageView.setFitWidth(100);
        subImageView.setFitHeight(100);
        subImageView.setPreserveRatio(true);
        vBox.getChildren().addAll(name,description,subImageView);

        tab.setContent(vBox);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == tab) {
                System.out.println(title);
                drawLocatingRectangles(pillMap.get(Integer.parseInt(title)), image);
            }
        });
    }




}