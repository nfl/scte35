<img src="http://static.nfl.com/static/content/public/static/img/logos/nfl-engineering-light.svg" width="300" />

# SCETE 35 Decoder

[![Build Status](https://github.com/nfl/scte35/workflows/Java%20CI%20with%20Gradle/badge.svg)]
[![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/nfl/scte35/blob/master/LICENSE)

Java library that handles decoding strings that follow the SCTE 35 Standard: https://en.wikipedia.org/wiki/SCTE-35. The result is an Plain Old Java Object of type `SpliceInfoSection`

This library was forked from https://github.com/riedlse/scte35. 

## Goals
* Provide a simple interface that can be instatiated or injected
* Remove Netbeans dependency 
* Remove UI dependency
* Compatibility with Android
  * Removing dependency to `javax.xml.bind.DatatypeConverter` http://stackoverflow.com/a/34424297. Using `commons-codec`
  * Removing  ` com.turner.decoder.Base64`. Using `commons-codec`
  * Keep it independent of Android libraries
* Fully define model
* Move to gradle
* Add unit tests

## Integration
This library is made available via maven repository. Alternatively it can be downloaded and included directly into a private maven repository or directly in the libs folder of the target app.
### Add maven repository
```groovy
  repositories {
      maven {
        name = "NFL Scte35"
        url = uri("https://maven.pkg.github.com/nfl/scte35")
      }
    }
```
### Add dependency
```groovy
  dependencies {
...
  implementation 'com.nfl.dm.scte35:scte35-decoder:1.0.31'
...
  }
```
### Create or inject instance:
```java
Scte35Decoder scte35Decoder = new Scte35Decoder(false);
SpliceInfoSection spliceInfoSection = scte35Decoder.base64Decode("SCTE35_ENCODED_STRING");
//Use values from model: spliceInfoSection
```

## TODOs
* Make models immutable
* Add more examples to increase code coverage
