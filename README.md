# a_groovy_apache_fortress
A groovy wrapper for Apache Fortress

## Fortress Config w/ System Properties:

Add to runtime: ```-Dfortress.admin.user=cn=Manager,dc=example,dc=com -Dfortress.admin.pw=secret -Dfortress.config.root=ou=Config,dc=example,dc=com```

Note: Fortress config can be set using system properties containing coordinates to node in ldap.

## Setting Up Config Directory

Add to runtime classpath:  ```src/test/resources``` which contains the config files for fortress (if not set via sys props), ehcache and log4j.

## Running tests

```
java -classpath target/fortress-core-groovy-0.0.1-SNAPSHOT-jar-with-dependencies.jar:src/test/resources/ org.apache.directory.fortress.core.groovy.FortressAdminMgrTests
```

## Understand the security policy

#### 1. User-to-Role Assignment Table

 For this app, user-to-role assignments are:

| user       | Teller      | Washer   |
| ---------- | ----------- | -------- |
| huey       | true        | true     |
| dewey      | true        | true     |
| louie      | true        | true     |

#### 2. User-to-Role Activation Table by Branch

 But we want to control role activation using attributes based on Branch location:

| user       | Teller    | Washer        |
| ---------- | --------- | ------------- |
| huey       | East      | North, South  |
| dewey      | North     | East, South   |
| louie      | South     | North, East   |

 *Even though the test users are assigned both roles, they are limited which can be activated by branch.*

#### 3. Role-to-Role Dynamic Separation of Duty Constraint Table

 Furthermore due to toxic combination, we must never let a user activate both roles simultaneously regardless of location. For that, we'll use a dynamic separation of duty policy.

| set name      | Set Members   | Cardinality   |
| ------------- | ------------- | ------------- |
| Bankers       | Washer        | 2             |
|               | Teller        |               |
|               |               |               |

#### 4. Role-Permissions

| role       | ACCT.deposit    | ACCT.withdrawal    | ACCT.inquiry     | MONEY.soak    | MONEY.rise    | MONEY.dry    |
| ---------- | --------------- | ------------------ | ---------------- | ------------- | ------------- | ------------ |
| Teller     | true            | true               | true             | false         | false         | false        |
| Washer     | false           | false              | false            | true          | true          | true         |

#### 5. User-Password Table

 | userId        | Password      |
 | ------------- | ------------- |
 | huey          | password      |
 | dewey         | password      |
 | louie         | password      |
