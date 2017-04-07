<img src="http://static.nfl.com/static/content/public/static/img/logos/nfl-engineering-light.svg" width="300" />

# SCETE 35 Decoder

[![Build Status](https://travis-ci.org/nfl/scte35decoder.svg?branch=master)](https://travis-ci.org/nfl/scte35decoder) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.nfl.scte35decoder/scte35decoder/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.nfl.scte35decoder/scte35decoder) [ ![Download](https://api.bintray.com/packages/nfl/maven/scte35decoder/images/download.svg) ](https://bintray.com/nfl/maven/scte35decoder/_latestVersion) [![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/nfl/scte35decoder/blob/master/LICENSE)

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

## How to use

Import dependency
```gradle
compile 'com.nfl.dm.scte35:scte35-decoder:1.0.30'
```

Create or inject instance: 
```
Scte35Decoder scte35Decoder = new Scte35Decoder(false);
SpliceInfoSection spliceInfoSection = scte35Decoder.base64Decode("SCTE35_ENCODED_STRING");
//Use values from model: spliceInfoSection
```

## TODOs
* Remove (Or clean up) Base64 class
* Make models immutable
* Add more examples to increase code coverage