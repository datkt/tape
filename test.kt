package ktape

/**
 * The 'Test' class represents a named test
 */
class Test {
  private var ctx: Context
  private var callback: Callback?

  constructor(name: String?, skip: Boolean, callback: Callback?) {
    this.ctx = Context(name, skip)
    this.callback = callback
  }

  fun assert(ok: Any?): AssertionResult {
    return assert(this.ctx, ok)
  }

  fun assert(ok: Any?, opts: AssertionOptions): AssertionResult {
    return assert(this.ctx, ok, opts)
  }

  fun run(): Test {
    val callback: Callback? = this.callback
    if (null != callback && true != this.ctx.skip) {
      callback(this)
    }
    return this
  }
}
