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
import datkt.tape.collect
import datkt.tape.test
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

  collect() // collects results and prints to stdout
}
```

## API

The `datkt.tape` package exports a public API documented in this section.

### `test(name: String, callback: (t: Test) -> Unit?): Test`

Creates and returns a new named scoped test. The test callback
will not be invoked if null is given.

```kotlin
import datkt.tape.test
test("function test", fun(t: Test) {
  t.plan(1)
  t.ok(true, "we're okay")
  t.end()
})
```

### `skip(name: String, callback: Callback?): Test`

Creates and returns a new skipped scoped test. The test callback
will not be invoked if null is given.

```kotlin
import datkt.tape.skip
skip("skipped test", fun(t: Test) {
  t.end(Error("This should never run"))
})
```

### `collect(): Results?`

Closes results container and writes results to underlying
write stream.

```kotlin
import datkt.tape.collect
val results = datkt.tape.collect()
```

### `class Test(name: String?, skip: Boolean, callback: Callback?, stream: Stream?)`

The `Test` class represents a named test that is invoked in a
function callback. When a test is running, it will call various function hooks
and write TAP formatted output to a stream.

```kotlin
t = Test("thing", false, fun(t: Test) {
  t.end
})

t.run()
```

#### `t.onBeforeRun(callback: (Test) -> Unit?)`

Add a callback that will be invoked before the test is ran.

```kotlin
t.onBeforeRun({
  // do something before the tests is ran
})
```

#### `t.onAfterRun(callback: (Test) -> Unit?)`

Add a callback that will be invoked after the test is ran.

```kotlin
t.onAferRun({
  // do something after the tests is ran
})
```

#### `t.onResult(callback: (Test, Any?) -> Unit?)`

Add a callback that will be invoked when there
is a test result. It could be a `String` or `AssertionResult`.

```kotlin
t.onResult(fun(_, result: Any?) {
  if (result is String) {
    // do something with result string
  } else if (result is AssertionResult) {
    // do something with assertion result
  }
})
```

#### `t.onPlan(callback: (Test, Int?) -> Unit?)`

Add a callback that will be invoked when a plan
has been set.

```kotlin
t.onPlan(fun(_, plan: Int?) {
  if (null != plan) {
    // do something with plan
  }
})
```

#### `t.onEnd(callback: (Test) -> Unit?)`

Add a callback that will be invoked when the test
has ended.

```kotlin
t.onEnd({
  // do something when test ends
})
```

#### `t.run(): Test`

Runs the test runner invoking the runner callback
given to the constructor if the test is not skipped.

```kotlin
t.run() // will call test callback, if test is not skipped
```

#### `t.plan(count: Int): Test`

Ensure a planned assertion count for a test. Will throw `Error` if
`count` is `0`.

```kotlin
t.plan(4)
```

#### `t.comment(comment: String): Test`

Emit a comment

```kotlin
t.comment("@TODO(jwerle): Fix this")
```

#### `t.end(err: Error?): Test`

Ends the test runner with an optional error that
will generate an error assertion.

```kotlin
t.end()
```

or with an `Error`

```kotlin
t.end(Error("oops"))
```

#### `t.assert(ok: Any?, opts: AssertionOptions?): AssertionResult`

Asserts that input is truthy based on some optional assertion options.

```kotlin
t.assert(true)
t.assert(1234)
t.assert("okay")
t.assert(::println)
t.assert({ 1 + 1 })
```

#### `ok(ok: Any?, msg: String?, opts: AssertionOptions?): AssertionResult`

Asserts that input is "ok" based on some optional assertion options.

```kotlin
t.ok(null == err)
t.ok(thing is Thing)
t.ok(string.length)
```

#### `notOk(ok: Any?, msg: String?, opts: AssertionOptions?): AssertionResult`

Asserts that input is "not ok" based on some optional assertion options.

```kotlin
t.notOk(err)
t.notOk(thing is That)
t.notOk(array.count)
```

#### `error(err: Error?, msg: String?, opts: AssertionOptions?): AssertionResult`

Asserts that an error is falsy. If an error is given
the message is used in the assertion.

```kotlin
t.error(Error("oops")) // failing assertion
t.error(null) // passing
```

#### `t.fail(msg: String?, opts: AssertionOptions?) : AssertionResult`

Creates a failing assertion with an optional message.

```kotlin
t.fail("well, that didn't work")
```

#### `t.pass( msg: String?, opts: AssertionOptions?) : AssertionResult`

Creates a passing assertion with an optional message.

```kotlin
t.pass("well done!")
```

#### `t.skip(msg: String?, opts: AssertionOptions?) : AssertionResult`

Creates a skipping assertion with an optional message.

```kotlin
t.skip("we'll come back to shit")
```

#### `t.equal(a: Any?, b: Any?, msg: String?, opts: AssertionOptions?): AssertionResult

Creates an equality assertion for two values with an optional
assertion error message.

```kotlin
t.equal("hello", "hello", "hello == hello")
t.equal("good", "food", "good != food")
```

#### `t.throws(fn: () -> Unit, expected: Any?, msg: String?, opts: AssertionOptions?): AssertionResult

Creates an assertion that checks for an error to be thrown
inside of a given function.

```kotlin
t.throws({ throw Error("oops") }) // passes
t.throws({ throw Exception("yikes") }, Error, "Expection != Error")
```

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
