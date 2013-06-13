package lib

import play.api.libs.json._

/**
 * Imports existing user data to prepopulate the application
 *
 *
 */
object Import {
  def loadFile(path: String): String = {
    io.Source.fromFile(path)
  }

  def parse(file: String) = {
    Json.parse(loadFile(file))
  }
}
