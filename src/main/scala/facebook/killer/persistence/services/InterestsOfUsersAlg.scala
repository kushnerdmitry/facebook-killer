package facebook.killer.persistence.services

import cats.effect.Effect
import cats.implicits._
import doobie.implicits.{toSqlInterpolator, _}
import doobie.util.transactor.Transactor
import doobie.{Query0, Update0}
import facebook.killer.data.Models.{InterestOfUser, NewInterestOfUser}
import facebook.killer.persistence.BaseAlgebra

import scala.util.control.NonFatal

object InterestsOfUsersAlg {
  def doobie[F[_]: Effect](xa: Transactor[F]): DoobieInterestsOfUsersAlg[F] =
    new DoobieInterestsOfUsersAlg[F](xa)

}

object DoobieInterestsOfUsersAlg {
  private[persistence] def getInterestOfUserQuery(
      id: Long): Query0[InterestOfUser] = {
    sql"""
      SELECT i.id, i.user_id, i.interest_id, i.estimate
      FROM interests_of_users as i
      WHERE i.id = $id
      LIMIT 1
    """.query[InterestOfUser]
  }

  private[persistence] val listInterestsOfUsersQuery: Query0[InterestOfUser] = {
    sql"""
      SELECT i.id, i.user_id, i.interest_id, i.estimate
      FROM interests_of_users as i
    """.query[InterestOfUser]
  }

  private[persistence] def insertInterestOfUserQuery(
      newIoU: NewInterestOfUser): Update0 = {
    sql"""
       INSERT
       INTO interests_of_users (user_id, interest_id, estimate)
       VALUES (${newIoU.userId}, ${newIoU.interestId}, ${newIoU.estimate})
    """.update
  }

  private[persistence] def deleteInterestOfUserQuery(id: Long): Update0 = {
    sql"""
      DELETE FROM interests_of_users as i
      WHERE i.id = $id
    """.update
  }

  private[persistence] def listSameUsersInterestsQuery(
      user1: Long,
      user2: Long
  ): Query0[Long] = {
    sql"""
      SELECT i.interest_id FROM interests_of_users i
      WHERE i.user_id = $user1 AND i.estimate > 30 AND i.interest_id in
      (select iu.interest_id from interests_of_users iu where iu.user_id = $user2 and iu.estimate > 30)
    """.query[Long]
  }

}

class DoobieInterestsOfUsersAlg[F[_]: Effect](xa: Transactor[F])
    extends BaseAlgebra[F, InterestOfUser, NewInterestOfUser] {

  import DoobieInterestsOfUsersAlg._

  override def get(id: Long): F[Option[InterestOfUser]] =
    getInterestOfUserQuery(id).option.transact(xa)

  override def list(): F[Seq[InterestOfUser]] =
    listInterestsOfUsersQuery.to[List].transact(xa).map(_.toSeq)

  override def insert(
      newInterest: NewInterestOfUser
  ): F[Either[String, InterestOfUser]] = {
    insertInterestOfUserQuery(newInterest)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)
      .map(id =>
        Either.right[String, InterestOfUser](newInterest.toInterestOfUser(id)))
      .recover {
        case NonFatal(e) =>
          Either.left[String, InterestOfUser](e.toString)
      }
  }

  override def delete(id: Long): F[Int] =
    deleteInterestOfUserQuery(id).run.transact(xa)

  def listSameUsersInterests(user1: Long, user2: Long): F[Seq[Long]] = {
    listSameUsersInterestsQuery(user1, user2)
      .to[List]
      .transact(xa)
      .map(_.toSeq)
  }
}
