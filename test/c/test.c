#include <ok/ok.h>
#include <stdio.h>
#include <tape.h>

void
test_hello(const TapeTest *t) {
  // @TODO
  printf("test_hello()\n");
  (void) t;
}

int
main(void) {
  tape_test("hello", test_hello);
  return 0;
}
