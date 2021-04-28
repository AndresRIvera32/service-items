package com.springboot.app.item.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.springboot.app.commons.models.entity.Product;

@FeignClient(name = "products-service"/*, url ="localhost:8001/api/products"*/)
public interface RestProductClient {
	
	///api/products
	@GetMapping("/getAll")
	public List<Product> getAllProducts();
	
	///api/products
	@GetMapping("/get/{id}")
	public Product productDetails(@PathVariable Long id);
	
	@PostMapping("/create")
	public Product create(@RequestBody Product product);
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable Long id);
	
	@PutMapping("/modify/{id}")
	public Product modify(@RequestBody Product product, @PathVariable Long id);
	
	

}
