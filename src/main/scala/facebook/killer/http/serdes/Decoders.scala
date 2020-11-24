package facebook.killer.http.serdes

import facebook.killer.data.Models._
import io.circe.Decoder
import io.circe.generic.semiauto._

object Decoders extends JsonDecoders

trait JsonDecoders {
  implicit val userJsonDecoder: Decoder[User] =
    deriveDecoder[User]

  implicit val newUserJsonDecoder: Decoder[NewUser] =
    deriveDecoder[NewUser]

  implicit val interestJsonDecoder: Decoder[Interest] =
    deriveDecoder[Interest]

  implicit val newInterestJsonDecoder: Decoder[NewInterest] =
    deriveDecoder[NewInterest]

  implicit val subscriptionJsonDecoder: Decoder[Subscription] =
    deriveDecoder[Subscription]

  implicit val newSubscriptionJsonDecoder: Decoder[NewSubscription] =
    deriveDecoder[NewSubscription]

  implicit val interestOfUserJsonDecoder: Decoder[InterestOfUser] =
    deriveDecoder[InterestOfUser]

  implicit val newInterestOfUserJsonDecoder: Decoder[NewInterestOfUser] =
    deriveDecoder[NewInterestOfUser]

  implicit val groupJsonDecoder: Decoder[Group] =
    deriveDecoder[Group]

  implicit val newGroupJsonDecoder: Decoder[NewGroup] =
    deriveDecoder[NewGroup]
}
