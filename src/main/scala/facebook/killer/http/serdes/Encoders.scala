package facebook.killer.http.serdes

import facebook.killer.data.Models._
import facebook.killer.http.data.{JsonSuccess, _}
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

object Encoders extends JsonEncoders

trait JsonEncoders {
  implicit val userJsonEncoder: Encoder[User] =
    deriveEncoder[User]

  implicit val newUserJsonEncoder: Encoder[NewUser] =
    deriveEncoder[NewUser]

  implicit val interestJsonEncoder: Encoder[Interest] =
    deriveEncoder[Interest]

  implicit val newInterestJsonEncoder: Encoder[NewInterest] =
    deriveEncoder[NewInterest]

  implicit val interestOfUserJsonEncoder: Encoder[InterestOfUser] =
    deriveEncoder[InterestOfUser]

  implicit val newInterestOfUserJsonEncoder: Encoder[NewInterestOfUser] =
    deriveEncoder[NewInterestOfUser]

  implicit val subscriptionJsonEncoder: Encoder[Subscription] =
    deriveEncoder[Subscription]

  implicit val newSubscriptionJsonEncoder: Encoder[NewSubscription] =
    deriveEncoder[NewSubscription]

  implicit val groupJsonEncoder: Encoder[Group] =
    deriveEncoder[Group]

  implicit val newGroupJsonEncoder: Encoder[NewGroup] =
    deriveEncoder[NewGroup]

  implicit val jsonErrorJsonEncoder: Encoder[JsonError] =
    deriveEncoder[JsonError]

  implicit val jsonSuccessJsonEncoder: Encoder[JsonSuccess] =
    deriveEncoder[JsonSuccess]

}
