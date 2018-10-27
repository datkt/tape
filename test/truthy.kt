package datkt.tape.test

val emptyStrings: Array<String> = emptyArray()

fun truthy(ok: (Boolean?, String?) -> Unit?): Boolean {
  ok(datkt.tape.truthy(1), "truthy(1)")
  ok(datkt.tape.truthy(0.1), "truthy(0.1)")
  ok(datkt.tape.truthy(1234), "truthy(1234)")
  ok(datkt.tape.truthy("hello"), "truthy(\"hello\")")
  ok(datkt.tape.truthy(::truthy), "truthy(::truthy)")
  ok(datkt.tape.truthy(fun () {}), "truthy(fun () {}")

  ok(false == datkt.tape.truthy(0), "false == truthy(0)")
  ok(false == datkt.tape.truthy(0.0), "false == truthy(0.0)")
  ok(false == datkt.tape.truthy(""), "false == truthy(\"\")")
  ok(false == datkt.tape.truthy(null), "false == truthy(null)")
  ok(false == datkt.tape.truthy(emptyStrings), "false == truthy(emptyArray())")

  return true
}
