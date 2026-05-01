package com.example.a1_smd;

public class Snack {
    private int id;
    private String name;
    private String description;
    private int price;
    private String imageName;
    private int imageResId;
    private int quantity;

    public Snack(int id, String name, String description, int price, String imageName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageName = imageName;
        this.quantity = 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public String getImageName() { return imageName; }
    
    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public void incrementQuantity() { this.quantity++; }
    public void decrementQuantity() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }
    
    public int getTotalPrice() { return price * quantity; }
}
