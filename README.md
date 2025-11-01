# MystiWax: Aesthetic Candle E-Commerce Platform

## 🎯 Executive Summary (For Recruiters)

**MystiWax** is a full-stack, Java-based e-commerce solution demonstrating proficiency in **Server-Side Rendering (SSR)** and robust data persistence.

* **Core Skills:** Java, Spring Boot, Spring Data, Thymeleaf, RESTful API Design.

* **Database:** MongoDB/GridFS for scalable product and binary asset management.

* **Impact:** Built a highly aesthetic, responsive product catalog with dynamic inventory status and efficient image streaming from GridFS.

---

## 🌟 Overview

**MystiWax** is a modern, single-page e-commerce application designed around a high-end, aesthetic user experience. The project is implemented using the **Spring Boot** framework, employing a **Server-Side Rendering (SSR)** architecture driven by **Thymeleaf** for dynamic HTML generation.

The application leverages a **RESTful API backend** (implemented via `ProductRestController.java`) to manage core data, while the UI layer is rendered by the `PageController`. Persistent storage is provided by **MongoDB**, utilizing **GridFS** for efficient storage and streaming of product media assets.

## ✨ Technical Features & Implementation

* **Server-Side Rendering (SSR):** Frontend templates are dynamically hydrated with data using **Thymeleaf**, ensuring fast initial page loads and search engine optimization.

* **Decoupled Image Service (GridFS):** Product images are stored in a dedicated GridFS bucket and retrieved via a specific image streaming endpoint (`/image/{imageId}`). This approach prevents database bloat and handles large binaries efficiently.

* **Microservice-Style Endpoints:** Core CRUD operations are exposed via a dedicated REST controller (`ProductRestController`), maintaining separation of concerns from the page rendering logic (`PageController`).

* **Dynamic Catalog Filtering:** The homepage displays a subset of the catalog (first 3 products) fetched using stream processing (`.stream().limit(3)`) for quick featured content presentation.

* **Stock Status Logic:** Frontend availability status ("In Stock" / "Out of Stock") is conditionally rendered in Thymeleaf based on the `quantity` field's value.

---

## ⚙️ Project Architecture & Dependencies

The application relies on core Spring Boot starters and specialized data handlers:

| Component | Spring Technology/Library | Role |
| :--- | :--- | :--- |
| **Framework** | `spring-boot-starter-web` | Provides core infrastructure and Tomcat server. |
| **UI Engine** | `spring-boot-starter-thymeleaf` | Server-side template processing for view rendering. |
| **Data Access** | `spring-boot-starter-data-mongodb` | Manages MongoDB connections, Repositories, and basic data mapping. |
| **Image Storage** | `spring-data-mongodb` (GridFS) | Handles file storage and retrieval using `GridFsTemplate`. |
| **Data Serialization** | `ProductRepository` | Interface extension of `MongoRepository` for simplified data access. |

---

## 🚀 Getting Started

These instructions will guide you through setting up and running the application.

### Prerequisites

* Java Development Kit (JDK) 17 or higher (for modern Spring Boot versions)
* Maven 3.6+ (for project build and dependency management)
* A running **MongoDB instance** (version 4.0 or higher is recommended).

### Installation

1.  **Clone the Repository (if applicable):**

    ```
    git clone (https://github.com/aisha09agarwal/MystiWax)
    cd mystiwax
    ```

2.  **Configure MongoDB:**
    Set the MongoDB connection URI and the GridFS bucket name in your `application.properties` or `application.yml`.

    ```properties
    # application.properties example
    spring.data.mongodb.uri=mongodb://localhost:27017/mystiwax
    # Configures the GridFS bucket used by GridFsTemplate for image storage
    spring.data.mongodb.gridfs.bucket=productImages
    ```

3.  **Run the Application:**
    Start the embedded Tomcat server using the Maven wrapper:

    ```
    ./mvnw spring-boot:run
    ```

### Key Endpoints

| Endpoint Type | URL Pattern | Description | Controller/Service Integration |
| :--- | :--- | :--- | :--- |
| **Page View (SSR)** | `http://localhost:8080/` | Renders the homepage template (`index.html`). | `PageController.indexPage()` calls `ProductService.findAllProducts()`. |
| **Page View (SSR)** | `http://localhost:8080/products` | Renders the full catalog page (`products.html`). | `PageController.productsPage()` calls `ProductService.getAll()`. |
| **REST Image Stream** | `http://localhost:8080/image/{imageId}` | Streams binary image data back to the browser. | `PageController.handleImageRequest()` uses `ProductService.getImageResource()`. |

---

## 📦 Data Model (`Product.java`)

The MongoDB document mapping, annotated with `@Document`:

```java
public class Product {
    @Id
    private String id; // Maps to MongoDB's ObjectId
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity; // Inventory tracking
    private String imageId; // Key reference to the GridFS file ID
}
