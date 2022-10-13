package ro.comanitza.sticky.dto

class Sticky(val id: Int = 0, val content: String, val posX: Int, val posY: Int, val category: String, val created: Long = 0L) {

  override def toString: String = s"Sticky[$id]"
}
