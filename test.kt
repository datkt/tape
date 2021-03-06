package datkt.tape

import kotlin.reflect.KClass

import datkt.tape.truthy
import datkt.tape.Context
import datkt.tape.Callback
import datkt.tape.AssertionResult

import datkt.tape.UNNAMED_TEST

import datkt.tape.SHOULD_THROW
import datkt.tape.SHOULD_BE_EQUAL
import datkt.tape.SHOULD_BE_FALSY
import datkt.tape.SHOULD_BE_TRUTHY

import datkt.tape.OK_OPERATOR
import datkt.tape.FAIL_OPERATOR
import datkt.tape.PASS_OPERATOR
import datkt.tape.SKIP_OPERATOR
import datkt.tape.EQUAL_OPERATOR
import datkt.tape.THROWS_OPERATOR
import datkt.tape.NOT_EQUAL_OPERATOR

/**
 * The `Test` class represents a named test that is invoked in a
 * function callback. When a test is running, it will call various
 * function hooks and write TAP formatted output to a stream,
 * defaulting to stdout * (`println`).
 */
open class Test {
  private var callback: Callback?
  private val ctx: Context

  // runtime hooks
  private var onBeforeRunCallbacks: Array<(Test) -> Any?> = emptyArray()
  private var onAfterRunCallbacks: Array<(Test) -> Any?> = emptyArray()
  private var onResultCallbacks: Array<(Test, Any?) -> Any?> = emptyArray()
  private var onPlanCallbacks: Array<(Test, Int?) -> Any?> = emptyArray()
  private var onEndCallbacks: Array<(Test) -> Any?> = emptyArray()

  public var planned: Int? = null
  public var ending: Boolean = false
  public var ended: Boolean = false

  public val asserts: Int get() = this.ctx.asserts
  public val name: String? get() = this.ctx.name
  public val skip: Boolean get() = this.ctx.skip

  /**
   * 'Test' class constructor.
   */
  constructor(
    name: String? = UNNAMED_TEST,
    skip: Boolean? = false,
    callback: Callback? = noop
  ) {
    this.ctx = Context(name, truthy(skip))
    this.callback = callback
  }

  /**
   * Add a callback that will be invoked before the test
   * is ran.
   */
  fun onBeforeRun(callback: (Test) -> Any?) {
    this.onBeforeRunCallbacks += callback
  }

  /**
   * Add a callback that will be invoked after the test
   * is ran.
   */
  fun onAfterRun(callback: (Test) -> Any?) {
    this.onAfterRunCallbacks += callback
  }

  /**
   * Add a callback that will be invoked when there
   * is a test result. It could be a `String` or `AssertionResult`.
   */
  fun onResult(callback: (Test, Any?) -> Any?) {
    this.onResultCallbacks += callback
  }

  /**
   * Add a callback that will be invoked when a plan
   * has been set.
   */
  fun onPlan(callback: (Test, Int?) -> Any?) {
    this.onPlanCallbacks += callback
  }

  /**
   * Add a callback that will be invoked when the test
   * has ended.
   */
  fun onEnd(callback: (Test) -> Any?) {
    this.onEndCallbacks += callback
  }

  /**
   * Runs the test runner invoking the runner callback
   * given to the constructor.
   */
  fun run(): Test {
    val callback: Callback? = this.callback

    if (this.ctx.skip) {
      this.comment("skip ${this.ctx.name}")
    }

    if (null == callback || this.ctx.skip) {
      this.end()
      return this
    }

    for (hook in this.onBeforeRunCallbacks) {
      hook(this)
    }

    callback(this)

    for (hook in this.onAfterRunCallbacks) {
      hook(this)
    }

    return this
  }

  /**
   * Ensure a planned assertion count for a test.
   */
  fun plan(count: Int): Test {
    if (0 == count) {
      throw Error("Plan count cannot be 0.")
    }

    this.planned = count

    for (hook in this.onPlanCallbacks) {
      hook(this, this.planned)
    }

    return this
  }

  /**
   * Emit a comment
   */
  fun comment(comment: String): Test {
    val lines = comment.split("\n").toTypedArray()
    for (line in lines) {
      for (hook in this.onResultCallbacks) {
        hook(this, line)
      }
    }
    return this
  }

  /**
   * Ends the test runner with an optional error that
   * will generate an error assertion.
   */
  fun end(err: Error? = null) {
    if (null != err) {
      this.error(err)
    }

    if (this.ended) {
      this.fail("Test runner has already ended")
    }

    if (!this.ended) {
      if (this.ending) {
        this.fail(".end() called twice")
      } else {
        this.ending = true
      }
    }

    if (!this.ended && this.ending) {
      val planned = this.planned
      val asserts = this.ctx.asserts
      val pendingAsserts = if (null == planned) 1 else planned - asserts

      if (null != planned && planned > 0 && pendingAsserts > 0) {
        this.fail("plan != count", AssertionOptions(
          actual = asserts.toString(),
          expected = planned.toString()
        ))
      } else {
        for (hook in this.onEndCallbacks) {
          hook(this)
        }
      }

      this.ending = false
      this.ended = true
    }
  }

  /**
   * Asserts that input is truthy based on some optional assertion
   * options.
   */
  fun assert(ok: Any?, opts: AssertionOptions? = null): AssertionResult {
    val result = assert(this.ctx, ok, opts)

    for (hook in this.onResultCallbacks) {
      hook(this, result)
    }

    return result
  }

  /**
   * Asserts that input is "ok" based on some optional assertion
   * options.
   */
  fun ok(
    ok: Any?,
    msg: String? = null,
    opts: AssertionOptions? = null
  ): AssertionResult {
    return this.assert(ok, AssertionOptions(
      expected = true.toString(),
      actual = ok.toString(),
      error = opts?.error,
      skip = truthy(opts?.skip),
      op = OK_OPERATOR,
      message =
        if (truthy(msg)) msg
        else if (truthy(opts) && truthy(opts?.message)) opts?.message
        else SHOULD_BE_TRUTHY
    ))
  }

  /**
   * Asserts that input is "not ok" based on some optional assertion
   * options.
   */
  fun notOk(
    ok: Any?,
    msg: String? = null,
    opts: AssertionOptions? = null
  ): AssertionResult {
    return this.assert(ok, AssertionOptions(
      expected = false.toString(),
      actual = ok.toString(),
      error = opts?.error,
      skip = truthy(opts?.skip),
      op = NOT_OK_OPERATOR,
      message =
        if (truthy(msg)) msg
        else if (truthy(opts) && truthy(opts?.message)) opts?.message
        else SHOULD_BE_FALSY
    ))
  }

  /**
   * Asserts that an error is falsy. If an error is given
   * the message is used in the assertion.
   */
  fun error(
    err: Any? = null,
    msg: String? = null,
    opts: AssertionOptions? = null
  ): AssertionResult {
    return this.assert(!truthy(err), AssertionOptions(
      actual = if (err is Error) err.message else null,
      skip = truthy(opts?.skip),
      op = ERROR_OPERATOR,
      message =
        if (null != msg) msg
        else if (err is Error) err.message
        else null
    ))
  }

  /**
   * Creates a failing assertion with an optional message.
   */
  fun fail(
    msg: String? = null,
    opts: AssertionOptions? = null
  ) : AssertionResult {
    return this.assert(false, AssertionOptions(
      expected = opts?.expected,
      message = msg,
      actual = opts?.actual,
      skip = truthy(opts?.skip),
      op = FAIL_OPERATOR
    ))
  }

  /**
   * Creates a passing assertion with an optional message.
   */
  fun pass(
    msg: String? = null,
    opts: AssertionOptions? = null
  ) : AssertionResult {
    return this.assert(true, AssertionOptions(
      expected = opts?.expected,
      message = msg,
      actual = opts?.actual,
      skip = truthy(opts?.skip),
      op = PASS_OPERATOR
    ))
  }

  /**
   * Creates a skipping assertion with an optional message.
   */
  fun skip(
    msg: String? = null,
    opts: AssertionOptions? = null
  ) : AssertionResult {
    return this.assert(false, AssertionOptions(
      expected = opts?.expected,
      message = msg,
      actual = opts?.actual,
      skip = true,
      op = SKIP_OPERATOR
    ))
  }

  /**
   * Creates an equality assertion for two values with an optional
   * assertion error message.
   */
  fun equal(
    a: Any? = null,
    b: Any? = null,
    msg: String? = null,
    opts: AssertionOptions? = null
  ): AssertionResult {
    return this.assert(a == b, AssertionOptions(
      expected = b?.toString(),
      message = if (truthy(msg)) msg else SHOULD_BE_EQUAL,
      actual = a?.toString(),
      error = opts?.error,
      op = EQUAL_OPERATOR
    ))
  }

  /**
   * Creates an assertion that checks for an error to be thrown
   * inside of a given function.
   */
  fun throws(
    fn: () -> Any?,
    expected: Any? = null,
    msg: String? = null,
    opts: AssertionOptions? = AssertionOptions()
  ): AssertionResult {
    data class Caught(var error: Throwable? = null)
    val caught = Caught()

    if (expected is String) {
      opts?.message = expected
      opts?.expected = null
    } else {
      opts?.message = msg
      opts?.expected = expected
    }

    try {
      fn()
    } catch (err: Throwable) {
      caught.error = err
    }

    var passed = truthy(caught.error)

    if (expected is String) {
      if (passed) {
        passed = expected == caught.error?.message
      }
    } else if (expected is Regex) {
      if (passed) {
        passed = expected.matches(caught.error.toString())
      }

      opts?.expected = expected.toString()
    } else if (null != expected) {
      if (passed) {
        passed = (expected as KClass<*>).isInstance(caught.error)
      }
    }

    return this.assert(passed, AssertionOptions(
      expected = opts?.expected,
      message = if (truthy(msg)) msg else SHOULD_THROW,
      actual = if (truthy(caught.error)) caught.error.toString() else null,
      error = caught.error,
      op = THROWS_OPERATOR
    ))
  }
}
