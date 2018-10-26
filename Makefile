CD = cd
CP = cp -rf
RM = rm -rf
MV = mv -f
KCC ?= konanc
KLIB ?= klib
MKDIR = mkdir -p
INSTALL ?= install

OS ?= $(shell uname)
SRC += *.kt
TEST ?= test/
NAME ?= tape
BUILD ?= build
PREFIX ?= /usr/local
SHARED_LIB_SUFFIX ?= so
ifeq ($(OS),Darwin)
SHARED_LIB_SUFFIX = dylib
endif

INCLUDE += tape.h

CLASSES += $(wildcard *.class)
CLASSES += $(wildcard **/*.class)
CLASSES += $(wildcard **/**/*.class)

HEADER ?= $(NAME)_api.h
STATIC_LIBRARY ?= $(BUILD)/lib/lib$(NAME).a
SHARED_LIBRARY ?= $(BUILD)/lib/lib$(NAME).$(SHARED_LIB_SUFFIX)
KOTLIN_LIBRARY ?= $(BUILD)/lib/$(NAME).klib
FRAMEWORK_LIBRARY ?= $(BUILD)/lib/$(NAME).framework

KCCFLAGS += -output $(NAME)

define build
$(MKDIR) $(BUILD)/{include,lib}
$(KCC) $(KCCFLAGS) -produce $1 $(SRC)
$(MV) $$(basename $@) $@
if [ -f $(HEADER) ]; then $(MV) $(HEADER) $(BUILD)/include; fi
$(CP) $(INCLUDE) $(BUILD)/include
endef

.PHONY: static shared klib framework

build: $(STATIC_LIBRARY)
build: $(SHARED_LIBRARY)
build: $(KOTLIN_LIBRARY)

ifeq ($(OS),Darwin)
build: $(FRAMEWORK_LIBRARY)
framework: $(FRAMEWORK_LIBRARY)
endif

static: $(STATIC_LIBRARY)
shared: $(SHARED_LIBRARY)
klib: $(KOTLIN_LIBRARY)

install: build
	test -f $(STATIC_LIBRARY) && $(INSTALL) $(STATIC_LIBRARY) $(SHARED_LIBRARY) $(PREFIX)/lib
	test -f $(INCLUDE) && $(INSTALL) $(INCLUDE) $(PREFIX)/include
	test -f $(KLIB) && $(KLIB) install $(KOTLIN_LIBRARY)

uninstall:
	$(RM) $(PREFIX)/lib/lib$(NAME).{a,so,dylib}
	$(RM) $(PREFIX)/include/tape*
	$(KLIB) remove $(NAME)

clean:
	$(RM) $(BUILD) $(CLASSES) *.class
	$(MAKE) $@ -C test

.PHONY: test
test:
	$(MAKE) -C $@

.PHONY: test/c
test/c:
	$(MAKE) -C $@

$(STATIC_LIBRARY): $(SRC)
	$(call build, static)

$(SHARED_LIBRARY): $(SRC)
	$(call build, dynamic)

ifeq ($(OS),Darwin)
$(FRAMEWORK_LIBRARY): $(SRC)
	$(call build, framework)
endif

$(KOTLIN_LIBRARY): $(SRC)
	$(MKDIR) $(BUILD)/lib
	$(KCC) -output $(BUILD)/lib/$(NAME) -produce library $(SRC)
	$(CP) $(KOTLIN_LIBRARY) .

$(foreach inc,$(INCLUDE), $(BUILD)/$(inc)): $(INCLUDE)
	$(CP) $< $@
