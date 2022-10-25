package ro.comanitza.sticky.web

import java.util

import javax.servlet.http.{HttpServletRequest, HttpSession}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestMapping, RequestParam}
import org.springframework.web.servlet.ModelAndView
import ro.comanitza.sticky.Constants
import ro.comanitza.sticky.dto.{Sticky, User}
import ro.comanitza.sticky.service.{StickiesService, UsersService}

import collection.JavaConverters._

@Controller
@RequestMapping(path = Array("/"))
@Autowired
class WebController(private val usersService: UsersService, private val stickiesService: StickiesService) {

  private val log: Logger = LoggerFactory.getLogger(classOf[WebController])

  @GetMapping(path = Array("/", "start"))
  def index(): ModelAndView = {

    new ModelAndView("web/index")
  }

  @GetMapping(path = Array("login"))
  def logIn(): ModelAndView = {
    new ModelAndView("web/login")
  }

  @GetMapping(path = Array("createuser"))
  def createUser(): ModelAndView = {
    new ModelAndView("web/createuser")
  }

  @PostMapping(path = Array("createuseraction"))
  def createUserAction(@RequestParam(required = false) email: String, @RequestParam(required = false) pass: String, req: HttpServletRequest): ModelAndView = {
    
    usersService.createUser(new User(email = email, pass = pass)) match {
      case Right(id) => {

        /**
         * log in the newly created user
         */
        usersService.performLogin(email, pass, req.getSession(true))
        stickiesService.createSticky(stickyContent = "hello, welcome to stickies!", userId = id)

        new ModelAndView("redirect:/stickies")
      }

      case Left(errorMessage) => {

        val paramsMap = new util.HashMap[String, Any]()
        paramsMap.put("errorMessage", errorMessage)

        new ModelAndView("web/createuser", paramsMap)
      }
    }
  }

  @GetMapping(path = Array("stickies"))
  def stickies(session: HttpSession): ModelAndView = {

    val stickies = stickiesService.fetchAllStickiesForUser(session.getAttribute(Constants.USER_ID).asInstanceOf[Int])

    val m = new util.HashMap[String, Any]()
    m.put("stickies", stickies.asJava)

    new ModelAndView("web/stickies", m)
  }

  @PostMapping(path = Array("loginaction"))
  def loginAction(@RequestParam(required = false) email: String, @RequestParam(required = false) pass: String, req: HttpServletRequest): ModelAndView = {

    val session = req.getSession(true)

    if (!usersService.performLogin(email, pass, session)) {

      val paramsMap = new util.HashMap[String, Any]()

      paramsMap.put("errorMessage", "Wrong email or password.")

      return new ModelAndView("web/login", paramsMap)
    }

    new ModelAndView("redirect:/stickies")
  }
}
