package facebook.killer.http.services

import cats.effect.IO
import org.http4s.Method.GET
import org.http4s.dsl.io.{Root, _}
import org.http4s.headers.Location
import org.http4s.{HttpRoutes, Uri}


object Index {
  private val swaggerPath = "/assets/swagger-ui/3.36.1/index.html?url=/api/swagger.json"

  val service: HttpRoutes[IO] = HttpRoutes.of[IO]{
    case GET -> Root =>
      TemporaryRedirect(
        Location(Uri.unsafeFromString(swaggerPath))
      )
  }
}
