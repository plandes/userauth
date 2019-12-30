# Java Command Line User Authentication

[![Travis CI Build Status][travis-badge]][travis-link]
[![Maven Central][maven-badge]][maven-link]

This is an interface to authenticate and get user information in UNIX/Linux
environments.  This just wraps command line utilities and so that JNI, and
thus, system specific compilation can be avoided.

Specifically, this provides:
* User authentication using the [pwauth] program, which is commonly available
  on UNIX/Linux machines as a package.
* User full name and other basic information using the [getent] binary.
* Can be used for the fairly simple and useful IPC/process API (`Command` class).
* Pure Java implementation.

Paths of these binaries default to those on Debian systems.  However, if the
path isn't found `which` is used to find it.  You can override the default
path with `com.zensols.unix.userauth.UserManager.overridePath`.


## Obtaining

In your `pom.xml` file add
the
[dependency XML element](https://plandes.github.io/userauth/dependency-info.html) below:
```xml
<dependency>
    <groupId>com.zensols.sys</groupId>
    <artifactId>userauth</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Installation

The [pwauth] program is installed on Debian/Ubuntu machines with:
```bash
apt-get install pwauth
```

On Redhat/Fedora/CentOS:
```bash
yum install pwauth
```


## Documentation

More [documentation](https://plandes.github.io/userauth/):
* [Javadoc](https://plandes.github.io/userauth/apidocs/index.html)
* [Dependencies](https://plandes.github.io/userauth/dependencies.html)


## Building

To build from source, do the following:

- Install [Maven](https://maven.apache.org)
- Install [GNU make](https://www.gnu.org/software/make/) (optional)
- Build the software: `make`
- Build the distribution binaries: `make dist`

Note that you can also build a single jar file with all the dependencies with:
`make package`.


## Other Libraries

Jenkins has a [plugin] available, which uses [pwauth].  However, it is tightly
coupled their framework, for which libraries are difficult to find.


## Changelog

An extensive changelog is available [here](CHANGELOG.md).


## License

Copyright © 2019 Paul Landes

Apache License version 2.0

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


<!-- links -->
[travis-link]: https://travis-ci.org/plandes/userauth
[travis-badge]: https://travis-ci.org/plandes/userauth.svg?branch=master

[maven-link]: https://maven-badges.herokuapp.com/maven-central/com.zensols.sys/userauth
[maven-badge]: https://maven-badges.herokuapp.com/maven-central/com.zensols.sys/userauth/badge.svg

[pwauth]: https://github.com/phokz/pwauth
[getent]: https://en.wikipedia.org/wiki/Getent
[plugin]: https://github.com/jenkinsci/pwauth-plugin

