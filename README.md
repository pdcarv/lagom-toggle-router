# XPTO


# 1. Install

### 1.1 Start by installing sbt

MAC OS X

```brew install sbt```

Linux

```sudo apt-get install sbt```

Windows

Download the [installer](https://github.com/sbt/sbt/releases/download/v1.0.2/sbt-1.0.2.msi)

### 1.2 Checkout the project
```git clone git@github.com:pdcarv/lagom-toggle-router.git```

### 1.3 Run the project
Change into the project directory and, in your terminal of choice, type:

```sbt runAll```

This will run all services along with an embedded kafka and cassandra server.


# 2. Usage

### Get a toggle
```curl http://localhost:9000/api/v1/toggle/:id/version/:version```

Where id and version are the Toggle's corresponding id and version.

Lagom defines a PersistentEntity API that uses [event sourcing](https://msdn.microsoft.com/en-us/library/jj591559.aspx). This
means that we are persisting events, and deriving their current state from their history. As there is no mutation of data, it means
a high transaction rate and less concurrency and consistency issues. This example uses PersistentEntities. Lagom implements
persistent entities on top of cassandra keystore.

### Create or Update

Sample is as follow. Please note that user token is mocked in the current version. There is no token generation strategy.
This will also trigger a notification to other services.

```curl -H "User-Token: Xm28dxc" -X POST -d '{"id": "3", "name": "isButtonGreen", "version": "2", "service": "abc", "permission": true, "enabled": true }' http://localhost:9000/api/v1/toggle```

There are several strategies for asynchronous communication in lagom, [akka's distributed pub/sub](https://doc.akka.io/docs/akka/current/scala/distributed-pub-sub.html) and the [message broker api](https://www.lagomframework.com/documentation/1.3.x/java/MessageBrokerApi.html)
stick out. The message broker api reads and writes to and from an event stream, in lagom terminology this is known as subscribing or publishing to a Topic.
That capability is provided by a kafka instance that comes bundle in development environment, no configuration needed.
Lagom uses kafka providing two strategies for subscribers, atLeastOnce and atMostOnceSource. We're using atLeastOnce in this example which guarantees that a configuration change notification is consumed, at least once or possiblyt  more than once.


### Check if Toggle is enabled
```curl http://localhost:9000/api/v1/toggle/:id/version/:version/enabled```

Message serialization is provided by default, though one can implement their own serialization for each endpoint. In a similar mechanism one can customize HTTP header requests or responses by writing custom principals.
This keeps transport logic separate from our services. A sample authentication is provided using a custom security header.

Conductr comes bundle for service discovery that we're not using in this example. However for a production environment we might want to use something as consul for discovery and service authorization.

## 3. Tests
Ensure you're running sbt and then just type.

```
test
```

This will run all tests, if you just want to run one test, use:


```
testOnly com.xpto.impl.<TestClass>
```





