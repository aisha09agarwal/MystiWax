// src/main/java/in/aishaagarwal/mystiwax/repository/ProductRepository.java

package in.aishaagarwal.mystiwax.repository;

import in.aishaagarwal.mystiwax.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    // You can define custom queries here if needed
}