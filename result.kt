package tape

/**
 * A data class for an assertion result.
 */
data class AssertionResult(
  val id: Int,
  val ok: Boolean,
  var op: String? = UNNAMED_OPERATOR,
  var skip: Boolean? = false,
  var name: String? = UNNAMED_ASSERT,
  var error: Any? = null
)
