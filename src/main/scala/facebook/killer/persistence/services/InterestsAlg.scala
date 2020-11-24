package facebook.killer.persistence.services

import cats.effect.Effect
import cats.implicits._
import doobie.implicits.{toSqlInterpolator, _}
import doobie.util.transactor.Transactor
import doobie.{Query0, Update0}
import facebook.killer.data.Models.{Interest, NewInterest}
import facebook.killer.persistence.BaseAlgebra

import scala.util.control.NonFatal

object InterestsAlg {
  def doobie[F[_]: Effect](xa: Transactor[F]): DoobieInterestsAlg[F] =
    new DoobieInterestsAlg[F](xa)

}

object DoobieInterestsAlg {
  private[persistence] def getInterestQuery(id: Long): Query0[Interest] = {
    sql"""
      SELECT i.id, i.name
      FROM interests as i
      WHERE i.id = $id
      LIMIT 1
    """.query[Interest]
  }

  private[persistence] val listInterestsQuery: Query0[Interest] = {
    sql"""
      SELECT i.id, i.name
      FROM interests as i
    """.query[Interest]
  }

  private[persistence] def insertInterestQuery(
      newInterest: NewInterest): Update0 = {
    sql"""
      INSERT
      INTO interests (name)
      VALUES (${newInterest.name})
    """.update
  }

  private[persistence] def deleteInterestQuery(id: Long): Update0 = {
    sql"""
      DELETE FROM interests as i
      WHERE i.id = $id
    """.update
  }
}

class DoobieInterestsAlg[F[_]: Effect](xa: Transactor[F])
    extends BaseAlgebra[F, Interest, NewInterest] {

  import DoobieInterestsAlg._

  override def get(id: Long): F[Option[Interest]] =
    getInterestQuery(id).option.transact(xa)

  override def list(): F[Seq[Interest]] =
    listInterestsQuery.to[List].transact(xa).map(_.toSeq)

  override def insert(newInterest: NewInterest): F[Either[String, Interest]] = {
    insertInterestQuery(newInterest)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)
      .map(id => Either.right[String, Interest](newInterest.toInterest(id)))
      .recover {
        case NonFatal(e) =>
          Either.left[String, Interest](e.toString)
      }
  }

  override def delete(id: Long): F[Int] =
    deleteInterestQuery(id).run.transact(xa)
}
