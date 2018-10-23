package tape

/**
 * A type alias for a print callback. This type alias
 * describes a function signature equivalent to `println(Any?)`
 * built in to Kotlin
 */
typealias PrintCallback = (Any?) -> Unit?

/**
 * The Stream class represents a wrapper around
 * an "endable" print stream.
 */
class Stream {
  var print: PrintCallback = ::println
  var ended: Boolean

  /**
   * Stream class constructor.
   */
  constructor(print: PrintCallback? = null) {
    this.ended = false
    if (null != print) {
      this.print = ::print
    }
  }

  /**
   * Write output to the stream, if not ended.
   * This method will return `true` upon a succesful
   * print such that the stream has not ended, otherwise
   * it will return `false`.
   */
  fun write(output: Any? = null): Boolean {
    if (true != this.ended) {
      this.print(output)
      return true
    }

    return false
  }

  /**
   * End the stream if not already "ended". This method
   * will return `true` if the stream ends for the first
   * time, otherwise false. If output is given as an argument,
   * it will write the output prior to ending the stream.
   */
  fun end(output: Any? = null): Boolean {
    if (false == this.ended) {
      if (null != output) {
        if (this.write(output)) {
          this.ended = true
          return true
        }
      }
    }

    return false
  }
}
