package com.payu.ratel.context;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Java form of a payload send to a remote service. Contains information about
 * the contract, the parameter values and the particular endpoint used.
 *
 */
public class ServiceCallInput {

    private final Class apiType;
    private final String methodName;
    private final Object[] args;


    public ServiceCallInput(String methodName, Object[] args, Class apiType) {
        this.apiType = apiType;
        this.methodName = methodName;
        this.args = ArrayUtils.clone(args);
    }

    /**
     * Contract interface for the invoked service.
     * @return the contract interface for the invoked service.
     */
    public Class getApiType() {
        return apiType;
    }

    public String getMethodName() {
        return methodName;
    }

    @SuppressWarnings("PMD")
    public Object[] getArgs() {

        return ArrayUtils.clone(args);
    }

    @Override
    public String toString() {
        return "ServiceCallInput [apiType=" + apiType + ", methodName=" + methodName + ", args="
                + Arrays.toString(args) + "]";
    }




}
