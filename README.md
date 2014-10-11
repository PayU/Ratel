ratel - service discovery
=================


<img href="client-ratel-server.png"/>

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

