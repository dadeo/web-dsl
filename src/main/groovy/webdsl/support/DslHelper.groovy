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
        emc[toGetter(methodName)] = method
      }
    }
  }

  static def toGetter(name) {
    def start = name[0]
    if(start =~ /[A-Z]/) {
     start = '$' + start
    } else {
      start = start.toUpperCase()
    }
    "get" + start + (name.size() > 1 ? name[1..-1] : "")
  }

  static def fromGetter(name) {
    name -= 'get'
    def start
    if(name.startsWith('$')) {
      name -= '$'
      start = name[0]
    } else {
      start = name[0].toLowerCase()
    }
    start + (name.size() > 1 ? name[1..-1] : "")
  }
}