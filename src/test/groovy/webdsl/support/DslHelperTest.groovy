package webdsl.support


class DslHelperTest extends GroovyTestCase {
  def helper

  void setUp() {
    helper = new DslHelper()
    helper.factory = [create: {a, b-> b._value}]
  }
  
  void test_addGetterMethodsFor_multiple_elements() {
    def targetObject = new Object()

    helper.addGetterMethodsForAll([createElement(1, 123), createElement(2,'abc')], ['id', 'name'], targetObject)

    assertEquals 123, targetObject.element1
    assertEquals 123, targetObject.namedElement1

    assertEquals 'abc', targetObject.element2
    assertEquals 'abc', targetObject.namedElement2
  }

  void test_addGetterMethodsFor_single_letter_id_and_name() {
    def targetObject = new Object()

    helper.addGetterMethodsForAll([createElement("a", "b", 123)], ['id', 'name'], targetObject)

    assertEquals 123, targetObject.a
    assertEquals 123, targetObject.b
  }

  void test_addGetterMethodsFor_only_modifies_target_objects_class() {
    test_addGetterMethodsFor_multiple_elements()

    shouldFail(MissingPropertyException) {
      new Object().element1
    }
  }

  def createElement(int number, value) {
    createElement "element$number", "namedElement$number", value
  }

  def createElement(String id, String name, value) {
    def elementObject = [_id: id, _name: name, _value:value]
    elementObject.getAttribute = {it == 'id' ? elementObject._id : elementObject._name }
    elementObject
  }
}