package ro.comanitza.sticky.service

class CookieGuestIdEncoderImpl extends CookieGuestIdEncoder {

  override def encodeGuestId(guestId: Int): String = {

    String.valueOf(guestId)
  }

  override def decodeGuestId(encodedGuestId: String): Int = {

    encodedGuestId.toInt
  }
}
