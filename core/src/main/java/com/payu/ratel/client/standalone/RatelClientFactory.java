package com.payu.ratel.client.standalone;

import com.payu.ratel.config.RetryPolicyConfig;
import com.payu.ratel.config.TimeoutConfig;

/**
 * This interface represents a class which can be used to produce
 * fully-featured Ratel service proxies, on the basis of the service contract
 * only.
 *
 * @see RatelStandaloneFactory
 */
public interface RatelClientFactory {

    /**
     * Creates a lazily-initialized fully-featured client of the ratel service
     * with a given contract.
     * Creation of a proxy does not guarantee that such a service exist.
     * A lazy initialization means that the client will try to lookup an
     * address of a service with first call attempt.
     * The client has all client-side features provided by Ratel, such as
     * load-balancing, monitoring, fail-over, etc.
     *
     * @param <T>
     *            a type of the service created should contain contract
     *            interface for the service
     * @param serviceContractClass
     *            the desired contract class.
     * @param timeout
     *            timeout configuration for calling remote endpoint
     * @return a fully-featured service client.
     */
    <T> T getServiceProxy(Class<T> serviceContractClass, TimeoutConfig timeout);

    /**
     * @param <T>                  a type of the service created should contain contract
     *                             interface for the service
     * @param serviceContractClass the desired contract class.
     * @see RatelClientFactory#getServiceProxy(Class, TimeoutConfig)
     * @return a fully-featured service client.
     */
    <T> T getServiceProxy(Class<T> serviceContractClass);

    /**
     * @param <T>                  a type of the service created should contain contract
     *                             interface for the service
     * @param serviceContractClass the desired contract class.
     * @param retryPolicy          retry policy configuration for calling remote endpoint
     * @param timeout              timeout configuration for calling remote endpoint
     * @see RatelClientFactory#getServiceProxy(Class, TimeoutConfig)
     * @return a fully-featured service client.
     */
    <T> T getServiceProxy(Class<T> serviceContractClass, RetryPolicyConfig retryPolicy, TimeoutConfig timeout);

}
