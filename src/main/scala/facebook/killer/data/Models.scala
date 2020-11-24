package facebook.killer.data

object Models {

  trait Model

  trait NewModel

  final case class NewUser(
      firstName: String,
      secondName: String,
      age: Int
  ) extends NewModel {
    def toUser(id: Long): User = User(id, firstName, secondName, age)
  }

  case class User(
      id: Long,
      firstName: String,
      secondName: String,
      age: Int
  ) extends Model

  final case class Interest(
      id: Long,
      name: String
  ) extends Model

  final case class NewInterest(
      name: String
  ) extends NewModel {
    def toInterest(id: Long): Interest = Interest(id, name)
  }

  final case class InterestOfUser(
      id: Long,
      userId: Long,
      interestId: Long,
      estimate: Int
  ) extends Model

  final case class NewInterestOfUser(
      userId: Long,
      interestId: Long,
      estimate: Int
  ) extends NewModel {
    def toInterestOfUser(id: Long): InterestOfUser =
      InterestOfUser(id, userId, interestId, estimate)
  }

  final case class Subscription(
      id: Long,
      from: Long,
      to: Long
  ) extends Model

  final case class NewSubscription(
      from: Long,
      to: Long
  ) extends NewModel {
    def toSubscription(id: Long): Subscription =
      Subscription(id, from, to)
  }

  final case class Group(
      id: Long,
      name: String,
      description: String,
      admin: Long
  ) extends Model

  final case class NewGroup(
      name: String,
      description: String,
      admin: Long
  ) extends NewModel {
    def toGroup(id: Long): Group =
      Group(id, name, description, admin)
  }

}
