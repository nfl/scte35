SCETE35
=======

This library was forked from https://github.com/riedlse/scte35. 

Goals
=====

* Remove Netbeans dependency 
* Remove UI dependency
* Compatibility with Android
* Fully define model

How to use
==========

Import dependency
```
compile 'com.nfl.dm.scte35:scte35-decoder:1.0.7'
```

Create or inject instance: 
```
Scte35Decoder scte35Decoder = new Scte35Decoder(false);
SpliceInfoSection spliceInfoSection = scte35Decoder.base64Decode("SCTE35_ENCODED_STRING");
//Use valies from model: spliceInfoSection
```

TODOs
=====

* Remove (Or clean up) Base64 class
* Make models immutable
* Add more examples to increase code coverage