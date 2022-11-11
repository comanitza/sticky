package ro.comanitza.sticky.service

import javax.servlet.http.HttpSession
import ro.comanitza.sticky.dto.User

/**
 *
 * @param usersService
 */
//https://reflectoring.io/spring-boot-cookies/
//https://dzone.com/articles/how-to-use-cookies-in-spring-boot
class CookiesService(usersService: UsersService) {

  def createCookieUser(): Either[String, Int] = {

    usersService.createUser(new User(email = "guest" + System.currentTimeMillis(), pass = "guestPass"))

  }

  def loginGuestUser(guestId: Int, session: HttpSession): Boolean = {

    usersService.performGuestLogin(guestId, session)
  }

  def clearUserCookie(): Either[Exception, Boolean] = {
    null
  }
}
