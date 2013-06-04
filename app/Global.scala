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
    if(Role.all.isEmpty) {
      Seq(
        Role("developer"),
        Role("consultant"),
        Role("project manager")
      ).foreach { role => Role.create(role.role) }
    }
  }
}
