package test

fun simple(): Boolean {
  var called = false

  ktape.test("simple", fun(_) {
    called = true
  })

  return called
}
