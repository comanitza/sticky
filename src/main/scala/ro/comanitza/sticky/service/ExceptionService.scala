package ro.comanitza.sticky.service

import java.util.concurrent.locks.ReentrantReadWriteLock

import ro.comanitza.sticky.CircularList

import collection.JavaConverters._

/**
 *
 * Exception tracking service
 *
 * @author stefan.comanita
 * @param bufferSize the size of the used circular list
 */
class ExceptionService(private val bufferSize: Int = 20) {

  private val rwLock = new ReentrantReadWriteLock()
  private val circularList = new CircularList[Exception](bufferSize)

  def addException(exception: Exception): Unit = {

    rwLock.writeLock().lock()

    try {
      circularList.add(exception)
    } finally {
      rwLock.writeLock().unlock()
    }
  }

  def fetchException(): Iterable[Exception] = {

    rwLock.readLock().lock()
    try {
      circularList.fetchList().asScala
    } finally {
      rwLock.readLock().unlock()
    }
  }
}
