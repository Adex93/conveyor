package com.example.conveyor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ConveyorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConveyorApplication.class, args);
        log.info("Микросервис Conveyor успешно запущен");
    }

}
