package webdsl.support

class DslHelper {

  static def addGetterMethodsFor(attributes, method, element, ExpandoMetaClass emc) {
    attributes.each {attribute ->
      def methodName = element.getAttribute(attribute)
      if (methodName) {
        methodName = methodName[0].toUpperCase() + methodName[1..-1]
        emc."get$methodName" = method
      }
    }
  }

}