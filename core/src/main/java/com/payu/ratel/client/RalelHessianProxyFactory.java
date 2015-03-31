package com.payu.ratel.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;

import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.HessianRemoteObject;
import com.payu.ratel.context.RatelHessianProxy;

public class RalelHessianProxyFactory extends HessianProxyFactory {
	
	  public Object create(Class<?> api, URL url, ClassLoader loader)
	  {
	    if (api == null)
	      throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
	    InvocationHandler handler = null;

	    handler = new RatelHessianProxy(url, this, api);

	    return Proxy.newProxyInstance(loader,
	                                  new Class[] { api,
	                                                HessianRemoteObject.class },
	                                  handler);
	  }

}
