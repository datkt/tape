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
NAME ?= ktape
BUILD ?= build
PREFIX ?= /usr/local

INCLUDE += ktape.h

CLASSES += $(wildcard *.class)
CLASSES += $(wildcard **/*.class)
CLASSES += $(wildcard **/**/*.class)

HEADER ?= $(NAME)_api.h
STATIC_LIBRARY ?= $(BUILD)/lib/lib$(NAME).a
SHARED_LIBRARY ?= $(BUILD)/lib/lib$(NAME).so
KOTLIN_LIBRARY ?= $(BUILD)/lib/$(NAME).klib
FRAMEWORK_LIBRARY ?= $(BUILD)/lib/$(NAME).frame

KCCFLAGS += -output $(NAME)

define build
$(MKDIR) $(BUILD)/{include,lib}
$(KCC) $(KCCFLAGS) -produce $1 $(SRC)
$(MV) $$(basename $@) $@
if [ -f $(HEADER) ]; then $(MV) $(HEADER) $(BUILD)/include; fi
$(CP) $(INCLUDE) $(BUILD)/include
endef

build: $(STATIC_LIBRARY)
build: $(SHARED_LIBRARY)
build: $(KOTLIN_LIBRARY)
ifeq ($(OS),Darwin)
build: $(FRAMEWORK_LIBRARY)
endif

install: build
	$(INSTALL) $(STATIC_LIBRARY) $(SHARED_LIBRARY) $(PREFIX)/lib
	$(INSTALL) $(INCLUDE) $(PREFIX)/include
	$(KLIB) install $(KOTLIN_LIBRARY)

uninstall:
	$(RM) $(PREFIX)/lib/lib$(NAME).{a,so,dylib}
	$(RM) $(PREFIX)/include/ktape*

clean:
	$(RM) $(BUILD) $(CLASSES)
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
	$(MKDIR) $(BUILD)/{include,lib}
	$(KCC) -output $(BUILD)/lib/$(NAME) -produce library $(SRC)

$(foreach inc,$(INCLUDE), $(BUILD)/$(inc)): $(INCLUDE)
	$(CP) $< $@
