package com.payu.ratel.client;

import java.lang.reflect.Proxy;

import com.payu.ratel.proxy.CacheInvocationHandler;
import com.payu.ratel.proxy.RetryPolicyInvocationHandler;
import com.payu.ratel.proxy.UnicastingInvocationHandler;

public class RatelClientProducer {
  private FetchStrategy fetchStrategy;
  private ClientProxyGenerator clientProxyGenerator;


  public RatelClientProducer(FetchStrategy fetchStrategy, ClientProxyGenerator clientProxyGenerator) {
    super();
    this.fetchStrategy = fetchStrategy;
    this.clientProxyGenerator = clientProxyGenerator;
  }

  public FetchStrategy getFetchStrategy() {
    return fetchStrategy;
  }

  public void setFetchStrategy(FetchStrategy fetchStrategy) {
    this.fetchStrategy = fetchStrategy;
  }

  public ClientProxyGenerator getClientProxyGenerator() {
    return clientProxyGenerator;
  }

  public void setClientProxyGenerator(ClientProxyGenerator clientProxyGenerator) {
    this.clientProxyGenerator = clientProxyGenerator;
  }

  public <T> T produceServiceProxy(RemoteAutowireCandidateResolver remoteAutowireCandidateResolver,
      Class<T> serviceContractClass, boolean useCache, Class<? extends Throwable> retryOnException) {
    T client = produceUnicaster(serviceContractClass);
    

    if (useCache) {
      client = decorateWithCaching(client, serviceContractClass);
    }

    if (retryOnException != null) {
      client = decorateWithRetryPolicy(client, serviceContractClass, retryOnException);
    }
    
    return client;
  }

  @SuppressWarnings("unchecked")
  private <T> T produceUnicaster(Class<T> clazz) {
    return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { clazz },
        new UnicastingInvocationHandler(getFetchStrategy(), clazz, getClientProxyGenerator()));
  }

  @SuppressWarnings("unchecked")
  private <T> T decorateWithCaching(final Object object, final Class<T> clazz) {
    return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { clazz },
        new CacheInvocationHandler(object));
  }

  @SuppressWarnings("unchecked")
  private <T> T decorateWithRetryPolicy(final Object object, final Class<T> clazz,
      final Class<? extends Throwable> exception) {
    return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { clazz },
        new RetryPolicyInvocationHandler(object, exception));
  }

}