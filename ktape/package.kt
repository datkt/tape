package ktape

import ktape.Test
import ktape.Callback

/**
 * Creates and returns a new named scoped test. The test callback
 * will not be invoked if null is given.
 */
fun test(name: String, callback: Callback? = null): Test {
  val t = Test(name, false, callback)
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
