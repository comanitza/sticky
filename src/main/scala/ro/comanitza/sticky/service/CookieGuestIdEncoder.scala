package ro.comanitza.sticky.service

trait CookieGuestIdEncoder {

  def encodeGuestId(guestId: Int): String

  def decodeGuestId(encodedGuestId: String): Int
}
