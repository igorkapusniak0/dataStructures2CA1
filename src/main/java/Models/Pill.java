package Models;

import javafx.scene.image.Image;

public class Pill {
    private String name;
    private String description;
    private Image image;
    private int number;
    private int size;

    public Pill(String name, String description, Image image, int number, int size){
        setName(name);
        setDescription(description);
        setImage(image);
        setNumber(number);
        setSize(size);
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public Image getImage(){
        return this.image;
    }
    public int getSize(){
        return this.size;
    }
    public int getNumber(){
        return this.number;
    }
}
