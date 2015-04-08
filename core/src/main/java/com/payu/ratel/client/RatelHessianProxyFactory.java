package com.payu.ratel.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.HessianRemoteObject;
import com.payu.ratel.context.RatelHessianProxy;

/**
 * A custom implementation of {@link HessianProxyFactory} that creates instances
 * of {@link RatelHessianProxy} as remote hessian proxies. 
 *
 */
public class RatelHessianProxyFactory extends HessianProxyFactory {

  public Object create(Class<?> api, URL url, ClassLoader loader) {
    if (api == null)
      throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
    InvocationHandler handler = null;

    handler = new RatelHessianProxy(url, this, api);

    return Proxy.newProxyInstance(loader, new Class[] { api, HessianRemoteObject.class }, handler);
  }

}
