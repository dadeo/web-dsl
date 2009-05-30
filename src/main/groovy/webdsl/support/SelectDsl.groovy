package webdsl.support


class SelectDsl extends ElementDsl {
  SelectDsl(pageContainer, element) {
    super(pageContainer, element)
  }

  @Override
  def getValue() {
    element.getSelectedOptions()[0].getTextContent()
  }

  @Override
  def setValue(value) {
    pageContainer.page = element.setSelectedAttribute(value, true)
  }

  def tableValue(attributeName) {
    value
  }
  
}