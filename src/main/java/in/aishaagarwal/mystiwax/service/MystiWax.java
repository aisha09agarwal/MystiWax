package in.aishaagarwal.mystiwax.service;

import in.aishaagarwal.mystiwax.model.Product;
import java.util.List;

public interface MystiWax {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(String id);
    Product updateProduct(String id, Product product);
    void deleteProduct(String id);
}
