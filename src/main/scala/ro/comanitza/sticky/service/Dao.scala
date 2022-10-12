package ro.comanitza.sticky.service

import ro.comanitza.sticky.dto.{Sticky, User}

trait Dao {

  def createTables(): Unit

  def createUser(user: User): Either[Exception, Boolean]

  def fetchUserByUsername(userName: String, pass: String): Either[Exception, Option[User]]

  def createSticky(sticky: Sticky): Either[Exception, Boolean]

  def fetchStickiesForUser(userId: Int): Either[Exception, List[Sticky]]
}
