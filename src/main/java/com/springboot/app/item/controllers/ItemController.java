package com.springboot.app.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springboot.app.item.models.Item;
import com.springboot.app.commons.models.entity.Product;
import com.springboot.app.item.service.ItemService;

@RestController
//@RequestMapping("/api/items")
public class ItemController {
	
	private static Logger log = LoggerFactory.getLogger(ItemController.class);
	
	//this is used to read the environment properties that is set up in the properties
	@Autowired
	private Environment env;

	@Autowired
	@Qualifier("restTemplateService")
	private ItemService itemService;
	
	@Value("${configuration.text}")
	private String text;
	
	@GetMapping("/getAll")
	public List<Item> getAll(){
		return itemService.findAll();
	}
	
    //@HystrixCommand(fallbackMethod = "defaultItemMethod")
	@GetMapping("/get/{id}/amount/{amount}")
	public Item itemDetail(@PathVariable Long id, @PathVariable Integer amount) {
		return itemService.findById(id, amount);
	}
	
	public Item defaultItemMethod(Long id, Integer amount) {
		Item item = new Item();
		item.setAmount(amount);
		Product product = new Product();
		product.setName("defaultProduct");
		product.setId(id);
		product.setPrice(0.0);
		item.setProduct(product);
		return item;
	}
	
	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public Product create(@RequestBody Product product) {
		return itemService.save(product);
	}
	
	@PostMapping("/update/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Product update(@RequestBody Product product, @PathVariable Long id) {
		return itemService.update(product, id);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		itemService.delete(id);
	}
	
	@GetMapping("/get-config")
	public ResponseEntity<?> getConfiguration(@Value("${server.port}") String port){
		
		log.info(text);
		Map<String,String> json = new HashMap<>(); 
		json.put("text", text);
		json.put("port", port);
		if(env.getActiveProfiles().length >0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("author.name", env.getProperty("configuration.author.name"));
			json.put("author.email", env.getProperty("configuration.author.email"));
		}
		return new ResponseEntity<Map<String,String>>(json, HttpStatus.OK);
	}
}
