package shop;

import java.util.Objects;

public class Product {
    private final long id;
    private String name;
    private ProductCategory category;
    private double price;

    public Product(long id, String name, ProductCategory category, double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною: " + price);
        }
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public ProductCategory getCategory() { return category; }
    public double getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setCategory(ProductCategory category) { this.category = category; }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною: " + price);
        }
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return id == ((Product) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', category=%s, price=%.2f}",
                id, name, category, price);
    }
}
