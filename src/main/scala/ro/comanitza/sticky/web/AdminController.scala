package ro.comanitza.sticky.web

import java.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping}
import org.springframework.web.servlet.ModelAndView
import ro.comanitza.sticky.service.{ExceptionService, StickiesService, UsersService}

import collection.JavaConverters._

@Controller
@RequestMapping(path = Array("/admin"))
@Autowired
class AdminController(private val usersService: UsersService, private val stickiesService: StickiesService, exceptionService: ExceptionService) {


  @GetMapping(path = Array("stats"))
  def stats(): ModelAndView = {

    val m = new util.HashMap[String, Any]()
    m.put("userCount", usersService.countUsers())
    m.put("stickiesCount", stickiesService.countStickies())

    new ModelAndView("admin/stats", m)
  }

  @GetMapping(path = Array("exceptions"))
  def exceptions(): ModelAndView = {

    val m = new util.HashMap[String, Any]()

    m.put("exceptions", exceptionService.fetchException().toList.asJava)

    new ModelAndView("admin/exceptions", m)
  }
}
