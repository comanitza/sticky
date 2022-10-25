package ro.comanitza.sticky.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import ro.comanitza.sticky.service.{StickiesService, UsersService}

@Controller
@RequestMapping(path = Array("/admin"))
@Autowired
class AdminController(private val usersService: UsersService, private val stickiesService: StickiesService) {


}
