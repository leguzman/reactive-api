package com.example.reactiveapi.repository;

import com.example.reactiveapi.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ProductRepository extends ReactiveMongoRepository <Product,String>{

}
