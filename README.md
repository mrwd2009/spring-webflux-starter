# Let's go
This project is used to provide a basic skeleton for Spring Webflux application including following features.
1. Support two database migrations using liquibase and r2dbc.
2. Support openid connect(oauth2) login flow with custom classes.
3. Support two database repositories using spring data r2dbc.


Java version >= 21

Here is demo content of application-development.yaml
```
logging:
  level:
    root: INFO
    org.springframework.r2dbc: DEBUG
    org:
      springframework:
        http:
          server:
            reactive: TRACE

spring:
  security:
    oauth2:
      client:
        registration:
          auth0:
            client-id: xxx
            client-secret: xxx
            client-authentication-method: client_secret_post
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: "openid,profile,email,offline_access"
            client-name: "Auth0"
        provider:
          auth0:
            issuer-uri: https://cfex-dev.us.auth0.com/

management:
  endpoints:
    enabled-by-default: true
    web:
      exposure:
        include: "*"
cfex-oc:
  session:
    cookie-key: "oc-session"
    max-age-minutes: 480
    effective-minutes: 240
    extending-minutes: 120
  jwt:
    rsa-key-id: "oc"
    rsa-private-key: "xxx"
    rsa-public-key: "xxx"
  crypto:
    password: "XczMo1cLKJLKD(*A5w8hhAeVNQ1ZwOsw6d2zi4764fR0TG0GbTuKttQ=="
    salt: "itgjomQfkvWKJDYb+R6rww=="
  r2dbc:
    contract:
      url: r2dbc:pool:mysql://localhost:3306/contract
      username: xxx
      password: xxx
    gateway:
      url: r2dbc:pool:mysql://localhost:3306/gateway
      username: xxx
      password: xxx
  liquibase:
    contract:
      user: "xxx"
      password: "xxx"
      url: "jdbc:mysql://localhost:3306/contract"
      change-log: "classpath:/db/changelog/contract/master.xml"
    gateway:
      user: "xxx"
      password: "xxx"
      url: "jdbc:mysql://localhost:3306/gateway"
      change-log: "classpath:/db/changelog/gateway/master.xml"

```


## Available Scripts
In the project directory, you can run:

### `./gradlew bootRun`

Runs the app in the development mode.<br />
Service point is [http://localhost:4102](http://localhost:4102).





