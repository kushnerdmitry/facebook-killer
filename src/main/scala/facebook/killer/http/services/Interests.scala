package facebook.killer.http.services

import cats.effect.IO
import doobie.util.transactor.Transactor
import facebook.killer.data.Models.NewInterest
import facebook.killer.http.data.{JsonError, JsonSuccess}
import facebook.killer.persistence.services.InterestsAlg
import org.http4s.circe.jsonOf
import org.http4s.rho.RhoRoutes

object Interests extends Service {

  override val pathPrefix: String = "interests"

  val idDescription = "Interest id"

  def service(transactor: Transactor[IO]): RhoRoutes[IO] = {
    val algebra = InterestsAlg.doobie[IO](transactor)
    new RhoRoutes[IO] {

      import facebook.killer.http.serdes.Decoders._
      import facebook.killer.http.serdes.Encoders._
      import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
      import org.http4s.rho.swagger.syntax.io._

      "List all interests" **
        GET / pathPrefix |>> { () =>
        algebra.list().flatMap(interests => Ok(interests))
      }

      "Get interest name by id" **
        GET / pathPrefix / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.get(id).flatMap {
          case Some(interest) => Ok(interest)
          case _              => NotFound(JsonError(s"No interest with id $id"))
        }
      }

      "Post new interest" **
        POST / pathPrefix ^ jsonOf[IO, NewInterest] |>> { (newInterest: NewInterest) =>
        algebra.insert(newInterest).flatMap {
          case Right(interest) => Created(interest)
          case Left(insertErr) => BadRequest(insertErr)
        }
      }

      "Delete interest" **
        DELETE / pathPrefix / pathVar[Long]("id", idDescription) |>> { (id: Long) =>
        algebra.delete(id).flatMap {
          case 1 => Ok(JsonSuccess(Some(s"Deleted interest with id: $id")))
          case _ => BadRequest(JsonError("Update failed"))
        }
      }
    }
  }
}
