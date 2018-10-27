package datkt.tape

/**
 * A data class that represents a context for assertions.
 */
data class Context(
  val name: String? = UNNAMED_TEST,
  val skip: Boolean = false,
  var asserts: Int = 0
)
