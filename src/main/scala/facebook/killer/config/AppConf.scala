package facebook.killer.config

import pureconfig.error.ConfigReaderFailures
import pureconfig.syntax.ConfigReaderOps
import pureconfig.generic.auto._ //DO NOT REMOVE IT

final case class AppConf(server: ServerConf, db: DBConf, swagger: SwaggerConf)

object AppConf {

  import com.typesafe.config.ConfigFactory

  def load(): Either[ConfigReaderFailures, AppConf] =
    ConfigFactory.load.to[AppConf]

}
