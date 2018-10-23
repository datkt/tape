package tape

import tape.Test

/**
 * The `Results` class represents a container for all
 * observed test results.
 */
class Results {
  var tests: Array<Test> = emptyArray()

  /**
   * Results class constructor.
   */
  constructor() {
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
    return this
  }
}

private fun write() {
}
