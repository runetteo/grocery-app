package com.magenic.masters.item;

public class Item {

    private String name;
    private String unit;
    private double price;
    private double totalAmount;
	private int totalItemsInCart;
    private Category category;

	public Item(String name, double price, String unit, Category category) {
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public double getTotalAmount() {
		return totalAmount;
	}

	public double setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
		return totalAmount;
	}

	public int getTotalItemsInCart() {
		return totalItemsInCart;
	}

	public void setTotalItemsInCart(int totalItemsInCart) {
		this.totalItemsInCart = totalItemsInCart;
	}

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }
}
