package com.bharath.experiments.mirror.internal;

import com.bharath.experiments.driver.BrowserDriver;
import com.bharath.experiments.driver.SeleniumBasedDriver;

public class SessionSlot {

    private final BrowserDriver driver;
    private final long sessionCreatedAt;
    private final String seleniumServerUrl;

    public SessionSlot(final String browser, final String seleniumServerUrl) {
        this.sessionCreatedAt = System.currentTimeMillis();
        this.driver = new SeleniumBasedDriver(browser, seleniumServerUrl);
        this.seleniumServerUrl = seleniumServerUrl;
    }

    public BrowserDriver getDriver() {
        return driver;
    }

    public long getSessionCreatedAt() {
        return sessionCreatedAt;
    }

    public String getSeleniumServerUrl() {
        return seleniumServerUrl;
    }
}
