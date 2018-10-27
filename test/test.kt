package datkt.tape.test

import datkt.tape.test.OK

import datkt.tape.Test
import datkt.tape.UNNAMED_TEST

import datkt.tape.OK_OPERATOR
import datkt.tape.FAIL_OPERATOR
import datkt.tape.PASS_OPERATOR
import datkt.tape.SKIP_OPERATOR
import datkt.tape.EQUAL_OPERATOR
import datkt.tape.ERROR_OPERATOR
import datkt.tape.THROWS_OPERATOR
import datkt.tape.NOT_OK_OPERATOR
import datkt.tape.UNNAMED_OPERATOR
import datkt.tape.NOT_EQUAL_OPERATOR

fun test(ok: OK): Boolean {
  var t: Test
  var i: Int

  // public property defaults
  t = Test()
  ok(null == t.planned, "t.planned is null")
  ok(false == t.ending, "t.ending is set to false")
  ok(false == t.ended, "t.ended is set to false")
  ok(UNNAMED_TEST == t.name, "t.name is set to UNNAMED_TEST")
  ok(false == t.skip, "t.skip is set to false")
  ok(0 == t.asserts, "t.asserts is set to 0")

  t = Test("test", true)
  ok("test" == t.name, "t.name is set to test")
  ok(true == t.skip, "t.skip is set to true")

  t = Test()
  t.plan(123)
  ok(123 == t.planned, "t.planned set to 123")

  i = 0
  t = Test()

  try {
    t.plan(0)
    ok(false, "t.plan(0) should throw error")
  } catch (err: Error) {
    ok(true, "plan throws error when count is 0")
  }

  ok(t.ok("hello").ok, "ok for t.ok()"); i++
  ok(t.assert(123).ok, "ok for t.assert()"); i++
  ok(!t.assert(false).ok, "ok for !t.assert()"); i++
  ok(!t.fail("fail").ok, "ok for !t.fail()"); i++
  ok(t.pass("pass").ok, "ok is ok for t.pass()"); i++
  ok(!t.skip("skip").ok, "ok !t.skip()"); i++
  ok(!t.notOk(false).ok, "ok for !t.notOk()"); i++
  ok(t.equal(1.0, 1.0).ok, "ok for t.equal()"); i++
  ok(!t.error(Error("oops")).ok, "ok for !t.error()"); i++
  ok(t.throws({ throw Error("oops") }).ok, "ok t.throws()"); i++
  ok(i == t.asserts, "t.asserts is correctly updated")

  t = Test()
  ok(
    OK_OPERATOR == t.ok("ok").op,
    "OK_OPERATOR is set for t.ok() op"
  )

  ok(
    UNNAMED_OPERATOR == t.assert(true).op,
    "UNNAMED_OPERATOR is set for  t.assert() op"
  )

  ok(
    FAIL_OPERATOR == t.fail("fail").op,
    "FAIL_OPERATOR is set for t.fail() op"
  )

  ok(
    PASS_OPERATOR == t.pass("pass").op,
    "PASS_OPERATOR is set for t.pass() op"
  )

  ok(
    SKIP_OPERATOR == t.skip("skip").op,
    "SKIP_OPERATOR is set for t.skip() op"
  )

  ok(
    NOT_OK_OPERATOR == t.notOk("notOk").op,
    "NOT_OK_OPERATOR is set for t.notOk() op"
  )

  ok(
    EQUAL_OPERATOR == t.equal("equal").op,
    "EQUAL_OPERATOR is set for t.equal() op"
  )

  ok(
    ERROR_OPERATOR == t.error(Error("error")).op,
    "ERROR_OPERATOR is set for t.error() op"
  )

  ok(
    THROWS_OPERATOR == t.throws({ throw Error("error") }).op,
    "THROWS_OPERATOR is set for t.throw() op"
  )

  var called = false
  t = Test(callback = fun(_) { called = true })
  t.run()
  ok(true == called, "test callback called after t.run()")

  Test(callback = fun (t: Test) {
    t.end()
    ok(true == t.ended, "t.ended is true after t.end()")
  }).run()

  return true
}
