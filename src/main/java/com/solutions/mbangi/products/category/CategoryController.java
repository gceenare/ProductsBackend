package com.solutions.mbangi.products.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all categories
     * GET /api/categories
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    /**
     * Get category by ID
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create new category
     * POST /api/categories
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category saved = categoryRepository.save(category);
        return ResponseEntity.status(201).body(saved);
    }

    /**
     * Update category
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return categoryRepository.findById(id)
                .map(existing -> {
                    existing.setName(categoryDetails.getName());
                    return ResponseEntity.ok(categoryRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete category
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
