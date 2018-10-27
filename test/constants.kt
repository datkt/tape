package datkt.tape.test

import datkt.tape.test.OK

import datkt.tape.UNNAMED_TEST
import datkt.tape.UNNAMED_ASSERT
import datkt.tape.UNNAMED_OPERATOR
import datkt.tape.OK_OPERATOR
import datkt.tape.NOT_OK_OPERATOR
import datkt.tape.FAIL_OPERATOR
import datkt.tape.PASS_OPERATOR
import datkt.tape.SKIP_OPERATOR
import datkt.tape.ERROR_OPERATOR
import datkt.tape.EQUAL_OPERATOR
import datkt.tape.SHOULD_BE_TRUTHY
import datkt.tape.SHOULD_BE_FALSY
import datkt.tape.SHOULD_BE_EQUAL

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
