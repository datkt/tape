package ktape

/**
 * Assert that something is "ok" for a context.
 */
fun assert(ctx: Context, ok: Any?): AssertionResult {
  return assert(ctx, ok, AssertionOptions())
}

/**
 * Assert that something is "ok" with options.
 */
fun assert(ctx: Context, ok: Any?, opts: AssertionOptions): AssertionResult {
  val value = truthy(ok)
  var result = AssertionResult(ctx.asserts++, value)

  result.name = opts.message
  result.skip = opts.skip
  result.op = opts.op

  if (false == value) {
    result.error = if (truthy(opts.error)) opts.error else result.name
  }

  return result
}
