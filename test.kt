package tape

import kotlin.reflect.KClass

import tape.truthy
import tape.Context
import tape.Callback
import tape.AssertionResult

import tape.UNNAMED_TEST

import tape.SHOULD_THROW
import tape.SHOULD_BE_EQUAL
import tape.SHOULD_BE_FALSY
import tape.SHOULD_BE_TRUTHY

import tape.OK_OPERATOR
import tape.FAIL_OPERATOR
import tape.PASS_OPERATOR
import tape.SKIP_OPERATOR
import tape.EQUAL_OPERATOR
import tape.THROWS_OPERATOR
import tape.NOT_EQUAL_OPERATOR

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
  private var onBeforeRunCallbacks: Array<(Test) -> Unit?> = emptyArray()
  private var onAfterRunCallbacks: Array<(Test) -> Unit?> = emptyArray()
  private var onResultCallbacks: Array<(Test, Any?) -> Unit?> = emptyArray()
  private var onPlanCallbacks: Array<(Test) -> Unit?> = emptyArray()
  private var onEndCallbacks: Array<(Test) -> Unit?> = emptyArray()

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
  fun onBeforeRun(callback: (Test) -> Unit?) {
    this.onBeforeRunCallbacks += callback
  }

  /**
   * Add a callback that will be invoked after the test
   * is ran.
   */
  fun onAfterRun(callback: (Test) -> Unit?) {
    this.onAfterRunCallbacks += callback
  }

  /**
   * Add a callback that will be invoked when there
   * is a test result. It could be a `String` or `AssertionResult`.
   */
  fun onResult(callback: (Test, Any?) -> Unit?) {
    this.onResultCallbacks += callback
  }

  /**
   * Add a callback that will be invoked when a plan
   * has been set.
   */
  fun onPlan(callback: (Test) -> Unit?) {
    this.onPlanCallbacks += callback
  }

  /**
   * Add a callback that will be invoked when the test
   * has ended.
   */
  fun onEnd(callback: (Test) -> Unit?) {
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
      return this.end()
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
      hook(this)
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
  fun end(err: Error? = null): Test {
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
      }

      this.ending = false
      this.ended = true
    }

    return this
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
   * Asserts that input is "ok" based on some optional assertion
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
   * Asserts that an error is given with an optional message.
   */
  fun error(
    err: Error? = null,
    msg: String? = null,
    opts: AssertionOptions? = null
  ): AssertionResult {
    return this.assert(!truthy(err), AssertionOptions(
      actual = err?.message,
      skip = truthy(opts?.skip),
      op = ERROR_OPERATOR,
      message =
        if (null != msg) msg
        else if (null != err) err.message
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
      skip = true,
      op = EQUAL_OPERATOR
    ))
  }

  fun throws(
    fn: () -> Unit,
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
