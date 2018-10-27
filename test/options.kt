package datkt.tape.test

import datkt.tape.test.OK
import datkt.tape.AssertionOptions
import datkt.tape.UNNAMED_OPERATOR
import datkt.tape.UNNAMED_ASSERT

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
