package ro.comanitza.sticky

import scala.util.control.NonFatal

object SUtils {

  /**
   *
   * Method for quietly closing an AutoCloseable instance
   *
   * @param autoCloseable
   */
  def closeQuietly(autoCloseable: AutoCloseable): Unit = {

    try {

      if (autoCloseable != null) {
        autoCloseable.close()
      }

    } catch {
      case NonFatal(e) => //ignore exception
    }
  }

}
