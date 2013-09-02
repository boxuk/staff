package lib

import play.api.libs.functional.syntax._
import play.api.libs.json._

import java.net.URL

/**
 * Pull in data from Fleetsuite API. Create users if they are not already in the system.
 *
 */

sealed case class User(account_id: Long, 
                       email: String, 
                       firstname: String, 
                       id: Long, 
                       lastname: String)

object User {
  implicit val userReads = Json.reads[User]
}

object Import {

  private val baseUrl = "http://www.fleetsuite.com/api/v1/users?token="

  def buildUrl(token: String) = baseUrl ++ token

  def loadFile(path: String) = {
    io.Source.fromURL(path).mkString
  }

  /**
   * Get all the data as JSON and parse the result into a seq of Users
   */
  def parse(token: String) = {
    val json = Json.parse(loadFile(buildUrl(token)))
    Json.fromJson[Seq[User]](json)
  }
}
