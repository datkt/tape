package test.util

fun assert(value: Boolean?) {
  return assert(value, "")
}

fun assert(value: Boolean?, message: String?) {
  if (true != value) {
    throw Throwable("AssertionError: ${message}")
  }
}
