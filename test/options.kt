package test

import test.OK
import ktape.AssertionOptions
import ktape.UNNAMED_OPERATOR
import ktape.UNNAMED_ASSERT

fun options(ok: OK): Boolean {
  val defaultOptions = AssertionOptions()

  ok(
    UNNAMED_OPERATOR == defaultOptions.op,
    "UNNAMED_OPERATOR == defaultOptions.op"
  )

  ok(
    UNNAMED_ASSERT == defaultOptions.message,
    "UNNAMED_ASSERT == defaultOptions.message"
  )

  ok(false == defaultOptions.skip, "false == defaultOptions.skip")
  ok(null == defaultOptions.error, "null == defaultOptions.error")
  ok(null == defaultOptions.actual, "null == defaultOptions.actual")
  ok(null == defaultOptions.expected, "null == defaultOptions.expected")

  return true
}
