package com.example.demo.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.dto.CartDataResponse;
import com.example.demo.dto.CartResponse;
import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/user/")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@PostMapping("cart/add")
	public ResponseEntity add(@RequestBody AddToCartRequest addToCartRequest) {
		
		Optional<User> optionalUser = userRepository.findById(addToCartRequest.getUserId());
		User user = null;
		if(optionalUser.isPresent()) {
			user = optionalUser.get();
		}
		
		Optional<Product> optionalProduct = productRepository.findById(addToCartRequest.getProductId());
		Product product = null;
		if(optionalProduct.isPresent()) {
			product = optionalProduct.get();
		}
		
		Cart cart = new Cart();
		cart.setProduct(product);
		cart.setQuantity(addToCartRequest.getQuantity());
		cart.setUser(user);
		
		cartRepository.save(cart);
		
		return new ResponseEntity(HttpStatus.OK);
		
	}
	
	@GetMapping("mycart")
	public ResponseEntity getMyCart(@RequestParam("userId") int userId) throws JsonProcessingException {
		
		
		List<CartDataResponse> cartDatas = new ArrayList<>();
		
		List<Cart> userCarts = cartRepository.findByUser_id(userId);
		
		double totalCartPrice = 0;
		
		for (Cart cart : userCarts) {
			CartDataResponse cartData = new CartDataResponse();
			cartData.setCartId(cart.getId());
			
			cartData.setProductName(cart.getProduct().getTitle());
			cartData.setProductImage(cart.getProduct().getImageName());
			cartData.setQuantity(cart.getQuantity());
			cartData.setProductId(cart.getProduct().getId());
			
			cartDatas.add(cartData);
			
			double productPrice = Double.parseDouble(cart.getProduct().getPrice().toString());
			
			totalCartPrice =  totalCartPrice + (cart.getQuantity() * productPrice);
			
		}
		
		CartResponse cartResponse = new CartResponse();
		cartResponse.setTotalCartPrice(String.valueOf(totalCartPrice));
		cartResponse.setCartData(cartDatas);
		
		String json = objectMapper.writeValueAsString(cartResponse);
				
		return new ResponseEntity(cartResponse, HttpStatus.OK);
		
	}
	
	@GetMapping("mycart/remove")
	public ResponseEntity removeCartItem(@RequestParam("cartId") int cartId) throws JsonProcessingException {
		
		
		
		Optional<Cart> optionalCart = this.cartRepository.findById(cartId);
		Cart cart = new Cart();
		
		if(optionalCart.isPresent()) {
			cart = optionalCart.get();
		}
		
		this.cartRepository.delete(cart);
		
		return new ResponseEntity("SUCCESS", HttpStatus.OK);
		
	}

}
