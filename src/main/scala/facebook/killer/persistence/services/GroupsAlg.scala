package facebook.killer.persistence.services

import cats.effect.Effect
import cats.implicits._
import doobie.implicits.{toSqlInterpolator, _}
import doobie.util.transactor.Transactor
import doobie.{Query0, Update0}
import facebook.killer.data.Models.{Group, NewGroup}
import facebook.killer.persistence.BaseAlgebra

import scala.util.control.NonFatal

object GroupsAlg {
  def doobie[F[_] : Effect](xa: Transactor[F]): DoobieGroupsAlg[F] =
    new DoobieGroupsAlg[F](xa)

}

object DoobieGroupsAlg {
  private[persistence] def getGroupQuery(id: Long): Query0[Group] = {
    sql"""
      SELECT g.id, g.name, g.description, g.admin
      FROM groups as g
      WHERE g.id = $id
      LIMIT 1
    """.query[Group]
  }

  private[persistence] val listGroupsQuery: Query0[Group] = {
    sql"""
      SELECT g.id, g.name, g.description, g.admin
      FROM groups as g
    """.query[Group]
  }

  private[persistence] def insertGroupQuery(newGroup: NewGroup): Update0 = {
    sql"""
      INSERT
      INTO groups (name, description, admin)
      VALUES (${newGroup.name}, ${newGroup.description}, ${newGroup.admin})
    """.update
  }

  private[persistence] def deleteGroupQuery(id: Long): Update0 = {
    sql"""
      DELETE FROM groups as g
      WHERE g.id = $id
    """.update
  }

  private[persistence] def listSubscribersQuery(id: Long): Query0[Long] = {
    sql"""
      SELECT g.user_from_id
      FROM groups_subscriptions as g
      WHERE g.group_to_id = $id
    """.query[Long]
  }
}

class DoobieGroupsAlg[F[_] : Effect](xa: Transactor[F])
  extends BaseAlgebra[F, Group, NewGroup] {

  import DoobieGroupsAlg._

  override def get(id: Long): F[Option[Group]] =
    getGroupQuery(id).option.transact(xa)

  override def list(): F[Seq[Group]] =
    listGroupsQuery.to[List].transact(xa).map(_.toSeq)

  override def insert(newGroup: NewGroup): F[Either[String, Group]] = {
    insertGroupQuery(newGroup)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)
      .map(id => Either.right[String, Group](newGroup.toGroup(id)))
      .recover{
        case NonFatal(e) =>
          Either.left[String, Group](e.toString)
      }
  }

  override def delete(id: Long): F[Int] =
    deleteGroupQuery(id).run.transact(xa)

  def listSubscribers(id: Long): F[Seq[Long]] =
    listSubscribersQuery(id)
      .to[List]
      .transact(xa)
      .map(_.toSeq)
}
