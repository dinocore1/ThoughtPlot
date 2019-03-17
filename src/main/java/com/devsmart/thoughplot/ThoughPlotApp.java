package com.devsmart.thoughplot;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;


@SpringBootApplication
@EnableConfigurationProperties({
        Config.class
})
public class ThoughPlotApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThoughPlotApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ThoughPlotApp.class, args);
    }

    @Bean
    public File getRootDir(Config config) {
        String rootDir = config.getRootDir();
        if(StringUtils.isEmptyOrNull(rootDir)){
            LOGGER.warn("no configuration for `rootDir` using default 'thoughts'");
            rootDir = "thoughts";
        }

        File dir = new File(rootDir);
        return dir;
    }

    @Bean
    public Git getGit(File rootDir, Config config) {
        String gitUrl = config.getGiturl();
        Git git = null;

        try {
        if(!StringUtils.isEmptyOrNull(gitUrl)) {
            try {
                git = Git.open(rootDir);
                git.pull().call();
            } catch (RepositoryNotFoundException e) {
                LOGGER.error("no repo found in: {}", rootDir.getAbsolutePath());
                git = Git.cloneRepository()
                        .setURI(gitUrl)
                        .setDirectory(rootDir)
                        .call();
            }
        } else {
            git = Git.init()
                    .setDirectory(rootDir)
                    .call();
        }

        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return git;
    }

    @Bean
    public NoteDB getNoteDB(File rootDir, Git git) {
        return new FileSystemNoteDB(rootDir);
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ViewEngine getViewEngine() {
        return new ViewEngine();
    }
}
