package test

import test.OK
import tape.Context
import tape.AssertionResult
import tape.AssertionOptions
import tape.UNNAMED_ASSERT
import tape.UNNAMED_OPERATOR

fun assert(ok: OK): Boolean {
  val ctx = Context()

  ok(0 == ctx.asserts, "0 == ctx.asserts")

  var result = tape.assert(ctx, 0, AssertionOptions(
    op = "test",
    error = "error",
    message = "oops"
  ))

  ok(1 == ctx.asserts, "ctx.asserts incremented after assertion")
  ok(0 == result.id, "result.id is set")
  ok("error" == result.error, "result error set to assertion error")
  ok("oops" == result.name, "result name set to assertion message")
  ok("test" == result.op, "result op set to assertion op")

  result = tape.assert(ctx, 0, AssertionOptions(message = "oops"))

  ok(2 == ctx.asserts, "ctx.asserts incremented after assertion")
  ok(1 == result.id, "result.id is set")
  ok("oops" == result.error, "result error set to result assertion message")
  ok("oops" == result.name, "result name set to result assertion message")
  ok(UNNAMED_OPERATOR == result.op, "result op set to result UNNAMED_OPERATOR")

  result = tape.assert(ctx, true)

  ok(3 == ctx.asserts, "ctx.asserts incremented after assertion")
  ok(2 == result.id, "result.id is set")
  ok(null == result.error, "result error set to result assertion message")
  ok(UNNAMED_ASSERT == result.name, "result name set to result UNNAMED_ASSERT")
  ok(UNNAMED_OPERATOR == result.op, "result op set to result UNNAMED_OPERATOR")

  result = tape.assert(ctx, "", AssertionOptions(
    error = Error("ERROR")
  ))

  ok(4 == ctx.asserts, "ctx.asserts incremented after assertion")
  ok(3 == result.id, "result.id is set")
  ok("ERROR" == (result.error as Error).message, "result error set to Error instance")
  ok(UNNAMED_ASSERT == result.name, "result name set to result UNNAMED_ASSERT")
  ok(UNNAMED_OPERATOR == result.op, "result op set to result UNNAMED_OPERATOR")

  return true
}
