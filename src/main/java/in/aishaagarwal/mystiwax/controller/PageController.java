package in.aishaagarwal.mystiwax.controller;

import in.aishaagarwal.mystiwax.model.Product;
import in.aishaagarwal.mystiwax.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;

@Controller
public class PageController {
    
    // Inject the ProductService
    private final ProductService productService;
    
    @Autowired
    public PageController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Handles the root path (homepage) and fetches all products to display.
     * The model attribute "products" will be used by index.html.
     */
    @GetMapping({"/", "/index", "/products"})
    public String showProductList(Model model) {
        try {
            // NOTE: Assuming productService.findAllProducts() is now implemented 
            // to return List<Product>.
            List<Product> allProducts = productService.findAllProducts();
            // Attach the list to the model for Thymeleaf to access
            model.addAttribute("products", allProducts);
            
            // For the product tabs (New, Best Sellers), we can fetch a featured subset
            // For now, let's just use the first 3 products as "featured"
            int limit = Math.min(3, allProducts.size());
            // Ensure subList is safe even if allProducts is empty
            List<Product> featuredProducts = allProducts.subList(0, limit);
            model.addAttribute("featuredProducts", featuredProducts);

        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            // Add an empty list or an error flag to the model if fetching fails
            model.addAttribute("products", List.of()); 
            model.addAttribute("featuredProducts", List.of());
        }
        
        return "index"; // points to index.html
    }
    
    /**
     * REST endpoint to serve image files stored in GridFS.
     * The image is streamed directly to the browser based on the imageId.
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> serveFile(@PathVariable("id") String imageId) {
        try {
            // NOTE: Assuming productService.getImageResource(imageId) is now implemented
            // to fetch the file content as a Resource (e.g., GridFsResource).
            Resource file = productService.getImageResource(imageId);

            // Fetch the content type (MimeType) if possible, defaulting to octet-stream
            String mimeType = productService.getImageContentType(imageId);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(file);
        } catch (Exception e) {
            System.err.println("Error serving image: " + imageId + ". Error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    // NOTE: Keeping the upload methods for data entry purposes.
    
    @GetMapping("/upload") 
    public String showUploadForm(Model model) {
        model.addAttribute("product", new Product());
        return "upload"; // requires upload.html 
    }

    @PostMapping("/upload")
    public String handleUpload(
            @ModelAttribute("product") Product product, 
            @RequestParam("imageFile") MultipartFile imageFile) { 
        try {
            productService.createWithImage(product, imageFile); 
            return "redirect:/products"; 
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/upload?error=upload_failed";
        }
    }
}
