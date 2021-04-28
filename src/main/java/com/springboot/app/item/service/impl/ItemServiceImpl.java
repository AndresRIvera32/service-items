package com.springboot.app.item.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springboot.app.item.models.Item;
import com.springboot.app.commons.models.entity.Product;
import com.springboot.app.item.service.ItemService;

@Service("restTemplateService")
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private RestTemplate restClient;

	@Override
	public List<Item> findAll() {                           //"http://products-service/api/products/getAll"
		List<Product> products = Arrays.asList(restClient.getForObject("http://products-service/getAll", Product[].class));
		return products.stream().map(product -> new Item(product, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer amount) {
		Map<String, String> pathVariable = new HashMap<String, String>();
		pathVariable.put("id", id.toString());//http://products-service/api/products/get/{id}
		Product product = restClient.getForObject("http://products-service/get/{id}", Product.class, pathVariable);
		return new Item(product, amount);
	}

	@Override
	public Product save(Product product) {
		HttpEntity<Product> body = new HttpEntity<Product>(product);
		HttpEntity<Product> productResponse = restClient.exchange("http://products-service/create", HttpMethod.POST, body, Product.class );
		return productResponse.getBody();
	}

	@Override
	public Product update(Product product, Long id) {
		HttpEntity<Product> body = new HttpEntity<Product>(product);
		Map<String, String> pathVariable = new HashMap<String, String>();
		pathVariable.put("id", id.toString());
		HttpEntity<Product> productResponse = restClient.exchange("http://products-service/modify/{id}", HttpMethod.PUT, body, Product.class, pathVariable);
		return productResponse.getBody();
	}

	@Override
	public void delete(Long id) {
		Map<String, String> pathVariable = new HashMap<String, String>();
		pathVariable.put("id", id.toString());
		restClient.delete("http://products-service/delete/{id}", pathVariable);
	}

}
