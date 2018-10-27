package datkt.tape.test

import datkt.tape.test.OK
import datkt.tape.Stream

fun stream(ok: OK): Boolean {
  var written = ""
  fun writer(string: Any?) { written = string as String }
  val st = Stream(::writer)

  ok(::writer == st.writer, "::writer == st.writer")
  ok(false == st.ended, "false == st.ended")
  ok(true == st.write("hello"), "true == st.write(hello)")
  ok("hello" == written, "hello == written")
  ok(true == st.end("end"), "true == st.end()")
  ok("end" == written, "end == written")
  ok(false == st.write("hello"), "false == st.write(hello) after end")
  ok(false == st.end(), "false == st.end() after end")

  return true
}
