package ro.comanitza.sticky.web

import javax.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestMapping}
import ro.comanitza.sticky.Constants
import ro.comanitza.sticky.dto.Sticky
import ro.comanitza.sticky.service.{ExceptionService, StickiesService}

@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = Array("/rest/"))
@Autowired
class RestController(stickiesService: StickiesService, exceptionService: ExceptionService) {

  @PostMapping(path = Array("createSticky", "createsticky"))
  def createSticky(content: String, category: String, session: HttpSession): ResponseEntity[String] = {

    stickiesService.createSticky(content, category, session.getAttribute(Constants.USER_ID).asInstanceOf[Int]) match {
      case Right(id) => ResponseEntity.status(HttpStatus.OK).body(String.valueOf(id))
      case Left(exception) => {


        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.valueOf(exception.getMessage))
      }
    }
  }

  @GetMapping(path = Array("listStickies", "liststickies"))
  def listStickies(): List[Sticky] = {
    List.empty
  }
}
