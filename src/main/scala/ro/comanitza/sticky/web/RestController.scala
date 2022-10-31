package ro.comanitza.sticky.web

import java.util.stream.Collectors

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import javax.servlet.http.{HttpServletRequest, HttpSession}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestMapping, RequestParam}
import ro.comanitza.sticky.Constants
import ro.comanitza.sticky.dto.Sticky
import ro.comanitza.sticky.service.{ExceptionService, StickiesService}

@org.springframework.web.bind.annotation.RestController
@RequestMapping(path = Array("/rest/"))
@Autowired
class RestController(stickiesService: StickiesService, exceptionService: ExceptionService) {

  private val log: Logger = LoggerFactory.getLogger(classOf[WebController])
  private val mapper: ObjectMapper = createObjectMapper()

  @PostMapping(path = Array("createSticky", "createsticky"))
  def createSticky(@RequestParam(required = false) jsonPayLoad: Map[String, String], session: HttpSession, req: HttpServletRequest): ResponseEntity[String] = {


    try {
      val stickyPayload = mapper.readValue(req.getReader().lines().collect(Collectors.joining()), classOf[StickyPayload])

      stickiesService.createSticky(stickyPayload.content, stickyPayload.category, session.getAttribute(Constants.USER_ID).asInstanceOf[Int]) match {
        case Right(id) => ResponseEntity.status(HttpStatus.OK).body(String.valueOf(id))
        case Left(exception) => {

          log.error("Error creating sticky", exception)

          exceptionService.addException(exception)

          ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.valueOf(exception.getMessage))
        }
      }
    } catch {
      case e: Exception => {

        log.error("Error creating sticky", e)

        exceptionService.addException(e)

        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.valueOf(e.getMessage))
      }
    }
  }

  @GetMapping(path = Array("listStickies", "liststickies"))
  def listStickies(): List[Sticky] = {
    List.empty
  }

  private def createObjectMapper(): ObjectMapper = {

    val mapper = new ObjectMapper()

    mapper.registerModule(DefaultScalaModule)

    mapper
  }
}

class StickyPayload(val content: String, val category: String) {

  def this() {
    this(null, null)
  }

}
