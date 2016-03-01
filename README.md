ratel - service discovery
=================

[![Build Status](https://travis-ci.org/PayU-Tech/Ratel.svg?branch=master)](https://travis-ci.org/PayU-Tech/Ratel)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.payu.ratel/ratel-project/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.payu.ratel/ratel-project)


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


# Reporting bugs and feature requests
    We use github issues to track bugs, improvements and feature requests.
    If you find security bug you can also send info to <security@payu.com>

# Contributing
    Your contribution is welcome.
    Please send pull request to development branch
    it is the only branch where we can accept your changes.

# Please visit project wiki for more info
[WIKI](https://github.com/PayU-Tech/Ratel/wiki)
