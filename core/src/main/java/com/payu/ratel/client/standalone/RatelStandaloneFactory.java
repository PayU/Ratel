package com.payu.ratel.client.standalone;

import com.payu.ratel.client.zookeeper.ZookeeperFetcher;

public class RatelStandaloneFactory {
  
  private static final RatelStandaloneFactory INSTANCE = null;

  private static RatelStandaloneFactory getInstance() {
      return INSTANCE;
  }
  
  private ZookeeperFetcher zookeeperFetcher;

  public <T> T getServiceProxy(Class<T> serviceContractClass) {
    return null;
  }
  
}
