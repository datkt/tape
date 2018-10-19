package test

import test.OK
import ktape.Context
import ktape.AssertionResult
import ktape.AssertionOptions

fun assert(ok: OK): Boolean {
  val ctx = Context()
  ok(
    ktape.assert(ctx, true) is AssertionResult,
    "assert(ctx, true) returns AssertionResult"
  )

  ok(1 == ctx.asserts, "ctx.asserts incremented after assertion")

  val result = ktape.assert(
    ctx,
    false,
    AssertionOptions(
      op = "test",
      error = "error",
      message = "oops"
  ))

  ok(2 == ctx.asserts, "ctx.asserts incremented after assertion")
  ok("error" == result.error, "result error set to assertion error")
  ok("oops" == result.name, "result message set to assertion name")
  ok("test" == result.op, "result op set to assertion op")

  ok(
    "oops" == ktape.assert(ctx, 0, AssertionOptions(message = "oops")).error,
    "result error set to result assertion message"
  )

  ok(3 == ctx.asserts, "ctx.asserts incremented after assertion")

  return true
}
