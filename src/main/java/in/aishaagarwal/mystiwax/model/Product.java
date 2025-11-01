package in.aishaagarwal.mystiwax.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a product entity stored in MongoDB.
 * It includes a reference (imageId) to the file stored in GridFS.
 */
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String imageId; // Reference to the file in GridFS

    // Constructors
    public Product() {}

    public Product(String name, String description, double price, String imageId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageId = imageId;
    }

    // Getters and Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }

    public int getQuantity() { // Getter for quantity
        return quantity;
    }

    public void setQuantity(int quantity) { // Setter for quantity
        this.quantity = quantity;
    }
}
