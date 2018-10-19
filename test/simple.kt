package test

import test.OK
import ktape.test

fun simple(ok: OK): Boolean {
  test("simple", fun(_) {
    ok(true, "'test(name, cb)' callback function was called")
  })

  return true
}
