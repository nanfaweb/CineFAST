package com.example.a1_smd;

public class Snack {
    private String name;
    private String description;
    private int price;
    private int imageResId;
    private int quantity;

    public Snack(String name, String description, int price, int imageResId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }

    public int getTotalPrice() {
        return price * quantity;
    }
}
