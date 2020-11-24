package facebook.killer.http.data

final case class JsonError(message: String)

final case class JsonSuccess(message: Option[String])
