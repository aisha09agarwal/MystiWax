// src/main/java/in/aishaagarwal/mystiwax/controller/ProductRestController.java

package in.aishaagarwal.mystiwax.controller;

import in.aishaagarwal.mystiwax.model.Product;
import in.aishaagarwal.mystiwax.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products") // Added a base mapping for better API structure
public class ProductRestController {

    @Autowired
    private ProductService productService;

    // FIX for "The method getAll() is undefined..."
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAll();
    }
    
    // FIX for "The method getById(String) is undefined..."
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // FIX for "The method create(Product) is undefined..."
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // Assumes this endpoint is for data only, without file upload
        return productService.create(product);
    }
    
    // The image serving endpoint is kept, but its path is now absolute to simplify:
    @GetMapping("/images/{gridFsId}")
    public ResponseEntity<ByteArrayResource> serveImage(@PathVariable String gridFsId) {
        // ... (The implementation remains the same as before)
        try {
            byte[] data = productService.getFileContent(gridFsId);

            if (data == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) 
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"image.png\"")
                    .body(new ByteArrayResource(data));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}