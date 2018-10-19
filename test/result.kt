package test

import test.OK
import ktape.AssertionResult
import ktape.UNNAMED_ASSERT
import ktape.UNNAMED_OPERATOR

fun result(ok: OK): Boolean {
  val defaultResult = AssertionResult(123, true)
  ok(123 == defaultResult.id, "sets AssertionResult.id")
  ok(true == defaultResult.ok, "sets AssertionResult.ok")

  ok(
    UNNAMED_OPERATOR == defaultResult.op,
    "UNNAMED_OPERATOR == defaultResult.op"
  )

  ok(
    UNNAMED_ASSERT == defaultResult.name,
    "UNNAMED_ASSERT == defaultResult.name"
  )

  ok(null == defaultResult.error, "null == defaultResult.error")
  return true
}
