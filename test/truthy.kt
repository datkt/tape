package test

val emptyStrings: Array<String> = emptyArray()

fun truthy(ok: (Boolean?, String?) -> Unit?): Boolean {
  ok(tape.truthy(1), "truthy(1)")
  ok(tape.truthy(0.1), "truthy(0.1)")
  ok(tape.truthy(1234), "truthy(1234)")
  ok(tape.truthy("hello"), "truthy(\"hello\")")
  ok(tape.truthy(::truthy), "truthy(::truthy)")
  ok(tape.truthy(fun () {}), "truthy(fun () {}")

  ok(false == tape.truthy(0), "false == truthy(0)")
  ok(false == tape.truthy(0.0), "false == truthy(0.0)")
  ok(false == tape.truthy(""), "false == truthy(\"\")")
  ok(false == tape.truthy(null), "false == truthy(null)")
  ok(false == tape.truthy(emptyStrings), "false == truthy(emptyArray())")

  return true
}
