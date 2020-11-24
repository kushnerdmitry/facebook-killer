package facebook.killer.http.services

import cats.effect.IO
import doobie.util.transactor.Transactor
import facebook.killer.data.Models.{NewSubscription, NewUser}
import facebook.killer.http.data.{JsonError, JsonSuccess}
import facebook.killer.persistence.services.UsersAlg
import org.http4s.circe.jsonOf
import org.http4s.rho.RhoRoutes

object Users extends Service {

  override val pathPrefix: String = "users"

  val idDescription = "User id"

  def service(transactor: Transactor[IO]): RhoRoutes[IO] = {
    val algebra = UsersAlg.doobie[IO](transactor)
    new RhoRoutes[IO] {

      import facebook.killer.http.serdes.Decoders._
      import facebook.killer.http.serdes.Encoders._
      import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
      import org.http4s.rho.swagger.syntax.io._

      "List all users" **
        GET / pathPrefix |>> { () =>
        algebra.list().flatMap(users => Ok(users))
      }

      "Get user information by id" **
        GET / pathPrefix / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.get(id).flatMap{
          case Some(user) => Ok(user)
          case _ => NotFound(JsonError(s"No user with id $id"))
        }
      }

      "Post new user" **
        POST / pathPrefix ^ jsonOf[IO, NewUser] |>> { (newUser: NewUser) =>
        algebra.insert(newUser).flatMap{
          case Right(tweet) => Created(tweet)
          case Left(insertErr) => BadRequest(JsonError(insertErr))
        }
      }

      "Delete user" **
        DELETE/ pathPrefix / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.delete(id).flatMap{
          case 1 => Ok(JsonSuccess(Some(s"Deleted user with id: $id")))
          case _ => BadRequest(JsonError("Update failed"))
        }
      }

      "Subscribe to user" **
        POST / pathPrefix / "subscribe" / "user" ^ jsonOf[IO, NewSubscription] |>> { (newSubscription: NewSubscription) =>
        if (newSubscription.from == newSubscription.to) {
          BadRequest(JsonError("Could not subscribe user on his own"))
        } else {
          algebra.subscribeToUser(newSubscription).flatMap{
            case Right(subscription) => Created(subscription)
            case Left(insertErr) => BadRequest(JsonError(insertErr))
          }
        }
      }

      "Subscribe to group" **
        POST / pathPrefix / "subscribe" / "group" ^ jsonOf[IO, NewSubscription] |>> { (newSubscription: NewSubscription) =>
        algebra.subscribeToGroup(newSubscription).flatMap{
          case Right(subscription) => Created(subscription)
          case Left(insertErr) => BadRequest(JsonError(insertErr))
        }
      }

      "List user subscriptions to users" **
        GET / pathPrefix / "subscriptions" / "users" / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.listUserToUsersSubscription(id).flatMap(users => Ok(users))
      }

      "List user subscriptions to groups" **
        GET / pathPrefix / "subscriptions" / "groups" / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.listUserToGroupsSubscription(id).flatMap(users => Ok(users))
      }

      "List user subscribers" **
        GET / pathPrefix / "subscribers" / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.listUserSubscribers(id).flatMap(userId => Ok(userId))
      }

      "List user friends" **
        GET / pathPrefix / "friends" / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.listUserFriends(id).flatMap(userId => Ok(userId))
      }
    }
  }

}
