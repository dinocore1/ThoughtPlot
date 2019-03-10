package com.devsmart.thoughplot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;


@SpringBootApplication
public class ThoughPlotApp {

    public static void main(String[] args) {
        SpringApplication.run(ThoughPlotApp.class, args);
    }

    @Bean
    public File getRootDir() {
        return new File("thoughts");
    }
}
