package ro.comanitza.sticky.dto

class Note(val id: Int = 0, val stickyId: Int, val content: String, val created: Long = 0) {

  override def toString: String = s"Note[$id]"
}
