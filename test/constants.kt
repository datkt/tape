package test

import test.OK

import ktape.UNNAMED_TEST
import ktape.UNNAMED_ASSERT
import ktape.UNNAMED_OPERATOR
import ktape.OK_OPERATOR
import ktape.NOT_OK_OPERATOR
import ktape.FAIL_OPERATOR
import ktape.PASS_OPERATOR
import ktape.SKIP_OPERATOR
import ktape.ERROR_OPERATOR
import ktape.EQUAL_OPERATOR
import ktape.SHOULD_BE_TRUTHY
import ktape.SHOULD_BE_FALSY
import ktape.SHOULD_BE_EQUAL

fun constants(ok: OK): Boolean {
  ok(
    UNNAMED_TEST == "(anonymous test)",
    "UNNAMED_TEST == \"(anonymous test)\""
  )

  ok(
    UNNAMED_ASSERT == "(unnamed assert)",
    "UNNAMED_ASSERT == \"(unnamed assert)\""
  )

  ok(
    UNNAMED_OPERATOR == "(unnamed operator)",
    "UNNAMED_OPERATOR == \"(unnamed operator)\""
  )

  ok(
    OK_OPERATOR == "ok",
    "OK_OPERATOR == \"ok\""
  )

  ok(
    NOT_OK_OPERATOR == "not ok",
    "NOT_OK_OPERATOR == \"not ok\""
  )

  ok(
    FAIL_OPERATOR == "fail",
    "FAIL_OPERATOR == \"fail\""
  )

  ok(
    PASS_OPERATOR == "pass",
    "PASS_OPERATOR == \"pass\""
  )

  ok(
    SKIP_OPERATOR == "skip",
    "SKIP_OPERATOR == \"skip\""
  )

  ok(
    ERROR_OPERATOR == "error",
    "ERROR_OPERATOR == \"error\""
  )

  ok(
    EQUAL_OPERATOR == "equal",
    "EQUAL_OPERATOR == \"equal\""
  )

  ok(
    SHOULD_BE_TRUTHY == "should be truthy",
    "SHOULD_BE_TRUTHY == \"should be truthy\""
  )

  ok(
    SHOULD_BE_FALSY == "should be falsy",
    "SHOULD_BE_FALSY == \"should be falsy\""
  )

  ok(
    SHOULD_BE_EQUAL == "should be equal",
    "SHOULD_BE_EQUAL == \"should be equal\""
  )

  return true
}
