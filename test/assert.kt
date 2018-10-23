package test

import test.OK
import tape.Context
import tape.AssertionResult
import tape.AssertionOptions

fun assert(ok: OK): Boolean {
  val ctx = Context()

  ok(0 == ctx.asserts, "0 == ctx.asserts")

  val result = tape.assert(
    ctx,
    0,
    AssertionOptions(
      op = "test",
      error = "error",
      message = "oops"
  ))

  ok(1 == ctx.asserts, "ctx.asserts incremented after assertion")
  ok("error" == result.error, "result error set to assertion error")
  ok("oops" == result.name, "result message set to assertion name")
  ok("test" == result.op, "result op set to assertion op")

  ok(
    "oops" == tape.assert(ctx, 0, AssertionOptions(message = "oops")).error,
    "result error set to result assertion message"
  )

  return true
}
