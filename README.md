# a_groovy_apache_fortress
A groovy wrapper for Apache Fortress

## Fortress Config w/ System Properties:

Add to runtime: ```-Dfortress.admin.user=cn=Manager,dc=example,dc=com -Dfortress.admin.pw=secret -Dfortress.config.root=ou=Config,dc=example,dc=com```

Note: Fortress config can be set using system properties containing coordinates to node in ldap.

## Setting Up Config Directory

Add to runtime classpath:  ```src/test/resources``` which contains the config files for fortress (if not set via sys props), ehcache and log4j.

## Running tests

```
java -classpath target/fortress-core-groovy-0.0.1-SNAPSHOT-jar-with-dependencies.jar:src/test/resources/ org.apache.directory.fortress.core.groovy.AddFortressAbacDataTest
```


