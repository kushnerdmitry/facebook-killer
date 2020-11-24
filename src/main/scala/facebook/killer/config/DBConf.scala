package facebook.killer.config

import java.net.URI

final case class DBConf(
    host: String,
    port: Int,
    name: String,
    user: String,
    password: String
) {

  def driverClassName: String = "org.postgresql.Driver"

  def jdbcUrl: URI = URI.create(s"jdbc:postgresql://$host:$port/$name")

}
