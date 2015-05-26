package com.payu.ratel.client.standalone;

import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ZK_HOST;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import com.payu.ratel.client.RatelClientProducer;
import com.payu.ratel.client.RatelServiceCallPublisher;
import com.payu.ratel.config.beans.RegistryBeanProviderFactory;
import com.payu.ratel.config.beans.RegistryStrategiesProvider;
import com.payu.ratel.context.RemoteServiceCallListener;

/**
 * Utility class that enables you to imperatively create ratel service clients.
 * Different factory methods allow you to create clients in various contexts:
 * out of container or within container, with different registry servers.
 */
public class RatelStandaloneFactory implements BeanFactoryAware, RatelServiceCallPublisher, RatelClientFactory {

    private RatelClientProducer clientProducer;
    private ConfigurableListableBeanFactory beanFactory;

    /**
     * Creates an instance of RatelStandaloneFactory that will use given
     * zookeeper as a service registry.
     *
     * @param zookeeperAddress
     *            a comma-separated list of zookeeper nodes to communicate to.
     * @return a fully configured RatelStandaloneFactory.
     */
    public static RatelClientFactory fromZookeeperServer(String zookeeperAddress) {
        ConfigurableListableBeanFactory beanFactory = new BeanFactoryBuilder("zookeeperAddr").//
                withProperty(SERVICE_DISCOVERY_ZK_HOST, zookeeperAddress).//
                build();

        RatelStandaloneFactory result = new RatelStandaloneFactory();
        result.setBeanFactory(beanFactory);

        return result;
    }

    /**
     * Creates an instance of RatelStandaloneFactory that will use given ratel
     * server as a service registry.
     *
     * @param ratelServerAddr
     *            an url with a ratel registry service.
     *            Must contain complete address with path to the registry
     *            service
     *            (default
     *            http:[ratel_server_address]:[port]/server/discovery")
     * @return a fully configured RatelStandaloneFactory.
     */
    public static RatelClientFactory fromRatelServer(String ratelServerAddr) {
        ConfigurableListableBeanFactory beanFactory = new BeanFactoryBuilder("ratelAddr").//
                withProperty(SERVICE_DISCOVERY_ADDRESS, ratelServerAddr).//
                build();

        RatelStandaloneFactory result = new RatelStandaloneFactory();
        result.setBeanFactory(beanFactory);

        return result;
    }

    /**
     * Create a RatelStandaloneFactory with use of zookeeper service given in
     * system properties.
     * Equivalent of {@link #fromZookeeperServer(String)} but the zookeeper
     * address is read from system property
     * {@link RegistryBeanProviderFactory#SERVICE_DISCOVERY_ZK_HOST}.
     * If the property is not present, this method will throw an exception.
     *
     * @return fully configured RatelStandaloneFactory bound to zookeeper
     *         service registry.
     */
    public static RatelClientFactory fromZookeeperServer() {
        String zookeeperHost = System.getProperty(SERVICE_DISCOVERY_ZK_HOST);
        return fromZookeeperServer(zookeeperHost);
    }

    /**
     * Create a RatelStandaloneFactory with use of Ratel service given in
     * system properties.
     * Equivalent of {@link #fromRatelServer(String)} but the ratel
     * service address is read from system property
     * {@link RegistryBeanProviderFactory#SERVICE_DISCOVERY_ADDRESS}.
     * If the property is not present, this method will throw an exception.
     *
     * @return fully configured RatelStandaloneFactory bound to Ratel
     *         service registry.
     */
    public static RatelClientFactory fromRatelServer() {
        String ratelServer = System.getProperty(SERVICE_DISCOVERY_ADDRESS);
        return fromRatelServer(ratelServer);
    }

    /**
     * Creates a RatelStandaloneFactory bound to either zookeeper server or
     * Ratel Server, based on system properties.
     * Exactly one of
     * {@link RegistryBeanProviderFactory#SERVICE_DISCOVERY_ADDRESS} and
     * {@link RegistryBeanProviderFactory#SERVICE_DISCOVERY_ZK_HOST} must be
     * present in system properties.
     *
     * If {@link RegistryBeanProviderFactory#SERVICE_DISCOVERY_ADDRESS} is
     * present, then this method is eqivalent to {@link #fromRatelServer()}.
     * If {@link RegistryBeanProviderFactory#SERVICE_DISCOVERY_ZK_HOST} is
     * present, then this method is eqivalent to {@link #fromZookeeperServer()}
     * If none of the above is present, then this method will throw an
     * exception.
     *
     * @return Fully configured RatelStandaloneFactory
     */
    public static RatelClientFactory fromAnyConfiguration() {
        if (System.getProperty(SERVICE_DISCOVERY_ADDRESS) != null) {
            return fromRatelServer();
        }
        if (System.getProperty(SERVICE_DISCOVERY_ZK_HOST) != null) {
            return fromZookeeperServer();
        }
        throw new IllegalStateException("Can't resolve zookeeper address nor ratel server address.");
    }

    /**
     * Creates a RatelStandaloneFactory on the basis of configuration present
     * in a given beanFactory.
     * If the beanFactory already has Ratel configured, then the result of this
     * method will use exactly the same configuration.
     * If it not present then this method is equivalent of
     * {@link #fromAnyConfiguration()}, but Environment registered in
     * beanFactory will be used in place of system properties.
     *
     * @param beanFactory
     *           a beanFactory to use
     * @return fully configured RatelStandaloneFactory
     */
    public static RatelClientFactory fromBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        RatelStandaloneFactory result = new RatelStandaloneFactory();
        result.setBeanFactory(beanFactory);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getServiceProxy(Class<T> serviceContractClass) {
        return clientProducer.produceServiceProxy(serviceContractClass, false, null);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            ConfigurableListableBeanFactory lbl = (ConfigurableListableBeanFactory) beanFactory;

            this.beanFactory = lbl;
            RegistryStrategiesProvider strategiesProvider = new RegistryBeanProviderFactory()
                    .create(this.beanFactory);

            this.clientProducer = new RatelClientProducer(strategiesProvider.getFetchStrategy(),
                    strategiesProvider.getClientProxyGenerator());
        } else {
            throw new IllegalArgumentException("Only ConfigurableListableBeanFactory is provided ");
        }
    }

    public void addRatelServiceCallListener(RemoteServiceCallListener listener) {
        this.beanFactory.registerSingleton(listener.getClass().getName(), listener);
    }
}

class BeanFactoryBuilder {
    private final String name;
    private final Map<String, Object> environmentProperties = new HashMap<>();

    public BeanFactoryBuilder(String sourcePropertyName) {
        super();
        this.name = sourcePropertyName;
    }

    BeanFactoryBuilder withProperty(String name, Object value) {
        environmentProperties.put(name, value);
        return this;
    }

    public ConfigurableListableBeanFactory build() {
        ConfigurableListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ConfigurableEnvironment env = new StandardEnvironment();
        MapPropertySource propertySource = new MapPropertySource(this.name, environmentProperties);
        env.getPropertySources().addFirst(propertySource);
        beanFactory.registerSingleton("environment", env);
        return beanFactory;
    }

}
