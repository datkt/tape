package tape

import tape.Stream
import tape.AssertionResult

/**
 * The 'Test' class represents a named test
 */
class Test {
  private var callback: Callback?
  private val stream: Stream
  private val ctx: Context

  // runtime hooks
  private var onBeforeRunCallbacks: Array<(Test) -> Unit?> = emptyArray()
  private var onAfterRunCallbacks: Array<(Test) -> Unit?> = emptyArray()
  private var onResultCallbacks: Array<(Test, Any?) -> Unit?> = emptyArray()
  private var onPlanCallbacks: Array<(Test) -> Unit?> = emptyArray()
  private var onTestCallbacks: Array<(Test) -> Unit?> = emptyArray()
  private var onEndCallbacks: Array<(Test) -> Unit?> = emptyArray()

  public var planned: Int? = null
  public var ending: Boolean = false
  public var ended: Boolean = false

  /**
   * 'Test' class constructor.
   */
  constructor(
    name: String?,
    skip: Boolean,
    callback: Callback?,
    stream: Stream? = null
  ) {
    this.ctx = Context(name, skip)
    this.stream = if (null != stream) stream else Stream()
    this.callback = callback
  }

  fun onBeforeRun(callback: (Test) -> Unit?) {
    this.onBeforeRunCallbacks += callback
  }

  fun onAfterRun(callback: (Test) -> Unit?) {
    this.onAfterRunCallbacks += callback
  }

  /**
   * Runs the test runner invoking the runner callback
   * given to the constuctor.
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
    return assert(this.ctx, ok, opts)
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
      skip = opts?.skip,
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
  fun notok(
    ok: Any?,
    msg: String? = null,
    opts: AssertionOptions? = null
  ): AssertionResult {
    return this.assert(ok, AssertionOptions(
      expected = false.toString(),
      actual = ok.toString(),
      error = opts?.error,
      skip = opts?.skip,
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
    return this.assert(null != err, AssertionOptions(
      actual = err?.message,
      skip = opts?.skip,
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
      skip = opts?.skip,
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
    return this.assert(false, AssertionOptions(
      expected = opts?.expected,
      message = msg,
      actual = opts?.actual,
      skip = opts?.skip,
      op = PASS_OPERATOR
    ))
  }

  /**
   * Creates a skiping assertion with an optional message.
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
}
