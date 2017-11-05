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

### Create or Update

Sample is as follow. Please note that user token is mocked in the current version. There is no token generation strategy.
This will also trigger a notification to other services.

```curl -H "User-Token: Xm28dxc" -X POST -d '{"id": "3", "name": "isButtonGreen", "version": "2", "service": "abc", "permission": true, "enabled": true }' http://localhost:9000/api/v1/toggle```

### Check if Toggle is enabled
```curl http://localhost:9000/api/v1/toggle/:id/version/:version/enabled```


## 3. Tests
Ensure you're running sbt and then just type.

```
test
```
This will run all tests, if you just want to run one test, use:


```
testOnly com.xpto.impl.<TestClass>
```





