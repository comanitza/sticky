package ro.comanitza.sticky.service

import ro.comanitza.sticky.dto.{Note, Sticky, User}

/**
 *
 * @author stefan.comanita
 */
trait Dao {

  def createTables(): Unit

  def createUser(user: User): Either[Exception, Int]

  def fetchUserByEmail(userName: String, pass: String): Either[Exception, Option[User]]

  def createSticky(sticky: Sticky, userId: Int): Either[Exception, Int]

  def fetchStickiesForUser(userId: Int): Either[Exception, List[Sticky]]

  def createNote(note: Note): Either[Exception, Int]

  def updateLastLogin(userId: Int): Either[Exception, Boolean]

  def countRowsInTable(tableName: String): Either[Exception, Int]

  def deleteSticky(userId: Int, stickyId: Int): Either[Exception, Boolean]

  def moveSticky(userId: Int, stickyId: Int, posX: Int, posY: Int): Either[Exception, Boolean]

  def fetchGuestUserById(guestId: Int, pass: String): Either[Exception, Option[User]]
}
