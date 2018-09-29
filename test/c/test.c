#include <ktape.h>
#include <ok/ok.h>
#include <stdio.h>

void
test_hello(const KTapeTest *t) {
  // @TODO
  printf("test_hello()\n");
  (void) t;
}

int
main(void) {
  ktape_test("hello", test_hello);
  return 0;
}
