package ro.comanitza.sticky.service

import ro.comanitza.sticky.dto.Sticky

import scala.util.Random

class StickiesService(dao: Dao) {

  private val rand:Random = new Random()

  def createSticky(stickyContent: String, category: String = null, userId: Int): Either[Exception, Int] = {

    dao.createSticky(new Sticky(content = stickyContent, posX = 12 + rand.nextInt(20), posY = 12 + rand.nextInt(20), category = category), userId)
  }

  def fetchAllStickiesForUser(userId: Int): List[Sticky] = {

    dao.fetchStickiesForUser(userId) match {
      case Right(stickies) => stickies
      case Left(exception) => throw exception
    }
  }

}
