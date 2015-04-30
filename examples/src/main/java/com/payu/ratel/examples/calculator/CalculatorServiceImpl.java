package com.payu.ratel.examples.calculator;

import org.springframework.stereotype.Service;

import com.payu.ratel.Publish;


@Publish
@Service
public class CalculatorServiceImpl implements CalculatorService {

  public int add(int a, int b) {
    return a + b;
  }

  public int divide(int a, int b) {
    return a / b;
  }

}
