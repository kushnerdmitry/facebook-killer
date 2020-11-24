package facebook.killer.http.services

import cats.effect.IO
import doobie.util.transactor.Transactor
import org.http4s.rho.RhoRoutes

trait Service {
  val pathPrefix: String

  val idDescription: String

  def service(transactor: Transactor[IO]): RhoRoutes[IO]
}
