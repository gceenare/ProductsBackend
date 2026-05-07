package com.solutions.mbangi.products.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        if ("admin@mbangi.co.za".equals(loginRequest.getEmail()) && "admin123".equals(loginRequest.getPassword())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).build();
    }
}
