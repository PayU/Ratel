package com.payu.hackathon.front;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gateway")
public class GatewayController {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Status> open() {

        final Status status = new Status("working");
        status.add(linkTo(methodOn(GatewayController.class).open()).withSelfRel());

        return new ResponseEntity<>(status, HttpStatus.OK);

    }


}
