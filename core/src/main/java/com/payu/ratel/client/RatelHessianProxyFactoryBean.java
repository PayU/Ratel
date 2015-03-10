package com.payu.ratel.client;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

public class RatelHessianProxyFactoryBean extends HessianProxyFactoryBean {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return super.invoke(invocation);
        } catch (RemoteConnectFailureException e) {
            throw e;
        } catch (RemoteAccessException e) {
            if (e.getRootCause() != null)
                throw e.getRootCause();
            else
                throw e;
        }
    }
}