##### © 2023 iamfortress.net

## Overview

### What's a Groovy Apache Fortress?

This sample uses Apache Groovy to wrap Apache Fortress [AccessMgr](https://directory.apache.org/fortress/gen-docs/latest/apidocs/org/apache/directory/fortress/core/AccessMgr.html) and [AdminMgr](https://directory.apache.org/fortress/gen-docs/latest/apidocs/org/apache/directory/fortress/core/AdminMgr.html) APIs.  It provides a deep dive into advanced RBAC use cases showing a novel way of invoking its APIs.
It's not a basic tutorial on the fundamentals of RBAC or Apache Fortress.  There are better places for that, start with the [Apache Fortress project page](https://directory.apache.org/fortress/).

### Why Apache Groovy?

It's a dynamic language running on top of the Java Virtual Machine. Its relaxed language syntactical rules allow test cases to be written quickly and with better clarity than with traditional Java code.  

## How This Sample Works

There's one module for adding the RBAC policy (AdminMgr), another for verifying it (AccessMgr).  
Each test module uses its wrapper for invoking Apache Fortress manager APIs, in order to do the work.

1. [GroovyAdminMgrTests](src/test/groovy/org/apache/directory/fortress/GroovyAdminMgrTests.groovy) -> [GroovyAdminMgr](src/main/groovy/org/apache/directory/fortress/GroovyAdminMgr.groovy) -> Apache Fortress AdminMgr
2. [GroovyAccessMgrTests](src/test/groovy/org/apache/directory/fortress/GroovyAccessMgrTests.groovy) -> [GroovyAccessMgr](src/main/groovy/org/apache/directory/fortress/GroovyAccessMgr.groovy) -> Apache Fortress AccessMgr

Notice how these tests differ when written in Groovy vs Java:

3. [JavaAdminMgrTests](src/test/java/org/apache/directory/fortress/AdminMgrTests.java).
4. [JavaAccessMgrTests](src/test/java/org/apache/directory/fortress/AccessMgrTests.java).

Both do exactly the same thing, Groovy does it with less boilerplate, which makes the code easier to read and write.  Test cases are hard enough to write, anything that makes it easier, helps.

## More About the Test Cases

Apache Fortress can place dynamic constraints on roles.  The idea is limiting when a particular role can be activated, under what conditions.  
For more on this concept:

 * [Towards an Attribute-Based Role-Based Access Control System](https://iamfortress.net/2018/07/07/towards-an-attribute-based-role-based-access-control-system/)
 * [Adding Contextual Information to the RBAC Decision](https://symas.com/adding-contextual-information-to-the-rbac-decision/)
 * [Designing an Authorization System: a Dialogue in Five Scenes](https://iamfortress.net/2019/11/23/designing-an-authorization-system-a-dialogue-in-five-scenes/)

## How To Run Test Cases

Run from within a particular Java IDE, like Netbeans, Eclipse or Intellij. Or, run them from the command line.

## What Else Is Needed to Test

An operational Apache Fortress runtime using one of its supported backends: e.g. OpenLDAP or Apache Directory:
 * [README-QUICKSTART-APACHEDS](https://github.com/apache/directory-fortress-core/blob/master/README-QUICKSTART-APACHEDS.md)
 * [README-QUICKSTART-SLAPD](https://github.com/apache/directory-fortress-core/blob/master/README-QUICKSTART-SLAPD.md)
 * [README-QUICKSTART-DOCKER-APACHEDS](https://github.com/apache/directory-fortress-core/blob/master/README-QUICKSTART-DOCKER-APACHEDS.md)
 * [README-QUICKSTART-DOCKER-SLAPD](https://github.com/apache/directory-fortress-core/blob/master/README-QUICKSTART-DOCKER-SLAPD.md)
 
### Fortress Config w/ System Properties:

Fortress config is bootstrapped in one of two ways:

#### System Env

Add to runtime parameters to backend: ```-Dfortress.admin.user=cn=Manager,dc=example,dc=com -Dfortress.admin.pw=secret -Dfortress.config.root=ou=Config,dc=example,dc=com```

or

#### Enable an LDAP server:

 a. Copy the example [fortress.properties.example](src/test/resources/fortress.properties.example) file.

 ```bash
 cp src/test/resources/fortress.properties.example src/test/resources/fortress.properties
 ```

 b. Edit the file:

 ```bash
 vi src/test/resources/fortress.properties
 ```

 Pick either Apache Directory or OpenLDAP server:

 c. Prepare fortress for ApacheDS usage:

 ```properties
 # This param tells fortress what type of ldap server in use:
 ldap.server.type=apacheds

 # Use value from [Set Hostname Entry]:
 host=localhost

 # ApacheDS defaults to this:
 port=10389

 # These credentials are used for read/write access to all nodes under suffix:
 admin.user=uid=admin,ou=system
 admin.pw=secret
 ```

 -- Or --

 d. Prepare fortress for OpenLDAP usage:

 ```properties
 # This param tells fortress what type of ldap server in use:
 ldap.server.type=openldap

 # Use value from [Set Hostname Entry]:
 host=localhost

 # OpenLDAP defaults to this:
 port=389

 # These credentials are used for read/write access to all nodes under suffix:
 admin.user=cn=Manager,dc=example,dc=com
 admin.pw=secret
 ```

### Setting Up Config Directory in IDE

Be sure to add to the system properties to the runtime classpath of the ide:  ```src/test/resources``` which contains the config files for fortress (if not set via sys props), ehcache and log4j.

Or, add the to the runtime config of the ide as in the System env above.

### Building the sample

```mvn clean install```

### Running groovy tests from command line

Use the uber jar from the build, located under target folder.

```
java -classpath target/fortress-core-groovy-1.0.0-jar-with-dependencies.jar:src/test/resources/ org.apache.directory.fortress.GroovyAdminMgrTests
java -classpath target/fortress-core-groovy-1.0.0-jar-with-dependencies.jar:src/test/resources/ org.apache.directory.fortress.GroovyAccessMgrTests
```

Note there will warnings in the console.  This is expected as roles that have been assigned aren't being activated due to constraints not matching.

For example, Huey signing into the East, the Washer will not activate due to locale constraint (more later).

```
start-> userId:null password:null constraints:[userId:Huey, password:password, locale:East, strength:2fa, roles:[Washer, Teller]]
Key: locale Value: East                                                                                          
Key: strength Value: 2fa                                                                                           
2020-04-12 16:12:025 INFO  VUtil:615 - validateConstraints role [Washer] for userId[huey] was deactivated reason code [2058]   
End Huey Teller in the East.                                                                                                 
```

### Running java tests from command line

```
java -classpath target/fortress-core-groovy-1.0.0-jar-with-dependencies.jar:src/test/resources/:target/test-classes/ org.apache.directory.fortress.AdminMgrTests
java -classpath target/fortress-core-groovy-1.0.0-jar-with-dependencies.jar:src/test/resources/:target/test-classes/ org.apache.directory.fortress.AccessMgrTests
```

## Understand the security policy

If you read 'toward an attribute-based' article listed above, here is a similar use case.  Instead of stooges, curly, moe, larry
we have ducks, huey, dewey and louie.  Again centered around a simple banking scenario.  These ducks can be either a Teller or Washer but 
are limited which branches this may occur in, and never both (roles) at the same time, due to a dynamic separation of duty constraint placed between them.

An additional constraint has been placed on the 'Teller' role.  That is not only is 'locale' verified, now we must also verify the 'strength'
of the authentication.  The idea, we want to be sure Tellers have been strongly authenticated.  Washers perhaps not as sensitive of operation will only require valid locale.

For example, here are Huey's role assignments:

| Role Name  | Constraint  | Value    |
| ---------- | ----------- | -------- |
| teller     | locale      | east     |
| teller     | strength    | 2fa      |
| washer     | locale      | north    |
| washer     | locale      | south    |

Huey has two roles assigned, teller and washer.  The washer role is constrained by location.  Huey may be a washer in the north and south.
But, the teller role has two constraints placed upon it, locale and strength (of authN).  This means Huey may only be a teller at branches
that are in the east AND where he logged on via a two-factor authentication mechanism.  If either are missing, that role will not activate.

Here's the raw data for Huey's assignments.  This is how its stored on Huey's account in the database:

```
teller$type$USER$locale$East$
teller$type$USER$strength$2FA$
washer$type$USER$locale$North$
washer$type$USER$locale$South$  
```
Here are Dewey's role assignments:

| Role Name  | Constraint  | Value    |
| ---------- | ----------- | -------- |
| teller     | locale      | north    |
| teller     | strength    | 2fa      |
| washer     | locale      | east     |
| washer     | locale      | south    |

Dewey can be a teller in the north, if strongly authenticated.  And a washer in the east and south.

Louie's assignments:

| Role Name  | Constraint  | Value    |
| ---------- | ----------- | -------- |
| teller     | locale      | south    |
| teller     | strength    | 2fa      |
| washer     | locale      | east     |
| washer     | locale      | north    |

Louie can be a teller in the south, if strongly authenticated.  And a washer in the east and north.

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
