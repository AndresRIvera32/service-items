package com.springboot.app.item.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.springboot.app.item.clients.RestProductClient;
import com.springboot.app.item.models.Item;
import com.springboot.app.commons.models.entity.Product;
import com.springboot.app.item.service.ItemService;

@Service("feignService")
//@Primary
public class ItemFeignService implements ItemService {

	@Autowired
	private RestProductClient feignClient;
	
	@Override
	public List<Item> findAll() {
		return feignClient.getAllProducts().stream().map(product -> new Item(product, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer amount) {
		return new Item(feignClient.productDetails(id), amount);
	}

	@Override
	public Product save(Product product) {
		return feignClient.create(product);
	}

	@Override
	public Product update(Product product, Long id) {
		return feignClient.modify(product, id);
	}

	@Override
	public void delete(Long id) {
		feignClient.delete(id);
	}

}
