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

KCCFLAGS = -library $(LIB) -produce program -output $(TEST)

.PHONY: default
default: test test/c

.PHONY: test $(TESTS)
test: $(TEST)

.PHONY: test/c
test/c:
	$(MAKE) -C c/

$(TEST): $(TESTS)
ifndef NPM
	$(KCC) $(KCCFLAGS) $^
	./$@
else
	@$(KCC) $(KCCFLAGS) $^
endif

.PHONY: clean
clean:
	$(RM) $(TEST) $(CLASSES)
	$(MAKE) $@ -C c/
