package lib

import play.api.libs.json._

/**
 * Imports existing user data to prepopulate the application
 *
 *
 */
object Import {

  def loadFile(): String = {
    val f = "https://dl.dropboxusercontent.com/u/6475135/data.json"
    io.Source.fromURL(f).mkString
  }

  def parse() = {
    Json.parse(loadFile())
  }
}
