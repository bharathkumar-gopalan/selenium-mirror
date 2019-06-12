package com.bharath.experiments.mirror.application;

import com.bharath.experiments.mirror.io.ActionRequest;
import com.bharath.experiments.mirror.io.RegistrationRequest;
import com.bharath.experiments.mirror.io.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mirror")
public class MirrorServiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MirrorServiceController.class);

    @Autowired
    private MirrorServiceImpl mirrorService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid final RegistrationRequest request) {
        LOGGER.debug("Got a registration request...");
        return new ResponseEntity<>(mirrorService.registerForSession(request), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.POST)
    public ResponseEntity drive(@PathVariable("sessionId") final String sessionId,
                                @RequestBody @Valid final ActionRequest request){
        mirrorService.executeAction(sessionId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("sessionId") final String sessionId){
        mirrorService.deleteSession(sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
