package test

fun truthy(ok: (Boolean?, String?) -> Unit?): Boolean {
  ok(ktape.truthy(1), "truthy(1)")
  ok(ktape.truthy(1234), "truthy(1234)")
  ok(ktape.truthy("hello"), "truthy(\"hello\")")
  ok(ktape.truthy(::truthy), "truthy(::truthy)")
  ok(ktape.truthy(fun () {}), "truthy(fun () {}")
  return true
}
