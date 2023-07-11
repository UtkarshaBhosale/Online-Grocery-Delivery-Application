package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Product;

public interface ProductService {
	
	void addProduct(Product product, MultipartFile productImmage);

}
