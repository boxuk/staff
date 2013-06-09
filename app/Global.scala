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
        "developer",
        "consultant",
        "project manager"
      ).foreach { _ => Role.create(_) }
    }
  }
}
