package com.payu.hackathon.front;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payu.hackathon.front.resources.Index;

@RestController
public class IndexController {

    @RequestMapping("/")
    public HttpEntity<Index> index() {

        final Index index = new Index();
        index.add(linkTo(methodOn(GatewayController.class).open()).withRel("open"));

        return new ResponseEntity<>(index, HttpStatus.OK);
    }
}
