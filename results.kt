package datkt.tape

import datkt.tape.Test
import datkt.tape.Stream

/**
 * The `Results` class represents a container for all
 * observed test results.
 */
class Results {
  var closed: Boolean = false
  val stream: Stream
  var tests: Array<Test> = emptyArray()
  var count: Int = 0
  var fail: Int = 0
  var pass: Int = 0


  /**
   * Results class constructor.
   */
  constructor(stream: Stream? = null) {
    this.stream = if (null != stream) stream else Stream(::print)
  }

  /**
   * Adds a test to watch for results
   */
  fun push(t: Test): Results {
    this.tests += t
    this.watch(t)
    return this
  }

  /**
   * Watch a test for results.
   */
  fun watch(t: Test): Results {
    if (this.closed) {
      return this
    }

    val stream = this.stream

    t.onBeforeRun(fun(_) {
      if (this.closed) {
        return
      }

      stream.write("# ${t.name}\n")
    })

    t.onResult(fun(_, result: Any?) {
      if (this.closed) {
        return
      }

      if (result is String) {
        stream.write("# ${result}\n")
        return
      }

      this.count++

      if (result is AssertionResult) {
        stream.write(encode(result, this.count))
        if (result.ok) {
          this.pass++
        } else {
          this.fail++
        }
      }
    })

    return this
  }

  /**
   * Collect results and write them to underlying
   * write stream.
   */
  fun collect(): Results {
    if (this.closed || stream.ended) {
      return this
    }

    stream.write("\n")
    stream.write("1..${this.count}\n")
    stream.write("# tests ${this.count}\n")
    stream.write("# pass  ${this.pass}\n")

    if (this.fail > 0) {
      stream.write("# fail  ${this.fail}\n")
    } else {
      stream.write("\n")
      stream.write("# ok\n")
    }

    stream.end()
    return this
  }

  /**
   * Closes the results
   */
  fun close(): Results {
    if (this.closed) {
      return this
    }

    this.closed = true
    return this
  }
}

/**
 * Encodes an AssertionResult into a TAP result with YAML details
 * showing expected and actual values, if given.
 */
fun encode(result: AssertionResult, count: Int): String {
  var output: Array<String> = emptyArray()
  var inner = "   "
  var outer = " "

  fun push(s: Any?) { output += "${s?.toString()}" }
  fun join() = output.joinToString(" ")

  if (result.ok) {
    push("ok")
  } else {
    push("not ok")
  }

  push(count)

  if (truthy(result.name)) {
    push(result.name?.replace("""\s+""", " "))
  }

  if (result.skip) {
    push("# SKIP")
  }

  push("\n")

  if (result.ok) {
    return join()
  }

  push("${outer}---\n")
  push("${inner}operator: ${result.op}\n")

  if (null != result.expected || null != result.actual) {
    push("${inner}expected: ${result.expected}\n")
    push("${inner}actual:   ${result.actual}\n")
  }

  push("${outer}...\n")
  return join()
}
