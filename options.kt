package tape

/**
 * A data class for assertion options
 */
data class AssertionOptions(
  var op: String? = UNNAMED_OPERATOR,
  var skip: Boolean? = false,
  var error: Any? = null,
  var actual: String? = null,
  var message: String? = UNNAMED_ASSERT,
  var expected: String? = null
)
