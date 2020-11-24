package facebook.killer.persistence.services

import cats.effect.Effect
import cats.implicits._
import doobie.implicits.{toSqlInterpolator, _}
import doobie.util.transactor.Transactor
import doobie.{Query0, Update0}
import facebook.killer.data.Models._
import facebook.killer.persistence.BaseAlgebra

import scala.util.control.NonFatal

object UsersAlg {
  def doobie[F[_] : Effect](xa: Transactor[F]): DoobieUsersAlg[F] =
    new DoobieUsersAlg[F](xa)

}

object DoobieUsersAlg {
  private[persistence] def getUserQuery(id: Long): Query0[User] = {
    sql"""
      SELECT u.id, u.first_name, u.second_name, u.age
      FROM users as u
      WHERE u.id = $id
      LIMIT 1
    """.query[User]
  }

  private[persistence] val listUsersQuery: Query0[User] = {
    sql"""
      SELECT u.id, u.first_name, u.second_name, u.age
      FROM users as u
    """.query[User]
  }

  private[persistence] def insertUserQuery(newUser: NewUser): Update0 = {
    sql"""
      INSERT
      INTO users (first_name, second_name, age)
      VALUES (${newUser.firstName}, ${newUser.secondName}, ${newUser.age})
    """.update
  }

  private[persistence] def deleteUserQuery(id: Long): Update0 = {
    sql"""
      DELETE FROM users as u
      WHERE u.id = $id
    """.update
  }

  private[persistence] def subscribeToUserQuery(newSubscription: NewSubscription): Update0 = {
    sql"""
      INSERT
      INTO users_subscriptions (user_from_id, user_to_id)
      VALUES (${newSubscription.from}, ${newSubscription.to})
    """.update
  }

  private[persistence] def subscribeToGroupQuery(newSubscription: NewSubscription): Update0 = {
    sql"""
      INSERT
      INTO groups_subscriptions (user_from_id, group_to_id)
      VALUES (${newSubscription.from}, ${newSubscription.to})
    """.update
  }

  private[persistence] def listUserToUsersSubscriptionsQuery(id: Long): Query0[Long] = {
    sql"""
      SELECT u.user_to_id
      FROM users_subscriptions as u
      WHERE u.user_from_id = $id
    """.query[Long]
  }

  private[persistence] def listUserToGroupsSubscriptionsQuery(id: Long): Query0[Long] = {
    sql"""
      SELECT g.group_to_id
      FROM groups_subscriptions as g
      WHERE g.user_from_id = $id
    """.query[Long]
  }

  private[persistence] def listUserSubscribersQuery(id: Long): Query0[Long] = {
    sql"""
      SELECT u.user_from_id
      FROM users_subscriptions as u
      WHERE u.user_to_id = $id
    """.query[Long]
  }

  private[persistence] def listUserFriendsQuery(id: Long): Query0[Long] = {
    sql"""
      SELECT u.user_to_id
      FROM users_subscriptions as u
      WHERE u.user_from_id = $id AND u.user_to_id IN
      (select us.user_from_id from users_subscriptions us where us.user_to_id = $id)
    """.query[Long]
  }

}

class DoobieUsersAlg[F[_] : Effect](xa: Transactor[F])
  extends BaseAlgebra[F, User, NewUser] {

  import DoobieUsersAlg._

  private def listQueryToSeq[T](query: Query0[T]): F[Seq[T]] = {
    query
      .to[List]
      .transact(xa)
      .map(_.toSeq)
  }

  override def get(id: Long): F[Option[User]] =
    getUserQuery(id).option.transact(xa)

  override def list(): F[Seq[User]] =
    listQueryToSeq(listUsersQuery)

  override def insert(newUser: NewUser): F[Either[String, User]] = {
    insertUserQuery(newUser)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)
      .map(id => Either.right[String, User](newUser.toUser(id)))
      .recover{
        case NonFatal(e) =>
          Either.left[String, User](e.toString)
      }
  }

  override def delete(id: Long): F[Int] = deleteUserQuery(id).run.transact(xa)

  private def subscribe(
    newSubscription: NewSubscription,
    query: NewSubscription => Update0
  ): F[Either[String, Subscription]] = {
    query(newSubscription)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)
      .map(id =>
        Either.right[String, Subscription](newSubscription.toSubscription(id)))
      .recover{
        case NonFatal(e) =>
          Either.left[String, Subscription](e.toString)
      }
  }

  def subscribeToUser(
    newSubscription: NewSubscription
  ): F[Either[String, Subscription]] = {
    subscribe(newSubscription, subscribeToUserQuery)
  }

  def subscribeToGroup(
    newSubscription: NewSubscription
  ): F[Either[String, Subscription]] = {
    subscribe(newSubscription, subscribeToGroupQuery)
  }

  def listUserToUsersSubscription(id: Long): F[Seq[Long]] = {
    listQueryToSeq(listUserToUsersSubscriptionsQuery(id))
  }

  def listUserToGroupsSubscription(id: Long): F[Seq[Long]] = {
    listQueryToSeq(listUserToGroupsSubscriptionsQuery(id))
  }

  def listUserSubscribers(id: Long): F[Seq[Long]] = {
    listQueryToSeq(listUserSubscribersQuery(id))
  }

  def listUserFriends(id: Long): F[Seq[Long]] = {
    listQueryToSeq(listUserFriendsQuery(id))
  }
}
