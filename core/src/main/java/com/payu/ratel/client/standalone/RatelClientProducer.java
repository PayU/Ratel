package com.payu.ratel.client.standalone;

import com.payu.ratel.client.zookeeper.ZookeeperFetcher;

public class RatelClientProducer {
  
  private static final RatelClientProducer INSTANCE = null;

  private static RatelClientProducer getInstance() {
      return INSTANCE;
  }
  
  private ZookeeperFetcher zookeeperFetcher;

  public <T> T getServiceProxy(Class<T> serviceContractClass) {
    return null;
  }
  
}
