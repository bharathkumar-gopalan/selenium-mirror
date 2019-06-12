package com.bharath.experiments.mirror.internal;

import com.bharath.experiments.driver.BrowserDriver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SessionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionManager.class);
    private static final int MAX_BROWSERS_PER_HOST = 2;
    private static final String STATUS_URL = "/status";
    private static final String SESSION_ID_NOT_FOUND_MESSAGE = "Unable to locate the sessionId %s";
    private static final String UNABLE_TO_REGISTER_MESSAGE = "Unable to create a new session for selenium host %s";


    private final Map<String, SessionSlot> sessionSlotMap;
    private final OkHttpClient client;

    public SessionManager() {
        LOGGER.debug("Initialising the registration handler");
        this.sessionSlotMap = new ConcurrentHashMap<>();
        this.client = new OkHttpClient();
    }

    private boolean isHostOk(final String hostIdentifier) {
        LOGGER.debug("checking if the host is ok ");
        boolean isHostOk = false;
        Response response = null;
        try {
            response = client.newCall(new Request.Builder()
                    .url(hostIdentifier + STATUS_URL)
                    .build()).execute();
            if (response.code() == 200) {
                isHostOk = true;
            }
        } catch (IOException e) {

        } finally {
            if (response != null) {
                LOGGER.debug("Closing the response");
                response.close();
            }
        }
        return isHostOk;
    }

    private SessionSlot getMatchingSlot(final String sessionId){
        SessionSlot matchingSlot = sessionSlotMap.get(sessionId);
        if(matchingSlot == null){
            throw new NoSuchElementException(String.format(SESSION_ID_NOT_FOUND_MESSAGE, sessionId));
        }
        return matchingSlot;
    }

    private boolean isQuotaAvailable(final String seleniumServerUrl) {
        return sessionSlotMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getSeleniumServerUrl().equals(seleniumServerUrl))
                .collect(Collectors.toList())
                .size() < MAX_BROWSERS_PER_HOST;
    }

    public String registerSession(final String browser, final String seleniumServerUrl) {
        LOGGER.debug("Registering the grid host {} to the browser {}", seleniumServerUrl, browser);
        if (isHostOk(seleniumServerUrl) && isQuotaAvailable(seleniumServerUrl)) {
            LOGGER.debug("Quota check ok and host is alive !, Creating a new session slot ");
            final String sessionId = UUID.randomUUID().toString();
            sessionSlotMap.put(sessionId, new SessionSlot(browser, seleniumServerUrl));
            LOGGER.debug("Registration is completed !");
            return sessionId;
        }
        throw new IllegalArgumentException(String.format(UNABLE_TO_REGISTER_MESSAGE, seleniumServerUrl));
    }

    public void quitSession(final String sessionId) {
        LOGGER.debug("Attempting to deregister session {}", sessionId);
        getMatchingSlot(sessionId).getDriver()
                .quit();
        //Remove the slot
        sessionSlotMap.remove(sessionId);
    }

    public BrowserDriver getDriverForSession(final String sessionId){
        return getMatchingSlot(sessionId).getDriver();
    }

    public Map<String, SessionSlot> getSessionSlotMap(){
        return sessionSlotMap;
    }
}
