package lib

import java.net.{URI, URL, URLEncoder}

import java.security.{ MessageDigest }

private object Md5 {

  def hash(s: String) :String= {
    val md5 = MessageDigest.getInstance("MD5").digest(s.getBytes)
    asString(md5)
  }

  val hexDigits = "0123456789abcdef".toCharArray

  def asString(bytes:Array[Byte]) = {
    bytes.foldLeft(""){case (agg, b) => agg + hexDigits((b >> 4) & 0xf) + hexDigits(b & 0xf)}
  }
}

/**
 * Generates a Gravatar URL for a given email address
 *
 */
class Gravatar(private val email: String) {

  private val emailAddress = email.trim.toLowerCase
  private val emailHash = Md5.hash(emailAddress)

  def url(size: Int): String = {
    "http://www.gravatar.com/avatar/" + emailHash + "?d=mm&s=" + size
  }

  def hash() = emailHash
}

