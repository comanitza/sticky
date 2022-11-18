package ro.comanitza.sticky.service

import javax.servlet.http.HttpSession
import org.slf4j.{Logger, LoggerFactory}
import ro.comanitza.sticky.Constants
import ro.comanitza.sticky.dto.User

/**
 *
 * Service for handling user interactions
 *
 * @author stefan.comanita
 */
class UsersService(dao: Dao, exceptionService: ExceptionService) {

  private val log: Logger = LoggerFactory.getLogger(classOf[UsersService])

  def performGuestLogin(guestId: Int, session: HttpSession): Boolean = {

    performLoginBase(dao.fetchGuestUserById(guestId, "guestPass"), session)
  }

  def performLogin(email: String, pass: String, session: HttpSession): Boolean = {

    dao.fetchUserByEmail(email, pass) match {
      case Left(value) => {

        log.error("Error login in email {}", email: Any, value)
        exceptionService.addException(value)

        false
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
      case Left(ex) =>  {

        exceptionService.addException(ex)

        Left("Incorrect user or pass")
      }
    }
  }

  def countUsers(): Int = {

    dao.countRowsInTable("users") match {
      case Right(value) => value
      case Left(ex) => {
        exceptionService.addException(ex)

        -1
      }
    }
  }

  private def performLoginBase(daoResult: Either[Exception, Option[User]], session: HttpSession): Boolean = {

    daoResult match {
      case Left(value) => {

        log.error("Error login in", value)
        exceptionService.addException(value)

        false
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
}
