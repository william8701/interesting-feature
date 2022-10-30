package repositories

import com.google.inject.ImplementedBy
import models.Entities.UserEntity

import javax.inject.Singleton
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[InMemoryUserRepository])
trait UserRepository {

  def create(data: UserEntity): Future[Int]

  def list(): Future[Seq[UserEntity]]

  def get(id: Int): Future[Option[UserEntity]]

  def findByEmail(email: String): Future[Option[UserEntity]]

}

@Singleton
class InMemoryUserRepository extends UserRepository {

  val users = new ListBuffer[UserEntity]()
  users.addOne(UserEntity(Some(1), "user1@email.com"))
  users.addOne(UserEntity(Some(2), "user2@email.com"))
  users.addOne(UserEntity(Some(3), "user3@email.com"))

  override def create(data: UserEntity): Future[Int] = {
    val nextId = users.size + 1
    users.addOne(UserEntity(Some(nextId), data.email))
    Future.successful(nextId)
  }

  override def list(): Future[Seq[UserEntity]] = Future(users.toSeq)

  override def get(id: Int): Future[Option[UserEntity]] = Future(users.find(u => u.id.get.equals(id)))

  override def findByEmail(email: String): Future[Option[UserEntity]] = Future(users.find(u => u.email.equals(email)))

}
