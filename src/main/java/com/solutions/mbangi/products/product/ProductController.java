package com.solutions.mbangi.products.product;

import com.solutions.mbangi.products.category.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductsByCategory(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> {
                    MediaType contentType = product.getImageType() != null ? 
                            MediaType.parseMediaType(product.getImageType()) : MediaType.IMAGE_JPEG;
                    return ResponseEntity.ok()
                            .contentType(contentType)
                            .body(product.getImageData());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Product> createProduct(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "price") String price,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "categoryId") Long categoryId,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Product product = buildProduct(name, price, description, categoryId, file);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing product image", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create product: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Product product = buildProduct(name, price, description, categoryId, file);
            
            return productService.updateProduct(id, product)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating product image", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update product: " + e.getMessage());
        }
    }

    private Product buildProduct(String name, String price, String description, Long categoryId, MultipartFile file) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);

        if (file != null && !file.isEmpty()) {
            product.setImageData(file.getBytes());
            product.setImageType(file.getContentType());
        }

        Category category = new Category();
        category.setId(categoryId);
        product.setCategory(category);
        return product;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
