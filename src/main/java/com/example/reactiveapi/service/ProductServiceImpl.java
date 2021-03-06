package com.example.reactiveapi.service;

import com.example.reactiveapi.domain.Product;
import com.example.reactiveapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductServiceImpl  implements ProductService{
    private ProductRepository productRepository;

    @Override
    public Mono<Product> findById(String productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> delete(String productId) {
        return productRepository.findById(productId)
                .flatMap(product -> productRepository.deleteById(productId).thenReturn(product));

    }
}
