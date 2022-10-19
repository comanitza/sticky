package ro.comanitza.sticky.service

import ro.comanitza.sticky.dto.{Note, Sticky, User}

/**
 *
 * @author stefan.comanita
 */
trait Dao {

  def createTables(): Unit

  def createUser(user: User): Either[Exception, Boolean]

  def fetchUserByEmail(userName: String, pass: String): Either[Exception, Option[User]]

  def createSticky(sticky: Sticky, userId: Int): Either[Exception, Boolean]

  def fetchStickiesForUser(userId: Int): Either[Exception, List[Sticky]]

  def createNote(note: Note): Either[Exception, Boolean]

  def updateLastLogin(userId: Int): Either[Exception, Boolean]
}
