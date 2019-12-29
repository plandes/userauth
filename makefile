# build file for Java User Auth

MOCK_PWAUTH = 	src/test/python/pwauth.py

all:	compile

.PHONY:	compile
compile:
	mvn compile

.PHONY:	test
test:
	mvn -Plogging-deps test

.PHONY:	mockpwauthsanity
mockpwauthsanity:
	echo "bob\npass123" | $(MOCK_PWAUTH) - --debug

.PHONY:	testmockpwauth
testmockpwauth:	mockpwauthsanity
	$(eval RET := $(shell echo "bob\npass123" | $(MOCK_PWAUTH) ; echo $$?))
	[ $(RET) == 0 ]
	$(eval RET := $(shell echo "bob\nWRONG_PASS" | $(MOCK_PWAUTH) ; echo $$?))
	[ $(RET) == 2 ]
	@ echo mock pwauth success

.PHONY:	package
package:
	mvn -Plogging-deps package

.PHONY:	install
install:
	mvn -Plogging-deps install

.PHONY:	site
site:
	mvn -Plogging-deps site

.PHONY:	run
run:
	mvn -Plogging-deps compile exec:exec

.PHONY:	docs
docs:
	mvn javadoc:javadoc

.PHONY:	dist
dist:
	mvn -Plogging-deps package appassembler:assemble

.PHONY:	clean
clean:
	mvn clean
