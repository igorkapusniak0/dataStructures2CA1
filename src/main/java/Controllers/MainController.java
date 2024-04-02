package Controllers;

import API.API;
import Models.Pill;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;

public class MainController {
    private HashMap<Integer, HashMap> pillMap = new HashMap<>();
    private Image image;
    private ArrayList<int[]> pixelArray= new ArrayList<>();
    private Image processedImage;
    private int count = 0;
    private Image blackAndWhiteImage;
    private Image rebuild;
    private Image finalImage;
    private Image finalImage2;
    private int x;
    private int y;
    private Rectangle rectangle1;
    private Rectangle rectangle2;
    Color selectedColour1;
    Color selectedColour2;

    @FXML
    TextField nameTextField = new TextField();
    @FXML
    TextField descriptionTextField = new TextField();
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
    Slider toleranceColourSlider1 = new Slider();
    @FXML
    Slider toleranceColourSlider2 = new Slider();
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
    ImageView littleImageView6 = new ImageView();
    @FXML
    TabPane tabPane = new TabPane();
    @FXML
    Rectangle rectangleColour1 = new Rectangle();
    @FXML
    Rectangle rectangleColour2 = new Rectangle();
    @FXML
    Label totalPillsLabel = new Label();



    @FXML
    public void initialize() {
        redIntensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (image != null) {
                processedImage = API.processedImage(image, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
                imageView.setImage(processedImage);
                littleImageView2.setImage(processedImage);

            }
        });
        greenIntensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (image != null) {
                processedImage = API.processedImage(image, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
                imageView.setImage(processedImage);
                littleImageView2.setImage(processedImage);
            }
        });
        blueIntensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (image != null) {
                processedImage = API.processedImage(image, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
                imageView.setImage(processedImage);
                littleImageView2.setImage(processedImage);
            }
        });
        littleImageView1.setOnMouseClicked(mouseEvent -> imageView.setImage(image));
        littleImageView2.setOnMouseClicked(mouseEvent -> imageView.setImage(processedImage));
        littleImageView3.setOnMouseClicked(mouseEvent -> imageView.setImage(blackAndWhiteImage));
        littleImageView4.setOnMouseClicked(mouseEvent -> imageView.setImage(rebuild));
        littleImageView5.setOnMouseClicked(mouseEvent -> imageView.setImage(finalImage));
        littleImageView6.setOnMouseClicked(mouseEvent -> imageView.setImage(finalImage2));

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
        if (selectedColour2!=null){
            blackAndWhiteImage = API.convertToBlackAndWhite(processedImage,selectedColour1,selectedColour2, toleranceColourSlider1.getValue(), toleranceColourSlider2.getValue());

        }
        if (selectedColour2==null){
            blackAndWhiteImage = API.convertToBlackAndWhite(processedImage,selectedColour1, toleranceColourSlider1.getValue());

        }
        imageView.setImage(blackAndWhiteImage);
        littleImageView3.setImage(blackAndWhiteImage);

    }
    public void noiseReduction(){
        if (getPixels()!=null){
            int[] pixels = getPixels();
            rebuild = API.rebuildImage(pixels);
            littleImageView4.setImage(rebuild);
            imageView.setImage(rebuild);
        }
    }
    public void locatePills(){
        if (getPixels()!=null){
            HashMap map = API.getSets(getPixels());
            pillMap.putIfAbsent(count,map);
            Pane pane = (Pane) imageView.getParent();
            pane.getChildren().removeIf(node -> "pillRectangle".equals(node.getUserData()) || "pillLabel".equals(node.getUserData()));
            drawLocatingRectangles(map,image);
            imageView.setImage(image);
            finalImage = API.colourSeparateSetsImage(image,map);
            imageView.setImage(finalImage);
            littleImageView5.setImage(finalImage);
        }

    }
    private int[] getPixels(){
        int[] pixels = API.findWhite(blackAndWhiteImage);
        if (blackAndWhiteImage!=null){
            pixels = API.unionFind(blackAndWhiteImage, pixels);
            pixels = API.noiseFilter(pixels, (int) toleranceSlider.getValue());
            pixelArray.add(pixels);
        }
        return pixels;
    }

    private int totalPills(){
        int totalCount=0;
        if (pillMap!=null){
            for (Map.Entry<Integer, HashMap> pillTypes : pillMap.entrySet()){
                HashMap<Integer, LinkedList<Integer>> pills = pillTypes.getValue();
                for (Map.Entry<Integer, LinkedList<Integer>> pill : pills.entrySet()){
                    totalCount+=1;
                }
            }
        }
        return totalCount;
    }

    public void addPills(){
        if (pillMap!=null && image!=null){
            LinkedList pills = pillList(image,pillMap.get(count),nameTextField.getText(), descriptionTextField.getText());
            addTab(String.valueOf(count), pills);
            count+=1;
            finalImage2 = API.colourSampledSetsImage(image,pillMap);
            littleImageView6.setImage(finalImage2);

        }
        totalPillsLabel.setText("Total number of Pills: " + totalPills());
    }




    private void clickToGetColour(){
        imageView.setOnMouseClicked(mouseEvent ->  {
            if (image!=null){
                processedImage = API.processedImage(image, redIntensitySlider, greenIntensitySlider, blueIntensitySlider);
                littleImageView2.setImage(processedImage);
                imageView.setImage(processedImage);

                if (mouseEvent.getButton() == MouseButton.PRIMARY){
                    x = (int) mouseEvent.getX();
                    y = (int) mouseEvent.getY();
                    System.out.println("X= " + x + ", Y= " + y);
                    selectedColour1 = processedImage.getPixelReader().getColor(x,y);
                    rectangleColour1.setFill(selectedColour1);

                    if (rectangle1 != null) {
                        ((Pane) imageView.getParent()).getChildren().remove(rectangle1);
                    }
                    rectangle1 = API.selectRectangle(imageView, x-5,y-5,x+5,y+5);
                    rectangle1.setUserData("colourLocation1");
                    ((Pane) imageView.getParent()).getChildren().add(rectangle1);
                    System.out.println(selectedColour1);
                }
                if (mouseEvent.getButton() == MouseButton.SECONDARY){
                    x = (int) mouseEvent.getX();
                    y = (int) mouseEvent.getY();
                    System.out.println("X= " + x + ", Y= " + y);
                    selectedColour2 = processedImage.getPixelReader().getColor(x,y);
                    rectangleColour2.setFill(selectedColour2);

                    if (rectangle2 != null) {
                        ((Pane) imageView.getParent()).getChildren().remove(rectangle2);
                    }
                    rectangle2 = API.selectRectangle(imageView, x-5,y-5,x+5,y+5);
                    rectangle2.setUserData("colourLocation2");
                    ((Pane) imageView.getParent()).getChildren().add(rectangle2);
                    System.out.println(selectedColour2);
                }
            }
        });
    }


    @FXML
    private void resetImageView(){
        Pane pane = (Pane) imageView.getParent();
        pane.getChildren().removeIf(node -> node instanceof Rectangle || node instanceof Label);
        pane.getChildren().removeIf(node -> "colourLocation1".equals(node.getUserData()) || "colourLocation2".equals(node.getUserData()));
        selectedColour1 = null;
        selectedColour2 = null;
        blueIntensitySlider.setValue(0);
        redIntensitySlider.setValue(0);
        greenIntensitySlider.setValue(0);
        toleranceSlider.setValue(0);
        toleranceColourSlider1.setValue(0);
        toleranceColourSlider2.setValue(0);

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
            count++;
            Image subImage =API.getSubImage(image,minX,minY,maxX,maxY);
            Pill pill = new Pill(name,description ,subImage, count,list.size());
            pills.add(pill);
        }
        return pills;
    }
    @FXML
    public void drawAllLocatingRectangles(){
        System.out.println(pillMap.size());
        for (int i = 0; i<pillMap.size();i++){
            drawLocatingRectangles(pillMap.get(i),image);System.out.println(i);
        }
    }


    private void drawLocatingRectangles(HashMap<Integer,LinkedList<Integer>> hashMap, Image image){
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        int totalSize = height * width;
        int count = 0;

        Pane pane = (Pane) imageView.getParent();

        List<Map.Entry<Integer, LinkedList<Integer>>> sortedEntries = new ArrayList<>(hashMap.entrySet());
        sortedEntries.sort(Comparator.comparingInt(entry -> {
            LinkedList<Integer> list = entry.getValue();
            int minY = list.stream().mapToInt(pixelIndex -> pixelIndex / width).min().orElse(0);
            return minY;
        }));

        for (Map.Entry<Integer, LinkedList<Integer>> entry : sortedEntries) {
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
            Label label = new Label("Pill: "+ count);
            label.setLayoutX(minX);
            label.setLayoutY(maxY);
            label.setUserData(entry.getKey());
            ((Pane) imageView.getParent()).getChildren().add(label);
            Rectangle rectangle = new Rectangle(minX+5, minY, maxX - minX+2, maxY - minY);
            rectangle.setUserData(entry.getKey());
            rectangle.setOnMouseClicked(mouseEvent -> {
                System.out.println("del");
                hashMap.remove(rectangle.getUserData());
                pane.getChildren().remove(rectangle);
                pane.getChildren().remove(label);

            });
            rectangle.setStroke(Color.RED);
            rectangle.setFill(Color.TRANSPARENT);
            ((Pane) imageView.getParent()).getChildren().add(rectangle);
        }
    }

    private void addTab(String title,LinkedList<Pill> pills){
        Tab tab = new Tab(title);
        Pill pill = pills.get(0);

        VBox vBox = new VBox();
        Label name = new Label("Name: "+pill.getName());
        Label description = new Label("Description: "+pill.getDescription());
        Label pillNumber = new Label("Number of Pills: " + pills.size());
        int totalCount = 0;
        for (Pill pill1:pills){
            totalCount+= pill1.getSize();
        }
        Label totalSize = new Label("Total Size of Pills: " + totalCount);
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #ffffff; -fx-pref-width: 100px;-fx-pref-height: 3px;-fx-stroke-width: 3px;");
        vBox.getChildren().addAll(name, description, pillNumber, totalSize, separator);
        ScrollPane scrollPane = new ScrollPane();
        for (Pill pill1:pills){
            ImageView subImageView = new ImageView();
            subImageView.setImage(pill1.getImage());
            Label index = new Label("Index of pill: " + pill1.getNumber());
            Label size = new Label("Size of set: "+pill1.getSize());
            subImageView.setFitWidth(100);
            subImageView.setFitHeight(100);
            subImageView.setPreserveRatio(true);
            vBox.getChildren().addAll(index,size,subImageView);
        }

        vBox.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(vBox);

        tab.setContent(scrollPane);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == tab) {
                System.out.println(title);
                Pane pane = (Pane) imageView.getParent();
                pane.getChildren().removeIf(node -> node instanceof Rectangle || node instanceof Label);
                drawLocatingRectangles(pillMap.get(Integer.parseInt(title)), image);
            }
        });
    }



}


