package models

import play.api._
import play.api.mvc._
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

import play.api.libs.functional.syntax._
import play.api.libs.json._

import lib.{ Gravatar }

sealed case class Employee(
  id:      Option[Long]=None,
  first:   String,
  last:    String,
  email:   String,
  phone:   String,
  role:    String,
  website: String,
  bio:     String)

object Employee {

  implicit val employeeFormat = new Writes[Employee] {

    def writes(e: Employee): JsValue = {
      Json.obj(
        "id" -> e.id,
        "first" -> e.first,
        "last" -> e.last,
        "email" -> e.email,
        "phone" -> e.phone,
        "role" -> e.role,
        "website" -> e.website,
        "bio" -> e.bio,
        "html_url" -> controllers.routes.Employees.show(e.id.get).url,
        "gravatar_id" -> new Gravatar(e.email).hash
      )
    }
  }

  implicit val employeeReads  = Json.reads[Employee]

  val employee = {
    get[Long]("id")~
    get[String]("first_name")~
    get[String]("last_name")~
    get[String]("email")~
    get[String]("phone")~
    get[Int]("role_id")~
    get[String]("website")~
    get[String]("bio") map {
      case (id~first~last~email~phone~role~website~bio) => {
        val roleName = getRoleName(role)
        Employee(Some(id), first, last, email, phone, roleName, website, bio)
      }
    }
  }

  def getRoleName(role: Long) =
    Role.findById(role) match {
      case None => "None"
      case Some(r) => r.role
    }

  def getRoleId(role: String) =
    Role.findByName(role) match {
      case None => "None"
      case Some(r) => r.id
    }

  def gravatar(email: String, size: Int) =
    new Gravatar(email).url(size)

  /**
   * Build Employee without ID probably a much better way to do this
   *
   * Use apply_unapply or something
   *
   */
  def build(e: (String,String,String,String,Int,String,String)): Employee = {
    val roleName = getRoleName(e._5)
    Employee(None, e._1, e._2, e._3, e._4, roleName, e._6, e._7)
  }

  def name(e: Employee): String = e.first + " " + e.last

  def all(): List[Employee] = DB.withConnection { implicit c =>
    SQL("""select * from employees""").as(employee *)
  }

  def groupByRole(): Map[String, List[Employee]] = all.groupBy(_.role)

  def recent(): List[Employee] = DB.withConnection { implicit c =>
    SQL("select * from employees order by id desc limit 5").as(employee *)
  }

  /**
   * Find an employee by a generic value
   *
   */
  def findByType[T](identifier: T, value: T): Option[Employee] = DB.withConnection { implicit c =>
    SQL("select * from employees where {identifier} = {value}").on(
      'identifier -> identifier,
      'value      -> value
    ).as(employee singleOpt)
  }

  def findById(id: Long): Option[Employee] = DB.withConnection { implicit c =>
    SQL("select * from employees where id = {id}").on(
      'id -> id
    ).as(employee singleOpt)
  }

  def create(employee: Employee): Unit = {
    DB.withConnection { implicit c =>
      SQL(
        """insert into employees (first_name, last_name, email, phone, role_id, website, bio)
           values ({first}, {last}, {email}, {phone}, {role}, {website}, {bio})"""
      ).on(
        'first   -> employee.first,
        'last    -> employee.last,
        'email   -> employee.email,
        'phone   -> employee.phone,
        'role    -> getRoleId(employee.role),
        'website -> employee.website,
        'bio     -> employee.bio
      ).executeUpdate()
    }
  }

  /**
   * Update an existing employee record
   *
   */
  def update(id: Long, employee: Employee): Unit = {
    DB.withConnection { implicit c =>
      SQL("""update employees set first_name = {first}, last_name = {last}, email = {email},
             phone = {phone}, role_id = {role}, website = {website}, bio = {bio} where id = {id}""").on(
        'first   -> employee.first,
        'last    -> employee.last,
        'email   -> employee.email,
        'phone   -> employee.phone,
        'role    -> getRoleId(employee.role),
        'website -> employee.website,
        'bio     -> employee.bio,
        'id      -> id
      ).executeUpdate()
    }
  }

  def delete(id: Long): Unit = {
    DB.withConnection { implicit c =>
      SQL("delete from employees where id = {id}").on(
      'id -> id
      ).executeUpdate()
    }
  }

  /**
   * Finds all employees with a given role
   *
   */
  def byRole(role_id: Long): List[Employee] = {
    DB.withConnection { implicit c =>
      SQL("select * from employees where role_id = {role_id}").on(
        'role_id -> role_id
      ).as(employee *)
    }
  }

  /**
   * A simple search for employees by name
   *
   */
  def search(query: String): List[Employee] = {
    DB.withConnection { implicit c =>
      val sql = "select * from employees where lower(first_name) like '%" +
                 query + "%'" + "or lower(last_name) like '%" + query + "%'"
      SQL(sql).as(employee *)
    }
  }
}
