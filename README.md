ktape
=====

TAP [1] output producing test runner for Kotlin mostly ported from
[@substack's](https://github.com/substack)
[tape](https://github.com/substack/tape) module for Node.js [2]. This
package is installable with NPM [3], IMS [4], clib [5], GHI [6], and of
course, from [source][#install-from-source]

## Installation

### From NPM

```sh
$ npm install datkt/ktape
```

**Note:** *This will install **ktape** into `node_modules/ktape`*

### From clib

```sh
$ clib install datkt/ktape
```

**Note:** *This will install **ktape** into `deps/ktape`*

### From GHI

```sh
$ ghi install datkt/ktape
```

### Install From Source

```sh
$ git clone git@github.com:datkt/ktape.git
$ cd ktape
$ make
$ make install
```

## Usage

```kotlin
import ktape.*
import lerp.*

fun main(args: Array<String>) {
  test("lerp(a, b, c)", fun(t: Test) {
    // tests
  })
}
```

## API

### `test(name: String, callback: (t: Test) -> Unit?)`

Creates and returns a new named scoped test. The test callback
will not be invoked if null is given.

```kotlin
test("function()", fun(t: Test) {
  t.plan(1)
  t.ok(true, "we're okay")
  t.end()
})
```

### `t.plan(count: Int): Test`

Ensure a planned assertion count for a test. Will throw `Error` if
`count` is `0`.

## References

1. TAP - https://en.wikipedia.org/wiki/Test_Anything_Protocol
2. Node.js - https://nodejs.org/en/
3. NPM - https://www.npmjs.com/
4. IMS - https://github.com/mafintosh/ims
5. clib - https://github.com/clibs/clib
6. GHI - https://github.com/stephenmathieson/ghi
