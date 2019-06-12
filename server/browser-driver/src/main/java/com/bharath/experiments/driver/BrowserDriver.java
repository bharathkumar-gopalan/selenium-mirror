package com.bharath.experiments.driver;

import com.bharath.experiments.locator.ElementLocator;

public interface BrowserDriver {

    void click(final ElementLocator locator);

    void type(final ElementLocator locator, final String text);

    void hoverOver(final ElementLocator locator);

    String getCurrentUrl();

    void goToUrl(final String url);

    void quit();


}
