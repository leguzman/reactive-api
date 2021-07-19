package com.example.reactiveapi.config;

import com.example.reactiveapi.domain.Product;
import com.example.reactiveapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@AllArgsConstructor
@Log4j2
public class DatabaseInit implements ApplicationListener<ApplicationReadyEvent> {
    private ProductRepository productRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationEvent){
        productRepository.deleteAll()
                .thenMany(
                        Flux.just("Mac Book Pro","Mac Book Air","iPad pro","iPad Air", "iPhone")
                        .map( name -> new Product(
                                UUID.randomUUID().toString(),name, ThreadLocalRandom.current().nextDouble(1000,5000)

                        ))
                        .flatMap(productRepository::save)
                )
                .thenMany(productRepository.findAll())
                .subscribe( product -> log.info("Saved Product {}",product));
    }
}
