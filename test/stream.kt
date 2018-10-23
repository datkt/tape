package test

import test.OK
import tape.Stream

fun stream(ok: OK): Boolean {
  val st = Stream(::print)
  val msg = "hello"

  ok(false == st.ended, "false == st.ended")
  st.write(msg)

  fun print(string: String) {
    println(string)
  }

  return true
}
