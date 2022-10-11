package ro.comanitza.sticky.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestMapping}
import ro.comanitza.sticky.dto.Sticky

@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = Array("/rest/"))
@Autowired
class RestController {

  @PostMapping(path = Array("createSticky", "createsticky"))
  def createSticky(): Unit = {

  }

  @GetMapping(path = Array("listStickies", "liststickies"))
  def listStickies(): List[Sticky] = {
    List.empty
  }
}
