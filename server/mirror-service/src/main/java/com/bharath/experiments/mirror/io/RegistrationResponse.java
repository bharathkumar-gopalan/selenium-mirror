package com.bharath.experiments.mirror.io;

public class RegistrationResponse {
    private final String sessionId;

    public RegistrationResponse(final String sessionId){
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
