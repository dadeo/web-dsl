[![Build Status](https://travis-ci.org/dadeo/web-dsl.svg?branch=master)](https://travis-ci.org/dadeo/web-dsl)

# web-dsl
web-dsl is a groovy wrapper around [HtmlUnit](http://htmlunit.sourceforge.net/).  It was originally written for two reasons.
The first was to 
learn to write DSL's in groovy.  The second was to perform HtmlUnit page management since many actions on HtmlUnit elements 
return a new page instance that needs to be tracked.  Since then, many useful features have been added. 

Most of the groovy DSL like syntax has been deprecated or removed. However, the name has stuck.

## Getting Started
First create a WebDsl object
```
WebDsl webDsl = new WebDsl("http://localhost:8081/test.html")
```
Next, use it.
```
assert webDsl.title == 'Foo Bar'
assert webDsl.$('#foo').value == 'bar'
```

When doing multiple method calls on the webDsl object, they may be performed in a do block.
```
webDsl.do {
    assert title == 'Foo Bar'
    assert $('#foo').value == 'bar'
}
```

The $ selector method returns the first Dsl wrapper that matches the specified selector. Use $$ to return a list of
Dsl wrappers for the given selector.

## HtmlElement Dsl Wrappers
HtmlUnit elements are wrapped with a WebDsl element. The base Dsl element is BaseElementDsl. Some elements have a 
specialized Dsl wrapper like HtmlTable which maps to TableDsl. The mappings are defined in [DslFactory](https://github.com/dadeo/web-dsl/blob/master/src/main/groovy/com/github/dadeo/webdsl/support/DslFactory.groovy). 

## Where's The Rest Of The Docs?
Currently the only documentation exists in the tests.  

html element|tests
------ | ------
check box|[CheckBoxDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/CheckBoxDslTest.groovy)
div|[DivDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/DivDslTest.groovy)
input button|[ButtonInputDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/ButtonInputDslTest.groovy)
input hidden|[HiddenInputDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/HiddenInputDslTest.groovy)
input password|[PasswordInputDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/PasswordInputDslTest.groovy)
input submit|[SubmitInputDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/SubmitInputDslTest.groovy)
radio button|[RadioButtonDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/RadioButtonDslTest.groovy)
select|[SelectDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/SelectDslTest.groovy)
span|[SpanDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/SpanDslTest.groovy)
table|[TableDsl_objects_Test](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/TableDsl_objects_Test.groovy)
text area|[TextAreaDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/TextAreaDslTest.groovy)
text input|[TextInputDslTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/TextInputDslTest.groovy)

misc|tests
------ | ------
existence of elements|[ExistsTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/ExistsTest.groovy)
selecting elements|[CssSelectorTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/CssSelectorTest.groovy)
wait for|[WaitForTest](https://github.com/dadeo/web-dsl/blob/master/src/test/groovy/com/github/dadeo/webdsl/WaitForTest.groovy)

