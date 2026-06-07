package com.solutions.mbangi.products.product;

import com.solutions.mbangi.products.category.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productDetails.getName());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setDescription(productDetails.getDescription());
            if (productDetails.getImageData() != null) {
                existingProduct.setImageData(productDetails.getImageData());
            }
            if (productDetails.getImageType() != null) {
                existingProduct.setImageType(productDetails.getImageType());
            }
            if (productDetails.getImageUrl() != null) {
                existingProduct.setImageUrl(productDetails.getImageUrl());
            }
            if (productDetails.getCategory() != null) {
                existingProduct.setCategory(productDetails.getCategory());
            }
            return productRepository.save(existingProduct);
        });
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
