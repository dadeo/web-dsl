package webdsl.support


class RadioButtonDsl extends ElementDsl {
  RadioButtonDsl(pageContainer, element) {
    super(pageContainer, element)
  }

  @Override
  def getText() {
      findLabelFor(element.getAttribute("id"))?.getTextContent() ?: ""
  }

  def getChecked() {
    element.isDefaultChecked()
  }

  @Override
  def tableValue(attributeName) {
    if (attributeName == "name") {
      return checked ? text : null
    } else {
      return checked
    }
  }

}