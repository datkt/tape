tape
====

TAP [1] output producing test runner for Kotlin mostly ported from
[@substack's](https://github.com/substack)
[tape](https://github.com/substack/tape) module for Node.js [2]. This
package is installable with NPM [3], IMS [4], clib [5], GHI [6], and of
course, from [source][#install-from-source]

## Installation

The `tape` package an be installed with various package managers.

### From NPM

```sh
$ npm install @datkt/tape
```

**Note:** *This will install **tape** into `node_modules/@datkt/tape`*

### From clib

```sh
$ clib install datkt/tape
```

**Note:** *This will install **tape** into `deps/tape`*

### From GHI

```sh
$ ghi install datkt/tape
```

### Install From Source

```sh
$ git clone git@github.com:datkt/tape.git
$ cd tape
$ make build # or make klib
$ make install
```

## Compiling

### From NPM Installation

```sh
$ konanc -r node_modules/@datkt/tape -l tape test.kt -o test
```

### From clib Installation

```sh
$ konanc deps/@datkt/tape/*.kt test.kt -o test
```

### From Source Installation

```sh
$ konanc -l tape test.kt -o test ## library should be installed in ~/.konan/klib
```

## Usage

```kotlin
import tape.*
import lerp.*

fun main(args: Array<String>) {
  test("lerp(a, b, c)", fun(t: Test) {
    val x = 1.0
    val y = 2.0
    val z = lerp(x, y, 1.0)

    t.plan(1)
    t.ok(2.0 == z, "lerp compute fail :shrug:")
    t.end()
  })
}
```

## API

The `tape` package exports a public API documented in this section.

### `test(name: String, callback: (t: Test) -> Unit?): Test`

Creates and returns a new named scoped test. The test callback
will not be invoked if null is given.

```kotlin
test("function()", fun(t: Test) {
  t.plan(1)
  t.ok(true, "we're okay")
  t.end()
})
```

### `class Test(name: String?, skip: Boolean, callback: Callback?, stream: Stream?)`

The `Test` class represents a named test that is invoked in a
function callback. When a test is running, it will call various function hooks
and write TAP formatted output to a stream, defaulting to stdout
(`println`).

```kotlin
t = Test("thing", false, fun(t: Test) {
  t.end
})

t.run()
```

#### `t.onBeforeRun(callback: (Test) -> Unit?)`

Add a callback that will be invoked before the test is ran.

#### `t.plan(count: Int): Test`

Ensure a planned assertion count for a test. Will throw `Error` if
`count` is `0`.

## Building

The `tape` package can be built from source into various targets.

### Kotlin Library

`tape.klib`, a Kotlin library that can be linked with `konanc` can be
built from source.

```sh
$ make klib
```

which will produce `build/lib/tape.klib`. The library can be installed
with `klib` by running `make install`

### Static Library

`libtape.a`, a static library that can be linked with `konanc` can be
built from source.

```sh
$ make static
```

which will produce `build/lib/libtape.a` and C header files in
`build/include`. The library can be installed into your system by
running `make install`. The path prefix can be set by defining the
`PREFIX` environment or `make` variable. It defaults to
`PREFIX=/usr/local`

### Shared Library

`libtape.so`, a shared library that can be linked with `konanc` can be
built from source.

```sh
$ make shared
```

which will produce `build/lib/libtape.so` and C header files in
`build/include`. The library can be installed into your system by
running `make install`. The path prefix can be set by defining the
`PREFIX` environment or `make` variable. It defaults to
`PREFIX=/usr/local`

## References

1. TAP - https://en.wikipedia.org/wiki/Test_Anything_Protocol
2. Node.js - https://nodejs.org/en/
3. NPM - https://www.npmjs.com/
4. IMS - https://github.com/mafintosh/ims
5. clib - https://github.com/clibs/clib
6. GHI - https://github.com/stephenmathieson/ghi
