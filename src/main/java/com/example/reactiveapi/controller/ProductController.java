package com.example.reactiveapi.controller;

import com.example.reactiveapi.domain.Product;
import com.example.reactiveapi.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping
    public Flux<Product> findAll(){
        return productService.findAll();
    }

    @PostMapping
    public Mono<Product> save(@RequestBody Product product){
        return productService.save(product);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> findById(@PathVariable(value = "id") String productId){
        return productService.findById(productId)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> update
            (@PathVariable(value = "id") String productId,
             @RequestBody Product product){
        return productService.findById(productId)
                .flatMap(savedProduct -> {
                  savedProduct.setName(product.getName());
                  savedProduct.setPrice(product.getPrice());
                  return  productService.save(savedProduct);
                })
                .map(updatedProduct -> new ResponseEntity<>(updatedProduct, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Product>> deleteById(@PathVariable(value = "id") String productId){
        return productService.delete(productId)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
