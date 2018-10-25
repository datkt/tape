package test

import test.OK

import tape.UNNAMED_TEST
import tape.UNNAMED_ASSERT
import tape.UNNAMED_OPERATOR
import tape.OK_OPERATOR
import tape.NOT_OK_OPERATOR
import tape.FAIL_OPERATOR
import tape.PASS_OPERATOR
import tape.SKIP_OPERATOR
import tape.ERROR_OPERATOR
import tape.EQUAL_OPERATOR
import tape.SHOULD_BE_TRUTHY
import tape.SHOULD_BE_FALSY
import tape.SHOULD_BE_EQUAL

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
    NOT_OK_OPERATOR == "notOk",
    "NOT_OK_OPERATOR == \"notOk\""
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
