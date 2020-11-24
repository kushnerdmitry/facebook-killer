package facebook.killer

import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO}
import facebook.killer.data.Models.{NewSubscription, NewUser, Subscription, User}
import io.circe.Json
import org.http4s.Uri.{Authority, RegName}
import org.http4s._
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.client.blaze._
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class UsersIntegrationTests
  extends AnyFunSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience {

  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(3))

  implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)

  private val (httpClient, _) = BlazeClientBuilder[IO](ec).allocated.unsafeRunSync()

  import facebook.killer.http.serdes.Decoders._
  import facebook.killer.http.serdes.Encoders._

  describe("users endpoints"){
    it("should correctly select user from migrations"){
      httpClient.expect[Seq[User]](uri()).unsafeRunSync() should contain (User(id = 1, "Дмитрий", "Кушнер", 21))
    }

    it("should do CRD properly"){
      val test = for {
        usersBefore <- httpClient.expect[Seq[User]](uri())
        newUser <- httpClient.expect[User](Request[IO](Method.POST, uri()).withEntity(NewUser("TestUserName", "TestUserSecondName", 22)))
        usersAfter <- httpClient.expect[Seq[User]](uri())
        _ = usersAfter.size shouldBe usersBefore.size + 1
        _ = usersAfter should contain(newUser)
        _ <- httpClient.expect[Json](Request[IO](Method.DELETE, uri(Some(newUser.id.toString))))
        retrieveStatus <- httpClient.status(Request[IO](Method.GET, uri(Some(newUser.id.toString))))
        _ = retrieveStatus shouldBe Status.NotFound
      } yield ()

      test.unsafeRunSync()
    }

    val testUserId = 1

    it("should list user friends properly") {
      httpClient.expect[Seq[Long]](uri(Some(s"friends/$testUserId"))).unsafeRunSync() shouldBe Seq(2, 3)
    }

    it("should list user subscriptions properly") {
      httpClient.expect[Seq[Long]](uri(Some(s"subscriptions/users/$testUserId"))).unsafeRunSync() shouldBe Seq(2, 3, 12)
      httpClient.expect[Seq[Long]](uri(Some(s"subscriptions/groups/$testUserId"))).unsafeRunSync() shouldBe Seq(1, 5)

    }

    it("should list user subscribers properly") {
      httpClient.expect[Seq[Long]](uri(Some(s"subscribers/$testUserId"))).unsafeRunSync() shouldBe Seq(2, 3, 4, 6)
    }

    it("should subscribe users properly") {
      val test = for {
        _ <- httpClient.expect[Subscription](Request[IO](Method.POST, uri(Some("subscribe/user"))).withEntity(NewSubscription(testUserId, 9)))
        subscriptionsToUsers <- httpClient.expect[Seq[Long]](uri(Some(s"subscriptions/users/$testUserId")))
        _ = subscriptionsToUsers should contain (9)

        _ <- httpClient.expect[Subscription](Request[IO](Method.POST, uri(Some("subscribe/group"))).withEntity(NewSubscription(testUserId, 2)))
        subscriptionsToUsers <- httpClient.expect[Seq[Long]](uri(Some(s"subscriptions/groups/$testUserId")))
        _ = subscriptionsToUsers should contain (2)
      } yield ()

      test.unsafeRunSync()
    }

  }

  private def uri(pathOpt: Option[String] = None): Uri = {
    val path = "/api/users" + pathOpt.map("/" + _).getOrElse("")

    Uri(authority = Some(Authority(host = RegName("0.0.0.0"))), path = path)
  }

}

