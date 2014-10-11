ratel - service discovery
=================


![client->ratel->server](http://yuml.me/diagram/scruffy/class/[FooClient]-%3E[Ratel%20Server],%20[Ratel%20Server]-%3E[FooService])

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
    public class FooService {

        ...
        
    }	


FooService is auto-discovered by client application.

