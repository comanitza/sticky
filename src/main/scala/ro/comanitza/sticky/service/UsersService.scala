package ro.comanitza.sticky.service

import javax.servlet.http.HttpSession
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.web.servlet.ModelAndView
import ro.comanitza.sticky.Constants
import ro.comanitza.sticky.dto.User

/**
 *
 * Service for handling user interactions
 *
 * @author stefan.comanita
 */
class UsersService(dao: Dao) {

  private val log: Logger = LoggerFactory.getLogger(classOf[UsersService])

  def performLogin(email: String, pass: String, session: HttpSession): Boolean = {

    dao.fetchUserByEmail(email, pass) match {
      case Left(value) => {

        log.error("Error login in email {}", email: Any, value)

        return false
      }

      case Right(value) => {

        value match {
          case Some(user) => {

            session.setAttribute(Constants.LOGGED_IN, true)
            session.setAttribute(Constants.USER_ID, user.id)
            session.setAttribute(Constants.USER_EMAIL, user.email)

            /**
             * update the last login of the user
             */
            dao.updateLastLogin(user.id)

            true
          }
            
          case None => false
        }
      }
    }
  }

  def createUser (user: User): Either[String, Int] = {

    dao.createUser(user) match {
      case Right(id) => Right(id)
      //todo handle here duplicate email address
      case Left(ex) => Left("Incorrect user or pass")
    }
  }
}
