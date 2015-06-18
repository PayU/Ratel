package com.payu.ratel.tests.service.misconfig;

import org.springframework.stereotype.Service;

import com.payu.ratel.Publish;
import com.payu.ratel.tests.service.Test2Service;
import com.payu.ratel.tests.service.TestService;

@Publish(TestService.class)
@Service
public class WrongServiceImplementation implements Test2Service {

    @Override
    public String helloWorld() {
        return null;
    }

    @Override
    public int power(Integer arg) {
        return 0;
    }

}
