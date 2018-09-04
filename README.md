# bshpvs [![Build Status](https://travis-ci.com/thomasmchen/battleship-arena.svg?token=3EVMzaEnmkJ6Sm4cbJ4T&branch=master)](https://travis-ci.com/thomasmchen/battleship-arena) [![codecov](https://codecov.io/gh/thomasmchen/bshpvs/branch/master/graph/badge.svg?token=BVLIUv0Yps)](https://codecov.io/gh/thomasmchen/bshpvs)
Competitive Arena for Battleship AIs

## Testing
Run linter via:
```
mvn checkstyle:checkstyle -Dcheckstyle.consoleOutput=true
```
Run tests:
```
mvn test
```
Sample start flow:
```
mvn clean validate
mvn clean compile
mvn checkstyle:checkstyle -Dcheckstyle.consoleOutput=true
mvn clean test
```

## Contributing
Please refer to the [Git Flow](https://github.com/thomasmchen/bshpvs/wiki/Git-Flow) document in the Wiki for guidance on contributing.

## Versioning
Meter adheres to Semantic Versioning 2.0.0. Learn more [here](https://semver.org/). <br>
The current version of bshpvs is `0.0.1`.
