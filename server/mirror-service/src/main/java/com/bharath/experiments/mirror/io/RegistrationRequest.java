package com.bharath.experiments.mirror.io;

public class RegistrationRequest {
    private String browserName;
    private String seleniumServerUrl;

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getSeleniumServerUrl() {
        return seleniumServerUrl;
    }

    public void setSeleniumServerUrl(String seleniumServerUrl) {
        this.seleniumServerUrl = seleniumServerUrl;
    }
}
