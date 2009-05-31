package webdsl.support

class DslHelper {
  def factory = new DslFactory()

  def addGetterMethodsForAll(elements, List attributes, target) {
    def emc = new ExpandoMetaClass(target.getClass(), false, true)
    elements.each {element ->
      def dsl = factory.create(target, element)
      addGetterMethodsFor(attributes, {-> dsl }, element, emc)
    }
    emc.initialize()
    target.metaClass = emc
  }

  def addGetterMethodsFor(attributes, method, element, ExpandoMetaClass emc) {
    attributes.each {attribute ->
      def methodName = element.getAttribute(attribute)
      if (methodName) {
        methodName = methodName[0].toUpperCase() + (methodName.size() > 1 ? methodName[1..-1] : "")
        emc."get$methodName" = method
      }
    }
  }

}