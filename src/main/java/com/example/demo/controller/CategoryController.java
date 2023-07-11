package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

@RestController
@RequestMapping("api/category")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("all")
	public ResponseEntity<List<Category>> getAllCategories() {
		
		List<Category> categories = this.categoryRepository.findAll();
		
		ResponseEntity<List<Category>> response = new ResponseEntity<>(categories, HttpStatus.OK);
		
		
		return response;
		
	}
	
	@PostMapping("add")
	public ResponseEntity add(@RequestBody Category category) {
		
		
		Category c = categoryRepository.save(category);
		
		if(c != null) {
			System.out.println("response sent");
			return new ResponseEntity( c ,HttpStatus.OK);
		}
		
		else {
			System.out.println("response sent");
			return new ResponseEntity("Failed to add category!",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	 @PutMapping("update")
	    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
	        if (categoryRepository.existsById(category.getId())) {
	            Category updatedCategory = categoryRepository.save(category);
	            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }

	    @DeleteMapping("delete/{id}")
	    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
	        if (categoryRepository.existsById(id)) {
	            categoryRepository.deleteById(id);
	            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
	        }
	    }
	
	
	
}

