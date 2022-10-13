package ro.comanitza.sticky.service

import ro.comanitza.sticky.dto.{Note, Sticky, User}

/**
 *
 * @author stefan.comanita
 */
trait Dao {

  def createTables(): Unit

  def createUser(user: User): Either[Exception, Boolean]

  def fetchUserByUsername(userName: String, pass: String): Either[Exception, Option[User]]

  def createSticky(sticky: Sticky, userId: Int): Either[Exception, Boolean]

  def fetchStickiesForUser(userId: Int): Either[Exception, List[Sticky]]

  def createNote(note: Note, stickyId: Int): Either[Exception, Boolean]
}
