package com.payu.hackathon.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/gateway")
public class Gateway {

    private static final Logger LOG = LoggerFactory.getLogger(Gateway.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Status> open(UriComponentsBuilder builder) {

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                builder.path("/more")
                        .build()
                        .toUri());

        return new ResponseEntity<>(Status.OK, headers, HttpStatus.OK);

    }


}
