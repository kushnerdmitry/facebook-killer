package facebook.killer.http.services

import cats.effect.IO
import doobie.util.transactor.Transactor
import facebook.killer.data.Models.NewInterestOfUser
import facebook.killer.http.data.{JsonError, JsonSuccess}
import facebook.killer.persistence.services.InterestsOfUsersAlg
import org.http4s.circe.jsonOf
import org.http4s.rho.RhoRoutes

object InterestsOfUsers extends Service {

  override val pathPrefix: String = "interests-of-users"

  val idDescription = "Interest of user id"

  def service(transactor: Transactor[IO]): RhoRoutes[IO] = {
    val algebra = InterestsOfUsersAlg.doobie[IO](transactor)
    new RhoRoutes[IO] {

      import facebook.killer.http.serdes.Decoders._
      import facebook.killer.http.serdes.Encoders._
      import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
      import org.http4s.rho.swagger.syntax.io._

      "List all interests of users" **
        GET / pathPrefix |>> { () =>
        algebra.list().flatMap(interests => Ok(interests))
      }

      "Get interest of user" **
        GET / pathPrefix / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.get(id).flatMap{
          case Some(interest) => Ok(interest)
          case _ => NotFound(JsonError(s"No interest with id $id"))
        }
      }

      "Post new interest of user" **
        POST / pathPrefix ^ jsonOf[IO, NewInterestOfUser] |>> { (newInterest: NewInterestOfUser) =>
        algebra.insert(newInterest).flatMap{
          case Right(interest) => Created(interest)
          case Left(insertErr) => BadRequest(insertErr)
        }
      }

      "Delete interest of user" **
        DELETE / pathPrefix / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.delete(id).flatMap{
          case 1 => Ok(JsonSuccess(Some(s"Deleted interest with id: $id")))
          case _ => BadRequest(JsonError("Update failed"))
        }
      }

      "List same users interests(when interest`s estimate is greater then 30 for both users)" **
        GET / pathPrefix / "similar" / pathVar[Long]("user1", "User 1 id") / pathVar[Long]("user2", "User 2 id") |>> { (user1: Long, user2: Long) =>
          algebra.listSameUsersInterests(user1, user2).flatMap(id => Ok(id))
      }
    }
  }

}
