package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

// A role i.e software developer, project manager etc
case class Role(id: Long, role: String)

object Role {

  val role = {
    get[Long]("id")~
    get[String]("role_type") map {
      case (id~role) => Role(id, role)
    }
  }

  def all(): List[Role] = DB.withConnection { implicit c =>
    SQL("""select * from roles""").as(role *)
  }

  def findById(id: Long): Option[Role] = DB.withConnection { implicit c =>
    SQL("""select * from roles where id = {id}""").on('id -> id)
                                                  .as(role.singleOpt)
  }

  def create(role: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("insert into roles (role_type) values ({role})").on(
        'role   -> role
      ).executeUpdate()
    }
  }
}
