package ro.comanitza.sticky.dto

class Sticky(val id: Int, val text: String, posX: Int, posY: Int, category: String, created: Long) {

  override def toString: String = s"Sticky[$id]"
}
