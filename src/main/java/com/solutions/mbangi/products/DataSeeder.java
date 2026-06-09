package com.solutions.mbangi.products;

import com.solutions.mbangi.products.category.Category;
import com.solutions.mbangi.products.category.CategoryRepository;
import com.solutions.mbangi.products.product.Product;
import com.solutions.mbangi.products.product.ProductRepository;
import com.solutions.mbangi.products.user.User;
import com.solutions.mbangi.products.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public DataSeeder(CategoryRepository categoryRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Category businessStationery = categoryRepository.findById(1L).orElseGet(() -> categoryRepository.save(new Category(null, "Business Stationery")));
        Category promotionalGear = categoryRepository.findById(2L).orElseGet(() -> categoryRepository.save(new Category(null, "Promotional Gear")));
        Category apparelTextiles = categoryRepository.findById(3L).orElseGet(() -> categoryRepository.save(new Category(null, "Apparel & Textiles")));

        if (productRepository.count() == 0) {
            Product p1 = new Product();
            p1.setName("Business Card");
            p1.setPrice("5.00");
            p1.setImageUrl("https://example.com/business-card.jpg");
            p1.setDescription("High-quality business cards");
            p1.setCategory(businessStationery);
            productRepository.save(p1);

            Product p2 = new Product();
            p2.setName("Letterhead");
            p2.setPrice("10.00");
            p2.setImageUrl("https://example.com/letterhead.jpg");

            p2.setDescription("Professional letterhead paper");
            p2.setCategory(businessStationery);
            productRepository.save(p2);

            Product p3 = new Product();
            p3.setName("T-Shirt");
            p3.setPrice("15.00");
            p3.setImageUrl("https://example.com/tshirt.jpg");
            p3.setDescription("Custom printed t-shirts");
            p3.setCategory(apparelTextiles);
            productRepository.save(p3);

            Product p4 = new Product();
            p4.setName("Mug");
            p4.setPrice("8.00");
            p4.setImageUrl("https://example.com/mug.jpg");
            p4.setDescription("Promotional coffee mugs");
            p4.setCategory(promotionalGear);
            productRepository.save(p4);
        }

        // Create an admin user for admin login in an idempotent way
        try {
            String adminUsername = "admin";
            String adminPasswordPlain = System.getenv("ADMIN_PASSWORD") != null ? System.getenv("ADMIN_PASSWORD") : "admin123";

            if (!userRepository.existsByUsername(adminUsername)) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashed = encoder.encode(adminPasswordPlain);
                User admin = new User(null, adminUsername, hashed, "ROLE_ADMIN");
                userRepository.save(admin);
            }
        } catch (Exception ex) {
            // fail safe - don't stop application startup when seeding user
            System.err.println("Warning: could not create admin user: " + ex.getMessage());
        }
    }
}
