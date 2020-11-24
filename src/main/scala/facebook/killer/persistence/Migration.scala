package facebook.killer.persistence

import cats.effect.IO
import facebook.killer.config.DBConf
import org.flywaydb.core.Flyway

object Migration {

  final case class Success(migrationsRun: Int) extends AnyVal

  final case class Failure(underlying: Throwable) extends AnyVal

  def withConfig(dbConfig: DBConf): IO[Success] = IO {
    val dataSource = HikariOps.toDataSource(dbConfig)

    val migrationsRun = Flyway
      .configure()
      .dataSource(dataSource)
      .load()
      .migrate()

    dataSource.close()
    Success(migrationsRun)
  }
}
