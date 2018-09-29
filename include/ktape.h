#ifndef KTAPE_H
#define KTAPE_H
#ifdef __cplusplus
extern "C" {
#endif

#include <ktape_api.h>
#include <string.h>

#ifndef KTAPE_UNNAMED_TEST
#define KTAPE_UNNAMED_TEST "(anonymous test)"
#endif

#ifndef KTAPE_UNNAMED_ASSERT
#define KTAPE_UNNAMED_ASSERT "(unnamed assert)"
#endif

#ifndef KTAPE_UNNAMED_OPERATOR
#define KTAPE_UNNAMED_OPERATOR "(unnamed operator)"
#endif

#ifndef MAX_KTAPE_TEST_DEPTH
#define MAX_KTAPE_TEST_DEPTH 32
#endif

#ifndef kotlin
#define kotlin ktape_symbols()->kotlin.root
#endif

#define KTAPE_KREF(T) ktape_kref_ ## T

#define KTAPE_CLASS(T) KTAPE_KREF(ktape_##T)

typedef struct KTapeTest KTapeTest;
typedef struct KTapeContext KTapeContext;
typedef void (*KTapeCallback)(const KTapeTest *t);

struct KTapeContext {
  KTAPE_CLASS(Context) src;

  const char *name;
  unsigned int skip:1;
  unsigned int asserts;
  unsigned int pending;
};

struct KTapeTest {
  KTAPE_CLASS(Test) src;

  KTapeContext *ctx;
  KTapeCallback callback;
};

struct KTapeAssertionOptions {
  KTAPE_CLASS(AssertionOptions) src;

  const char *op;
  unsigned int skip:1;
  const char *error;
  const char *actual;
  const char *message;
  const char *expected;
};

KTapeTest KTAPE_TESTS[MAX_KTAPE_TEST_DEPTH];
unsigned int KTAPE_TEST_INDEX = 0;

KTapeTest
ktape_test(const char *name, KTapeCallback callback) {
  KTAPE_KREF(kotlin_Function1) noop = { (void *) 0 };
  KTAPE_CLASS(Test) src = kotlin.ktape.test(name, noop);

  KTapeTest test = { 0 };

  memcpy(&test.src, &src, sizeof(KTAPE_CLASS(Test)));
  if (0 != callback) {
    callback(&test);
  }

  return test;
}

void
ktape_test_run(KTapeTest *t, KTapeCallback callback) {
}

#ifdef __cplusplus
}
#endif
#endif
