import test.assert
import test.constants
import test.context
import test.options
import test.result
import test.results
import test.simple
import test.truthy

const val TAP_VERSION = 13
const val TAP_HEADER = "TAP version ${TAP_VERSION}"

val tests = listOf(
  ::assert,
  ::constants,
  ::context,
  ::options,
  ::result,
  ::results,
  ::simple,
  ::truthy
)

var asserts = 0
var passed = 0
var failed = 0
var skips = 0

fun main(args: Array<String>) {
  val count = tests.count()

  println("")
  println(TAP_HEADER)

  for (i in 1..count) {
    val test = tests[i-1]
    println("# ${test.name}")

    fun ok(value: Boolean? = null, comment: String? = null) {
      if (null == value) {
        return
      }

      asserts++

      var postfix = ""
      var line: String

      if (null != comment && comment.length > 0) {
        postfix = "# ${comment}"
      }

      if (true == value) {
        line = "ok ${asserts}"
        passed++
      } else {
        line = "not ok ${asserts}"
        failed++
      }

      println(trim("${line} ${postfix}"))
    }

    if (false == test(::ok)) {
      skips++
      println("# skip ${asserts + skips}")
    }
  }

  println("")
  println("1..${passed + failed}")
  println("# tests ${asserts}")
  println("# skips ${skips}")
  println("# pass ${passed}")
  println("# fail ${failed}")
  println("")
}

fun trim(s: String): String {
  return s.trim()
}
