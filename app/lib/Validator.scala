package lib

import models.{ Employee }

object Validate {

  def isUrl(url: String): Boolean = url.startsWith("http")

  def emailExists(email: String): Boolean = {
    Employee.findByType("email", email).isDefined
  }
}

