package ro.comanitza.sticky.dto

class User (val id: Int = 0, val name: String = "", val pass: String, val email: String, val created: Long = 0, val lastLogin: Long = 0) {

  override def toString: String = s"User[$id, $name]"
}
