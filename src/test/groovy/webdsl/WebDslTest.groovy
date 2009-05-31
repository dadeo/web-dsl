package webdsl

import webdsl.support.TableDsl
import webdsl.support.ElementDsl
import webdsl.support.SelectDsl
import webdsl.WebDsl
import webdsl.support.ElementDsl
import webdsl.support.SelectDsl
import webdsl.JettyRunner

class WebDslTest extends GroovyTestCase {
  public static final PORT = 8081
  def web
  def server = new JettyRunner(port:PORT)

  void setUp() {
    server.start()
    web = new WebDsl().for("http://localhost:$PORT/main.html")
  }

  void tearDown() {
    server.stop()
  }

  void test_closure_result_returned() {
    assertEquals 3, web.do {3}
  }
  
  void test_page_resets_getters() {
    web.do {
      namedRainbow

      shouldFail(MissingPropertyException) {
        namedMain
      }

      namedRainbow.click()

      shouldFail(MissingPropertyException) {
        namedRainbow
      }

      namedMain
    }
  }

  void test_page_resets_getters_when_string_properties_are_used() {
    web.do {
      namedRainbow

      'Submit 1'.click()

      shouldFail(MissingPropertyException) {
        namedRainbow
      }
    }
  }

  void test_title() {
    web.do {
      assertEquals "Main Page 1", title
    }
  }

  void test_click_anchor_by_name() {
    web.do {
      assertTrue namedRainbow instanceof ElementDsl
      assertEquals "the rainbow", namedRainbow.text
      namedRainbow.click()
    }
    assertEquals "Rainbow Page", web.title
  }

  void test_click_anchor_by_id() {
    web.do {
      assertTrue rainbow instanceof ElementDsl
      rainbow.click()
    }
    assertEquals "Rainbow Page", web.title
  }

  void test_click_anchor_by_href() {
    web.do {
      "rainbow.html".click()
    }
    assertEquals "Rainbow Page", web.title
  }

  void test_click_anchor_by_text() {
    web.do {
      "the rainbow".click()
    }
    assertEquals "Rainbow Page", web.title
  }

  void test_text_from_line_item() {
    web.do {
      assertEquals "message 3", message3.text
    }
  }

  void test_text_from_div() {
    web.do {
      assertEquals "message 0", message0.text
    }
  }

  void test_textOfChildren() {
    def expected = ["message 0", "message 1message 2message 3", "message 1", "message 2", "message 3"]
    web.do {
      bind.actual = errors.children.text
    }
    assertEquals expected.size(), web.bind.actual.size()
    assertEquals expected, web.bind.actual
  }

  void test_textOfChildren_of_type() {
    def expected = ["message 1", "message 2", "message 3"]
    web.do {
      bind.actual = errors.children(type:"li").text
    }
    assertEquals expected.size(), web.bind.actual.size()
    assertEquals expected, web.bind.actual
  }

  void test_textOfChildren_of_types() {
    def expected = ["message 0", "message 1", "message 2", "message 3"]
    web.do {
      bind.actual = errors.children(types:["li", "div"]).text
    }
    assertEquals expected.size(), web.bind.actual.size()
    assertEquals expected, web.bind.actual
  }

  void test_form_defaults_to_first_form_on_page() {
    web.do {
      form {
        submit
      }
    }
    assertEquals "TestServlet form0", web.title
    assertEquals "form0", server.path
  }

  void test_form_defaults_to_first_form_on_page_with_do() {
    web.do {
      form.do {
        submit
      }
    }
    assertEquals "TestServlet form0", web.title
    assertEquals "form0", server.path
  }

  void test_form_by_id() {
    web.do {
      form1 {
        submit
      }
    }
    assertEquals "TestServlet form1", web.title
    assertEquals "form1", server.path
  }

  void test_form_by_name_getter() {
    web.do {
      namedForm2 {
        submit
      }
    }
    assertEquals "TestServlet form2", web.title
    assertEquals "form2", server.path
  }

  void test_form_has_do() {
    web.do {
      namedForm2.do {
        submit
      }
    }
    assertEquals "TestServlet form2", web.title
    assertEquals "form2", server.path
  }

  void test_click_button_by_name() {
    web.do {
      form {
        namedSubmit1.click()
      }
    }
    assertEquals "TestServlet form0", web.title
    assertEquals "form0", server.path
    assertTrue server.params.containsKey('namedSubmit1')
  }

  void test_click_button_by_id() {
    web.do {
      form {
        submit1.click()
      }
    }
    assertEquals "form0", server.path
    assertTrue server.params.containsKey('namedSubmit1')
  }

  void test_click_button_by_value() {
    web.do {
      form {
        'Submit 1'.click()
      }
    }
    assertEquals "form0", server.path
    assertTrue server.params.containsKey('namedSubmit1')
  }

  void test_click_checkbox_by_name() {
    web.do {
      form {
        namedCheckbox1.click()
        namedCheckbox2.click()
        submit
      }
    }
    assertEquals "form0", server.path
    assertEquals "Checkbox 1", server.params.namedCheckbox1[0]
    assertEquals null, server.params.namedCheckbox2
  }

  void test_click_checkbox_by_id() {
    web.do {
      form {
        checkbox1.click()
        checkbox2.click()
        submit
      }
    }
    assertEquals "form0", server.path
    assertEquals "Checkbox 1", server.params.namedCheckbox1[0]
    assertEquals null, server.params.namedCheckbox2
  }

  void test_form_text_by_id() {
    web.do {
      form {
        assertEquals 'a default value', nameId.value
        nameId.value = 'henry'
        submit
      }
    }
    assertEquals "henry", server.params.name[0]
  }

  void test_form_text_by_name() {
    web.do {
      form {
        assertTrue name instanceof ElementDsl
        assertEquals 'a default value', name.value
        name.value = 'henry'
        submit
      }
    }
    assertEquals "henry", server.params.name[0]
  }

  void test_form_select_by_id() {
    web.do {
      form {
        assertTrue auto instanceof SelectDsl
        assertEquals 'Volvo', autoId.value
        autoId.value = 'audi'
        submit
      }
    }
    assertEquals "audi", server.params.auto[0]
  }

  void test_form_select_by_name() {
    web.do {
      form {
        assertTrue auto instanceof SelectDsl
        assertEquals 'Volvo', auto.value
        auto.value = 'audi'
        submit
      }
    }
    assertEquals "audi", server.params.auto[0]
  }

  void test_values() {
    Map actual
    web.do {
      form {
        actual = values()
      }
    }
    assertEquals([name:"a default value", auto:"Volvo", namedCheckbox1:false, namedCheckbox2:true, radio1:"radio content 3"], actual)
  }

  void test_valuesById() {
    Map actual
    web.do {
      form {
        actual = valuesById()
      }
    }
    assertEquals([nameId:"a default value", autoId:"Volvo", checkbox1:false, checkbox2:true, radio1_1:false, radio1_2:false, radio1_3:true], actual)
  }

  void test_radio_with_label() {
    web.do {
      form {
        assertEquals "radio content 3", radio1.text
        assertEquals "radio 1 value 3", radio1.value
        assertEquals true, radio1.checked
        radio1_2.click()
        submit
      }
    }
    assertEquals 'radio 1 value 2', server.params.radio1[0]
  }

  void test_radio_with_no_label() {
    web.do {
      form {
        assertEquals "", radio1_2.text
        assertEquals "radio 1 value 2", radio1_2.value
      }
    }
  }

  void test_table() {
    List result
    web.do {
      form {
        assertTrue table1 instanceof TableDsl
        result = table1.by.span
      }
    }
    def expected = [[firstName:"pinky", lastName:"jones"],[firstName:"john", lastName:"doe"]]
    assertEquals expected, result
  }

  void test_table_column() {
    List result
    web.do {
      form {
        result = table1.by.columns(['first', 'last'])
      }
    }
    def expected = [[first:"pinky", last:"jones"],[first:"john", last:"doe"]]
    assertEquals expected, result
  }

  void test_table_columns_not_all_columns_requested() {
    List result
    web.do {
      form {
        result = table1.by.columns(['first'])
      }
    }
    def expected = [[first:"pinky"],[first:"john"]]
    assertEquals expected, result
  }

  void test_table_columns_extra_columns_requested() {
    List result
    web.do {
      form {
        result = table1.by.columns(['first', 'last', 'ssn'])
      }
    }
    def expected = [[first:"pinky", last:"jones", ssn:""],[first:"john", last:"doe", ssn:""]]
    println result
    assertEquals expected, result
  }
}