package tape

/**
 * A type alias for a writer callback. This type alias
 * describes a function signature equivalent to `println(Any?)`
 * built in to Kotlin
 */
typealias WriteCallback = (Any?) -> Unit?

/**
 * NO-OP writer stream writer function
 */
private fun noop(string: Any? = null) = string as Unit

/**
 * The Stream class represents a wrapper around
 * an "endable" writer stream.
 */
class Stream {
  var writer: WriteCallback = ::noop
  var ended: Boolean

  /**
   * Stream class constructor.
   */
  constructor(writer: WriteCallback? = null) {
    this.ended = false
    if (null != writer) {
      this.writer = writer
    }
  }

  /**
   * Write output to the stream, if not ended.
   * This method will return `true` upon a succesful
   * writer such that the stream has not ended, otherwise
   * it will return `false`. If `null` is given, it will
   * "end" the stream.
   */
  fun write(output: Any?): Boolean {
    if (null == output) {
      return this.end()
    }

    if (true != this.ended) {
      this.writer(output)
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
