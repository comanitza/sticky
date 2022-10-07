package ro.comanitza.sticky.web

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping}
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping(path = Array("/"))
@Autowired
class WebController {

  private val log: Logger = LoggerFactory.getLogger(classOf[WebController])

  @GetMapping(path = Array("/", "start"))
  def index(): ModelAndView = {

    new ModelAndView("index")
  }
}
