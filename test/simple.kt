package test

import test.OK
import tape.test

fun simple(ok: OK): Boolean {
  test("simple", fun(_) {
    ok(true, "'test(name, cb)' callback function was called")
  })

  return true
}
