// package in.aishaagarwal.mystiwax.service;

// import com.mongodb.client.gridfs.GridFsFindIterable;
// import com.mongodb.client.gridfs.model.GridFSFile;
// import in.aishaagarwal.mystiwax.model.Product;
// import in.aishaagarwal.mystiwax.repository.ProductRepository;
// import org.bson.types.ObjectId;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.Resource;
// import org.springframework.data.mongodb.core.query.Criteria;
// import org.springframework.data.mongodb.core.query.Query;
// import org.springframework.data.mongodb.gridfs.GridFsResource;
// import org.springframework.data.mongodb.gridfs.GridFsTemplate;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.IOException;
// import java.util.List;
// import java.util.Objects;
// import java.util.Optional;

// @Service
// public class ProductService {

//     private final ProductRepository productRepository;
//     private final GridFsTemplate gridFsTemplate;

//     @Autowired
//     public ProductService(ProductRepository productRepository, GridFsTemplate gridFsTemplate) {
//         this.productRepository = productRepository;
//         this.gridFsTemplate = gridFsTemplate;
//     }

//     /**
//      * Retrieves all product documents from the MongoDB collection.
//      */
//     public List<Product> findAllProducts() {
//         return productRepository.findAll();
//     }

//     /**
//      * Saves a new product and its associated image file to GridFS.
//      */
//     public Product createWithImage(Product product, MultipartFile imageFile) throws IOException {
//         // 1. Store the file in GridFS
//         ObjectId fileId = gridFsTemplate.store(
//                 imageFile.getInputStream(), 
//                 imageFile.getOriginalFilename(), 
//                 imageFile.getContentType()
//         );

//         // 2. Set the GridFS ID on the Product model
//         product.setImageId(fileId.toString());

//         // 3. Save the Product model to the MongoDB 'products' collection
//         return productRepository.save(product);
//     }

//     /**
//      * Retrieves the image content from GridFS as a Spring Resource.
//      * This is used by the PageController to stream the image data.
//      */
//     public Resource getImageResource(String imageId) {
//         try {
//             // Find the file metadata in GridFS using the ObjectId derived from the imageId string
//             Query query = new Query(Criteria.where("_id").is(new ObjectId(imageId)));
//             GridFSFile gridFsFile = gridFsTemplate.findOne(query);

//             if (gridFsFile != null) {
//                 // Get the actual file content as a Resource
//                 return new GridFsResource(gridFsFile, gridFsTemplate.getResource(gridFsFile).getInputStream());
//             }
//         } catch (IllegalArgumentException | IOException e) {
//             // Handle cases where the ID is invalid or file reading fails
//             System.err.println("Failed to load image resource for ID: " + imageId + ". Error: " + e.getMessage());
//         }
//         return null; // Return null if file not found or error occurred
//     }

//     /**
//      * Retrieves the content type (MimeType) of the image stored in GridFS.
//      */
//     public String getImageContentType(String imageId) {
//         Query query = new Query(Criteria.where("_id").is(new ObjectId(imageId)));
//         GridFsFile gridFsFile = gridFsTemplate.findOne(query);

//         if (gridFsFile != null && gridFsFile.getMetadata() != null) {
//             // Use the contentType stored in the GridFS metadata
//             return Optional.ofNullable(gridFsFile.getMetadata().getString("_contentType"))
//                            .orElse("application/octet-stream");
//         }
//         return "application/octet-stream"; // Default fallback
//     }
// }
package in.aishaagarwal.mystiwax.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import in.aishaagarwal.mystiwax.model.Product;
import in.aishaagarwal.mystiwax.repository.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final GridFsTemplate gridFsTemplate;

    @Autowired
    public ProductService(ProductRepository productRepository, GridFsTemplate gridFsTemplate) {
        this.productRepository = productRepository;
        this.gridFsTemplate = gridFsTemplate;
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves all product documents from the MongoDB collection (used by getAll in REST and Page Controller).
     */
    public List<Product> getAll() {
        return productRepository.findAll();
    }
    
    /**
     * Retrieves a single product by its ID.
     */
    public Optional<Product> getById(String id) {
        return productRepository.findById(id);
    }

    /**
     * Saves a new product (without an image). Used by REST controller's POST /products.
     */
    public Product create(Product product) {
        // Ensure imageId is null or empty if no image is uploaded via this method
        if (product.getImageId() == null || product.getImageId().isEmpty()) {
            product.setImageId(null);
        }
        return productRepository.save(product);
    }


    // --- Image Handling and Persistence ---

    /**
     * Saves a new product AND its associated image file to GridFS. Used by the upload page.
     */
    public Product createWithImage(Product product, MultipartFile imageFile) throws IOException {
        // 1. Store the file in GridFS
        ObjectId fileId = gridFsTemplate.store(
                imageFile.getInputStream(), 
                imageFile.getOriginalFilename(), 
                imageFile.getContentType()
        );

        // 2. Set the GridFS ID on the Product model
        product.setImageId(fileId.toString());

        // 3. Save the Product model to the MongoDB 'products' collection
        return productRepository.save(product);
    }

    /**
     * Retrieves the image content from GridFS as a Spring Resource (used by PageController to stream images).
     */
    public Resource getImageResource(String imageId) {
        try {
            // Find the file metadata in GridFS using the ObjectId derived from the imageId string
            Query query = new Query(Criteria.where("_id").is(new ObjectId(imageId)));
            GridFSFile gridFsFile = gridFsTemplate.findOne(query);

            if (gridFsFile != null) {
                // Get the actual file content as a Resource
                return new GridFsResource(gridFsFile, gridFsTemplate.getResource(gridFsFile).getInputStream());
            }
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Failed to load image resource for ID: " + imageId + ". Error: " + e.getMessage());
        }
        return null; 
    }
    
    /**
     * Retrieves the image content from GridFS as a byte array (used by REST controller's getFileContent).
     */
    public byte[] getFileContent(String imageId) throws IOException {
        Resource resource = getImageResource(imageId);
        if (resource != null) {
            return FileCopyUtils.copyToByteArray(resource.getInputStream());
        }
        throw new IOException("Image file not found for ID: " + imageId);
    }

    /**
     * Retrieves the content type (MimeType) of the image stored in GridFS.
     */
    public String getImageContentType(String imageId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(imageId)));
        GridFSFile gridFsFile = gridFsTemplate.findOne(query);

        if (gridFsFile != null) {
            // Use the contentType stored in the GridFS metadata
            return Optional.ofNullable(gridFsFile.getMetadata())
                           .map(metadata -> metadata.getString("_contentType"))
                           .orElse("application/octet-stream");
        }
        return "application/octet-stream"; // Default fallback
    }
}

