package ro.comanitza.sticky.dto

class Note(val id: Int = 0, val content: String, val timestamp: Long = 0) {

  override def toString: String = s"Note[$id]"
}
