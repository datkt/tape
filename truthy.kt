package ktape

fun truthy(value: Any? = null): Boolean {
  if (null == value) {
    return false
  }

  if (value is String && 0 == value.length) {
      return false
  }

  if (value is Int && 0 == value) {
    return false
  }

  if (value is Array<*>) {
    if (0 == value.size) {
      return false
    } else {
      for (k: Any? in value) {
        if (false == truthy(k)) {
          return false
        }
      }
    }
  }

  return true
}
