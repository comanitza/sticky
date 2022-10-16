package ro.comanitza.sticky.dto

class Sticky(val id: Int = 0, val content: String, val posX: Int, val posY: Int, val category: String, val created: Long = 0L, var notes: List[Note] = List.empty[Note]) {

  override def toString: String = s"Sticky[$id]"
}
