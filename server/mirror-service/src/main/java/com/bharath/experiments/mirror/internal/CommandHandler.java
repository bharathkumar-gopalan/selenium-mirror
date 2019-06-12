package com.bharath.experiments.mirror.internal;

import com.bharath.experiments.driver.BrowserDriver;
import com.bharath.experiments.locator.ElementLocator;
import com.bharath.experiments.locator.LocationMethod;
import com.bharath.experiments.mirror.io.ActionContext;
import com.bharath.experiments.mirror.io.ActionRequest;
import com.bharath.experiments.mirror.io.LocationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);
    private static final String INVALID_LOCATION_METHOD = "Invalid location method %s";

    @Autowired
    private SessionManager sessionManager;


    private ElementLocator buildLocator(final LocationStrategy locationStrategy) {
        LocationMethod method = null;
        switch (locationStrategy.getMethod()) {
            case "id":
                method = LocationMethod.ID;
                break;
            case "class":
                method = LocationMethod.CLASS_NAME;
                break;
            case "xpath":
                method = LocationMethod.XPATH;
                break;
            case "name":
                method = LocationMethod.NAME;
                break;
            default:
                throw new IllegalArgumentException((String.format(INVALID_LOCATION_METHOD, locationStrategy.getMethod())));
        }
        return new ElementLocator(method, locationStrategy.getLocator());
    }

    private List<ElementLocator> buildLocators(final ActionRequest actionRequest) {
        return actionRequest.getLocationStrategyList()
                .stream()
                .map(locationStrategy -> buildLocator(locationStrategy))
                .collect(Collectors.toList());
    }

    public void handleCommand(final String sessionId, final ActionRequest actionRequest) {
        LOGGER.debug("Handling the command {} for the session {}", actionRequest, sessionId);
        final List<ElementLocator> elementLocatorList = buildLocators(actionRequest);
        final BrowserDriver driver = sessionManager.getDriverForSession(sessionId);
        // for now we don't care about anything else, Simply use xpath
        final ElementLocator matchingLocator = elementLocatorList.stream()
                .filter(elementLocator -> elementLocator.getStrategy() == LocationMethod.XPATH)
                .findFirst()
                .get();

        // Check if the URL matches with the request , else load the URL and proceed
        final String browserUrl = driver.getCurrentUrl().trim();
        final String actionUrl = actionRequest.getPageUrl().trim();

        LOGGER.debug("The browser URL is {} and the action URL is {}", browserUrl, actionUrl);

        if(browserUrl == null || !browserUrl.contains(actionUrl)){
            LOGGER.debug("URL mismatch , loading the current URL .....");
            driver.goToUrl(actionUrl);
        }

        final ActionContext context= actionRequest.getActionContext();


        switch(context.getAction()){
            case CLICK:
                driver.click(matchingLocator);
                break;
            case TYPE:
                driver.type(matchingLocator, context.getValue());
                break;
            case HOVER:
                driver.hoverOver(matchingLocator);
                break;
        }
    }


}
