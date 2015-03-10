package com.payu.ratel.config.beans;

import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.FetchStrategy;
import com.payu.ratel.register.RegisterStrategy;

public interface RegistryBeanProvider {

    RegisterStrategy getRegisterStrategy();
    FetchStrategy getFetchStrategy();
    ClientProxyGenerator getClientProxyGenerator();

}
