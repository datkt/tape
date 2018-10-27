#ifndef TAPE_H
#define TAPE_H
#ifdef __cplusplus
extern "C" {
#endif

#include <tape_api.h>
#include <string.h>

#ifndef TAPE_UNNAMED_TEST
#define TAPE_UNNAMED_TEST "(anonymous test)"
#endif

#ifndef TAPE_UNNAMED_ASSERT
#define TAPE_UNNAMED_ASSERT "(unnamed assert)"
#endif

#ifndef TAPE_UNNAMED_OPERATOR
#define TAPE_UNNAMED_OPERATOR "(unnamed operator)"
#endif

#ifndef MAX_TAPE_TEST_DEPTH
#define MAX_TAPE_TEST_DEPTH 32
#endif

#ifndef kotlin
#define kotlin TAPE_symbols()->kotlin.root
#endif

#define TAPE_KREF(T) tape_kref_datkt_ ## t

#define TAPE_CLASS(T) TAPE_KREF(tape_##T)

typedef struct TapeTest TapeTest;
typedef struct TapeContext TapeContext;
typedef void (*TapeCallback)(const TapeTest *t);

struct TapeContext {
  TAPE_CLASS(Context) src;

  const char *name;
  unsigned int skip:1;
  unsigned int asserts;
  unsigned int pending;
};

struct TapeTest {
  TAPE_CLASS(Test) src;

  TapeContext *ctx;
  TapeCallback callback;
};

struct tapeAssertionOptions {
  TAPE_CLASS(AssertionOptions) src;

  const char *op;
  unsigned int skip:1;
  const char *error;
  const char *actual;
  const char *message;
  const char *expected;
};

TapeTest TAPE_TESTS[MAX_TAPE_TEST_DEPTH];
unsigned int TAPE_TEST_INDEX = 0;

TapeTest
tape_test(const char *name, TapeCallback callback) {
  TAPE_KREF(kotlin_Function1) noop = { (void *) 0 };
  TAPE_CLASS(Test) src = kotlin.tape.test(name, noop);

  TapeTest test = { 0 };

  memcpy(&test.src, &src, sizeof(TAPE_CLASS(Test)));
  if (0 != callback) {
    callback(&test);
  }

  return test;
}

void
tape_test_run(TapeTest *t, TapeCallback callback) {
}

#ifdef __cplusplus
}
#endif
#endif
