package test

import test.OK
import tape.skip
import tape.test

fun simple(ok: OK): Boolean {
  var calls = 0

  test("simple", fun(_) {
    calls ++
    ok(true, "'test(name, cb)' callback function was called")
  })

  skip("skipped", fun(_) {
    calls ++
  })

  ok(1 == calls, "skipped test never called")

  return true
}
