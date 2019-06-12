package com.bharath.experiments.mirror.application;

import com.bharath.experiments.mirror.internal.CommandHandler;
import com.bharath.experiments.mirror.internal.SessionManager;
import com.bharath.experiments.mirror.io.ActionRequest;
import com.bharath.experiments.mirror.io.RegistrationRequest;
import com.bharath.experiments.mirror.io.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MirrorServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(MirrorServiceImpl.class);

    @Autowired
    private CommandHandler commandHandler;
    @Autowired
    private SessionManager sessionManager;

    public RegistrationResponse registerForSession(final RegistrationRequest request) {
        LOGGER.debug("Got the registration request ");
        final String sessionId = sessionManager.registerSession(request.getBrowserName(),
                request.getSeleniumServerUrl());
        return new RegistrationResponse(sessionId);
    }

    public void executeAction(final String sessionId, final ActionRequest request) {
        commandHandler.handleCommand(sessionId, request);
    }

    public void deleteSession(final String sessionId){
        LOGGER.debug("Deleting the session...");
        sessionManager.quitSession(sessionId);
    }
}
