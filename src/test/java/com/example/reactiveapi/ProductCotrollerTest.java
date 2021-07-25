package com.example.reactiveapi;

import java.util.UUID;

import com.example.reactiveapi.domain.Product;
import com.example.reactiveapi.service.ProductService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@WebFluxTest

public class ProductCotrollerTest {

        @MockBean private ProductService productService;

        private WebTestClient webTestClient;

        @Autowired
        public ProductCotrollerTest(WebTestClient webTestClient) {
            this.webTestClient = webTestClient;
            
        }

        @Test
        public void findAllProduct() {
            Mockito.when(productService.findAll())
                .thenReturn(
                    Flux.just(
                        new Product(UUID.randomUUID().toString(), "Mac Book Pro",2999.99),
                        new Product(UUID.randomUUID().toString(), "Mac Book Air",213.99),
                        new Product(UUID.randomUUID().toString(), "iPad Pro",3223.99)   
                        )
                );
            webTestClient.get()
            .uri("/product")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.[0].name").isEqualTo("Mac Book Pro");

            log.info("Test Find all product");
        }

        @Test
        public void findProductByIdTest() {
            Product product = new Product(UUID.randomUUID().toString(), "Mac Book Pro",2999.99);
            Mockito.when(productService.findById(product.getId()))
            .thenReturn(Mono.just(product));

            webTestClient.get()
            .uri("/product/"+product.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isEqualTo("Mac Book Pro")
            .jsonPath("$.price").isEqualTo(2999.99);

            log.info("Test find Product By Id");
        }

        @Test
        public void saveProductTest() {
            Product product = new Product(UUID.randomUUID().toString(), "Mac Book Pro",2999.99);
            Mockito.when(productService.save(Mockito.any(Product.class)))
            .thenReturn(Mono.just(product));
            
            webTestClient.post()
            .uri("/product")
            .body(Mono.just(product),Product.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.name").isEqualTo(product.getName())
            .jsonPath("$.price").isEqualTo(2999.99);

            log.info("Test save Product");
        }

        @Test
        public void updateProductTest() {
            Product product = new Product(UUID.randomUUID().toString(), "Mac Book Pro",2999.99);
            Mockito.when(productService.findById(product.getId()))
            .thenReturn(Mono.just(product));
            Mockito.when(productService.save(product))
            .thenReturn(Mono.just(product));

            webTestClient.put()
            .uri("/product/"+product.getId())
            .body(Mono.just(product),Product.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isEqualTo(product.getName())
            .jsonPath("$.price").isEqualTo(2999.99);

            log.info("Test update Product");


        }

        @Test
        public void deleteProductTest() {
            Product product = new Product(UUID.randomUUID().toString(), "Mac Book Pro",2999.99);

            Mockito.when(productService.delete(product.getId()))
            .thenReturn(Mono.just(product));

            webTestClient.delete()
            .uri("/product/"+product.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isEqualTo(product.getName())
            .jsonPath("$.price").isEqualTo(2999.99);

            log.info("Test delete Product by id");


            
        }
}
