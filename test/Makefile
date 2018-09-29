RM = rm -rf
KCC ?= konanc

LIB ?= ../build/lib/ktape
TEST ?= test.kexe

TESTS += $(wildcard *.kt)
TESTS += $(wildcard **/*.kt)
TESTS += $(wildcard **/**/*.kt)

CLASSES += $(wildcard *.class )
CLASSES += $(wildcard **/*.class)
CLASSES += $(wildcard **/**/*.class)

.PHONY: default
default: test test/c

.PHONY: test $(TESTS)
test: $(TEST)

.PHONY: test/c
test/c:
	$(MAKE) -C c/

$(TEST): $(TESTS)
	$(KCC) -library $(LIB) -produce program -output $(TEST) $(SRC) $^
	./$@

.PHONY: clen
clean:
	$(RM) $(TEST) $(CLASSES)
	$(MAKE) $@ -C c/
