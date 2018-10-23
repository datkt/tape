package tape

/**
 * Assert that a value is "ok" with options.
 */
fun assert(
  ctx: Context, value: Any?,
  opts: AssertionOptions? = null
): AssertionResult {
  val ok = truthy(value)
  val op = opts?.op
  val skip = opts?.skip
  val error = opts?.error
  var result = AssertionResult(ctx.asserts++, ok)
  val message = opts?.message

  if (!ok) {
    result.error = if (truthy(error)) error else message
  }

  result.name = if (truthy(message)) message else result.name
  result.skip = skip
  result.op = if (truthy(op)) op else result.op

  return result
}
