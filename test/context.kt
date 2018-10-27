package datkt.tape.test

import datkt.tape.test.OK

import datkt.tape.Context
import datkt.tape.UNNAMED_TEST

fun context(ok: OK): Boolean {
  // defaults
  val defaultContext = Context()
  ok(UNNAMED_TEST == defaultContext.name, "UNNAMED_TEST == defaultContext.name")
  ok(false == defaultContext.skip, "false == defaultContext.skip")
  ok(0 == defaultContext.asserts, "0 == defaultContext.asserts")

  // argument order
  val configuredContext = Context("name", true, 123)
  ok("name" == configuredContext.name, "\"name\" == configuredContext.name")
  ok(true == configuredContext.skip, "true == configuredContext.skip")
  ok(123 == configuredContext.asserts, "123 == configuredContext.asserts")

  return true
}
