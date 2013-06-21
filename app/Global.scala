import play.api._

import models.{ Role, Employee }
import anorm._

import lib.{ Import }

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    SeedData.insert()
  }
}

object SeedData {

  // Requires an authentication token from FS!
  def seedFromFleetsuite(token: String) {
    val users = Import.parse(token)
    users.get.foreach { user =>
      val employee = Employee.build((user.firstname, user.lastname, user.email, "", 1, "", ""))
      // Only create the user is they don't already exist on the system
      if (!Employee.findByType("email", email).isDefined)
        Employee.create(employee)
    }
  }

  def insert() {
    println("Seeding roles...")
    if(Role.all.isEmpty) {
      Seq(
        "developer",
        "consultant",
        "project manager"
      ).foreach { r => if (Role.findByName(r).isEmpty) Role.create(r) }
    }
  }
}
