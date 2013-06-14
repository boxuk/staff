package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

import play.api.libs.functional.syntax._
import play.api.libs.json._

// A role represents a job title -> i.e software developer, project manager etc
sealed case class Role(id: Long, role: String)

object Role {

  val role = {
    get[Long]("id")~
    get[String]("role_type") map {
      case id~role => Role(id,role)
    }
  }

  def all(): List[Role] = DB.withConnection { implicit c =>
    SQL("""select * from roles""").as(role *)
  }

  def findById(id: Long): Option[Role] = DB.withConnection { implicit c =>
    SQL("""select * from roles where id = {id}""").on('id -> id)
                                                  .as(role.singleOpt)
  }

  def findByName(name: String): Option[Role] = DB.withConnection { implicit c =>
    SQL("select * from roles where role = {name}").on('name -> name)
                                                  .as(role.singleOpt)

  }

  def create(role: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("insert into roles (role_type) values ({role})").on(
        'role   -> role
      ).executeUpdate()
    }
  }

  def delete(id: Long): Unit = {
    DB.withConnection { implicit c =>
      SQL("delete from roles where id = {id}").on(
      'id -> id
      ).executeUpdate()
    }
  }

  // JSON serialization methods
  implicit val roleFormat = Json.writes[Role]
  implicit val roleReads  = Json.reads[Role]
}
