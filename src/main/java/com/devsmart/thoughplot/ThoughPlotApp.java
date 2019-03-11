package com.devsmart.thoughplot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

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

    @Bean
    public String getDefaultNote() {
        return "index";
    }

    @Bean
    public NoteDB getNoteDB() {
        return new FileSystemNoteDB(getRootDir());
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ViewEngine getViewEngine() {
        return new ViewEngine();
    }
}
