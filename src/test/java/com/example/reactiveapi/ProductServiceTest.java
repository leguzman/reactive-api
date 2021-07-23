package com.example.reactiveapi;

import com.example.reactiveapi.domain.Product;
import com.example.reactiveapi.service.ProductService;
import com.example.reactiveapi.service.ProductServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
@Log4j2
@DataMongoTest
@Import(ProductServiceImpl.class)
public class ProductServiceTest {

    private ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService){
        this.productService = productService;
    }

    @Test
    public void saveTest(){
        Product product = getProduct();

        Mono<Product> productMono = productService.save(product);
        StepVerifier.create(productMono)
                .expectNextMatches(p -> StringUtils.hasText(p.getName()))
                .verifyComplete();
        log.info("Save Test");
    }
    @Test
    public  void updateTest() {
    Product product= getProduct();
    Mono<Product> productMono = productService.save(product);
    Product savedProduct = productMono.block();
    savedProduct.setName("iPad Pro");
    Mono<Product> updatedProduct = productService.save(product);

    StepVerifier.create(updatedProduct)
            .expectNextMatches(p -> p.getName() .equals("iPad Pro"))
            .verifyComplete();
        log.info("Update Test");

    }
    @Test
    public  void deleteTest() {
        Mono<Product> productMono = productService.save(getProduct())
                .flatMap(product -> productService.delete(product.getId()));

        StepVerifier.create(productMono)
                .expectNextMatches(p -> p.getName() .equals("Mac Book Pro"))
                .verifyComplete();
        log.info("Delete Test");
    }
    @Test
    public  void findByIdTest() {
        Mono<Product> productMono = productService.save(getProduct())
                .flatMap(product -> productService.findById(product.getId()));

        StepVerifier.create(productMono)
                .expectNextMatches(p -> p.getName() .equals("Mac Book Pro"))
                .verifyComplete();
        log.info("FindById Test");
    }


    private Product getProduct() {
        Product product = new Product(
                UUID.randomUUID().toString(),
                "Mac Book Pro",
                ThreadLocalRandom.current().nextDouble(1000,5000));
        return product;
    }



}
