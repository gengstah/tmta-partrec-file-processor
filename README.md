## Complaints Dashboard ~ Drewps

The Complaints Dashboard ~ Drewps provides a web application to easily manage and view current and closed complaints.


## Building from source
The Complaints Dashboard ~ Drewps uses a [Gradle](http://gradle.org)-based build system. In the instructions
below, [`./gradlew`](http://vimeo.com/34436402) is invoked from the root of the source tree and
serves as a cross-platform, self-contained bootstrap mechanism for the build. The only
prerequisites are [git](http://help.github.com/set-up-git-redirect) and JDK 1.6+.

### check out sources
`git clone git://github.com/gengstah/complaints-dashboard-drewps.git`

### compile and test, build all jars, distribution zips and docs
`./gradlew build`








## Additional configuration

server.xml (%TOMCAT_INSTALLATION%/conf)

Comment out the existing realm then add this tag.

```xml
<Realm className="org.apache.catalina.realm.JDBCRealm"
    driverName="com.mysql.jdbc.Driver" digest="MD5"
    connectionURL="jdbc:mysql://localhost:3306/complaints-dashboard-drewps"
    connectionName="root" connectionPassword="root"
    userTable="USERS" userNameCol="USERNAME" userCredCol="PASSWORD"
    userRoleTable="USERS_ROLENAMES" roleNameCol="ROLENAME" />
```


## License
The Complaints Dashboard ~ Drewps is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).
