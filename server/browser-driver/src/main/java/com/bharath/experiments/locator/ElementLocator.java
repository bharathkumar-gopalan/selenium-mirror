package com.bharath.experiments.locator;

public class ElementLocator {
    private static final String TO_STRING_FORMAT = "[%s-%s]";
    private final String locator;
    private final LocationMethod strategy;

    public ElementLocator(final LocationMethod strategy, final String locator) {
        this.strategy = strategy;
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public LocationMethod getStrategy() {
        return strategy;
    }

    public String toString(){
        return String.format(TO_STRING_FORMAT, locator, strategy);
    }
}
