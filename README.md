ratel - service discovery
=================

[![Build Status](https://travis-ci.org/HoneyBadgerTeam/Ratel.svg)](https://travis-ci.org/HoneyBadgerTeam/Ratel)

![client->ratel->server](http://yuml.me/diagram/scruffy/class/[FooClient]discover-%3E[Ratel%20Server],%20[FooServer]publish-%3E[Ratel%20Server],%20[FooClient]-%3E[FooServer])

`FooClient.java` in **Client** application

    @Service
    public class FooClient {
    
        @Discover
        private FooService fooService;
        
        ...
        
    }	



`FooService.java` in **Server** application

    @Service
    @Publish
    public class FooServiceImpl implements FooService {

        ...
        
    }	


FooService is auto-discovered by client application.

