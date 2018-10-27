package datkt.tape

import datkt.tape.Test
import datkt.tape.Results
import datkt.tape.Callback

private var results: Results? = null

/**
 * Explicitly initialize module variables.
 */
fun init() {
  if (null == results) {
    results = Results()
  }
}

/**
 * Explicitly de-initialize module variables.
 */
fun deinit() {
  if (null != results) {
    results = null
  }
}

/**
 * Creates and returns a new named scoped test. The test callback
 * will not be invoked if null is given.
 */
fun test(name: String, callback: Callback? = null): Test {
  val t = Test(name, false, callback)

  // ensure library is initialized
  init()

  results?.push(t)

  if (null != callback) {
    t.run()
  }

  return t
}

/**
 * Creates and returns a new skipped scoped test. The test callback
 * will not be invoked if null is given.
 */
fun skip(name: String, callback: Callback? = null): Test {
  val t = Test(name, true, callback)
  if (null != callback) {
    t.run()
  }
  return t
}

/**
 * Closes results container and writes results to underlying
 * write stream.
 */
fun collect(): Results? {
  results?.collect()
  results?.close()
  return results
}
