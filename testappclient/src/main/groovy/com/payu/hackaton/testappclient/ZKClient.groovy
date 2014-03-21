package com.payu.hackaton.testappclient

import com.payu.hackathon.discovery.client.DiscoveryClient
import com.payu.hackathon.discovery.client.ZkDiscoveryClient
import com.payu.hackathon.discovery.model.Service

/**
 * Created by ext_przemyslaw.galaz on 21.03.2014.
 */

@Singleton
class ZKClient {

    Set<Service> services = new HashSet<Service>()

    def registerListener() {

        String address = 'localhost:7777'
        DiscoveryClient discoveryClient = new ZkDiscoveryClient(address)

        services.addAll discoveryClient.fetchAllServices()

        discoveryClient.listenForServices(services) {
            Service service -> services.add(service)
        }


    }

}
