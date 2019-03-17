package com.devsmart.thoughplot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class Config {

    private String giturl;
    private String rootDir;

    public String getGiturl() {
        return giturl;
    }

    public void setGiturl(String giturl) {
        this.giturl = giturl;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }
}
