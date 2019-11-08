# Course and Module Catalogue

This is a system for displaying information about courses and modules available at Warwick.

This is a [Spring Boot](https://spring.io/projects/spring-boot) application written in [Kotlin](https://kotlinlang.org). [Hibernate](https://hibernate.org/orm/) is used for persistence. Database migrations are handled by [Flyway](https://flywaydb.org). [FreeMarker](https://freemarker.apache.org) is the template language. Authentication is provided via [SSO Client](https://github.com/UniversityofWarwick/sso-client) and authorisation is managed by [Spring Security](https://spring.io/projects/spring-security).

## Getting started

1. Copy `application-example.yml` to `application.yml` and replace the example PostgreSQL database details with your own
1. Copy `external-conf/camcat-sso-config-example.xml` to `external-conf/camcat-sso-config.xml` and add your own details
1. Run `./gradlew bootRun` to install dependencies, build assets and compile and run the application
1. Run `./gradlew test` to run the tests

To run within IDEA, edit the run configuration and set Environment > VM options to `-classpath $ProjectFileDir$/external-conf:$Classpath$`.

Use Docker to run a local Elasticsearch instance with Kibana:

```docker-compose up```

Kibana is available at http://localhost:5601.
