import play.api._

import models._
import anorm._

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    SeedData.insert()
  }
}

object SeedData {

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
