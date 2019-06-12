package com.bharath.experiments.driver;

import com.bharath.experiments.locator.ElementLocator;
import com.bharath.experiments.locator.LocationMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class SeleniumBasedDriver implements BrowserDriver {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumBasedDriver.class);
    private static final String UNSUPPORTED_BROWSER_MESSAGE = "The browser %s is not supported!";
    private static final String INVALID_GRID_URL = "The grid URL %s is invalid !";
    private static final String INVALID_LOCATOR = "Uhh we screwed up the locator (By %s , locator %s) ";
    private static final int TIMEOUT_IN_SECONDS = 15;
    private static final int POLL_INTERVAL_IN_MILLIS = 250;
    private final WebDriver driver;
    private final FluentWait<WebDriver> wait;

    private WebDriver createWebDriver(final String browser, final String gridUrl) {
        LOGGER.debug("creating driver for {} using the gridURL {}", browser, gridUrl);
        try {
            final Browser selectedBrowser = Browser.valueOf(browser.trim().toUpperCase());
            DesiredCapabilities caps = null;
            switch (selectedBrowser) {
                case FIREFOX:
                    caps = new DesiredCapabilities(DesiredCapabilities.firefox());
                    break;
                case CHROME:
                    caps = new DesiredCapabilities(DesiredCapabilities.chrome());
                    break;
            }
            return new RemoteWebDriver(new URL(gridUrl), caps);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(String.format(INVALID_GRID_URL, gridUrl));
        } catch (IllegalArgumentException err) {
            throw new UnsupportedOperationException(String.format(UNSUPPORTED_BROWSER_MESSAGE, browser));
        }
    }

    private By getByFromLocator(final ElementLocator elementLocator) {
        final String locator = elementLocator.getLocator();
        final LocationMethod strategy = elementLocator.getStrategy();
        By by = null;
        switch (strategy) {
            case ID:
                by = By.id(locator);
                break;
            case NAME:
                by = By.name(locator);
                break;
            case CLASS_NAME:
                by = By.className(locator);
                break;
            case XPATH:
                by = By.xpath(locator);
                break;
            default:
                throw new IllegalArgumentException(String.format(INVALID_LOCATOR, strategy, locator));
        }
        return by;
    }

    public SeleniumBasedDriver(final String browser, final String gridUrl) {
        this.driver = createWebDriver(browser, gridUrl);
        this.wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(TIMEOUT_IN_SECONDS))
                .ignoring(RuntimeException.class)
                .pollingEvery(Duration.ofMillis(POLL_INTERVAL_IN_MILLIS));
    }

    public void click(final ElementLocator locator) {
        LOGGER.debug("Clicking on the element locator ", locator);
        wait.until(ExpectedConditions.visibilityOfElementLocated(getByFromLocator(locator)))
                .click();
    }

    public void type(final ElementLocator locator, final String text) {
        LOGGER.debug("Typing the text {} on the locator{}", locator);
        wait.until(ExpectedConditions.visibilityOfElementLocated(getByFromLocator(locator)))
                .sendKeys(Keys.chord(text));
    }

    public void hoverOver(final ElementLocator locator) {
        LOGGER.debug("Hovering over the element {}", locator);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(getByFromLocator(locator)));
        new Actions(driver).moveToElement(element)
                .build()
                .perform();
    }

    @Override
    public String getCurrentUrl() {
        final String url = driver.getCurrentUrl();
        LOGGER.debug("Getting the current URL {}", url);
        return url;
    }

    @Override
    public void goToUrl(String url) {
        LOGGER.debug("Going to tge URL {}", url);
        driver.get(url);
    }

    public void quit(){
        LOGGER.debug("Quitting the driver");
        driver.quit();
    }

    public static void main(String args[]) throws InterruptedException {
        BrowserDriver d = new SeleniumBasedDriver("chrome", "http://localhost:4444/wd/hub");
        d.goToUrl("https://www.google.com");
        Thread.sleep(5000);
        System.out.println(d.getCurrentUrl());
    }

}
