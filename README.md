# KTM Technologies nlcmd -- a natural language phrase matching library for Android

![Android CI](https://github.com/ktm-technologies/nlcmd/workflows/Android%20CI/badge.svg)

Android supports a number of voice control features, but enabling an individual
app for fine grained voice control is not straight forward. This library helps to
achieve that.

Features include
* Fast and efficient markov-chain based matching.
* Simple API for training markov chains from an string array of possible
  commands variations or loading models from JSON data.
* Support for generic terms such as locations or objects, such that they
  don't need to be enumerated in the training data.
* Support for creating [Graphviz Dot](https://www.graphviz.org) representations
  of models for visualization and debugging

The code is in proof-of-concept stage and feedback is welcome. 
* Maven repo: <a href="https://github.com/ktm-technologies/nlcmd/packages/178866">https://github.com/ktm-technologies/nlcmd/packages/178866</a>
* Github page: <a href="https://github.com/ktm-technologies/nlcmd/">https://github.com/ktm-technologies/nlcmd/</a>
* API reference: <a href="https://ktm-technologies.github.io/nlcmd/doc/">https://ktm-technologies.github.io/nlcmd/doc/</a>
* Releases: <a href="https://ktm-technologies.github.io/nlcmd.html">https://ktm-technologies.github.io/nlcmd.html</a>
* Example app: <a href="https://github.com/ktm-technologies/nlcmd-android-examples">https://github.com/ktm-technologies/nlcmd-android-examples</a>

Proudly supported by [KTM Technologies](https://ktm-technologies.com)
