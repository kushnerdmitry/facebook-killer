package facebook.killer

import java.util.concurrent.Executors

import cats.data.Kleisli
import cats.effect.{Blocker, ExitCode, IO, IOApp}
import doobie.hikari.HikariTransactor
import facebook.killer.config.AppConf
import facebook.killer.http.services._
import facebook.killer.persistence.{HikariOps, Migration}
import org.http4s.implicits._
import org.http4s.rho.RhoMiddleware
import org.http4s.rho.swagger.SwaggerMetadata
import org.http4s.rho.swagger.models.Info
import org.http4s.rho.swagger.syntax.{io => ioSwagger}
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.http4s.server.staticcontent.{MemoryCache, WebjarService, webjarService}
import org.http4s.{HttpRoutes, Request, Response}

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val blockingPool = Executors.newFixedThreadPool(2) //for UI
    val blocker = Blocker.liftExecutorService(blockingPool)

    buildServer(blocker).flatMap{ client =>
      client.serve
            .compile
            .drain
            .as(ExitCode.Success)
    }
  }

  private val swaggerMiddleware: RhoMiddleware[IO] =
    ioSwagger.createRhoMiddleware(
      swaggerMetadata = SwaggerMetadata(
        basePath = Some("/api"),
        apiInfo = Info(title = "Facebook killer API", version = "1.0.0")
      )
    )

  private val SERVICES = Seq(
    Users,
    Interests,
    InterestsOfUsers,
    Groups
  )

  private def services(transactor: HikariTransactor[IO]): HttpRoutes[IO] = {
    val (head, tail) = SERVICES match {
      case h :: t => (h, t)
      case _ => throw new RuntimeException("no services were specified")
    }

    tail.foldLeft(head.service(transactor)){ (acc, service: Service) =>
      acc.and(service.service(transactor))
    }.toRoutes(swaggerMiddleware)
  }

  private def httpApp(blocker: Blocker, transactor: HikariTransactor[IO]): Kleisli[IO, Request[IO], Response[IO]] =
    Router(
      "/" -> Index.service,
      "/api" -> services(transactor),
      "/assets" -> webjarService[IO](
        WebjarService.Config(
          cacheStrategy = MemoryCache[IO],
          blocker = blocker
        )
      )
    ).orNotFound

  private def buildServer(blocker: Blocker): IO[BlazeServerBuilder[IO]] = {
    for {
      appConfigEither <- IO(AppConf.load())
      appConfig = appConfigEither match {
        case Right(c) =>
          c
        case Left(err) =>
          throw new IllegalStateException(
            s"Could not load config: ${err.toList.mkString("\n")}")
      }
      dbConfig = appConfig.db
      _ <- Migration.withConfig(dbConfig)
      transactor <- HikariOps.toTransactor(dbConfig)
    } yield
      BlazeServerBuilder[IO](global)
        .bindHttp(appConfig.server.httpPort, "0.0.0.0")
        .withHttpApp(httpApp(blocker, transactor))
  }

}
