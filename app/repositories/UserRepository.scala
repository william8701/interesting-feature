package repositories

import models.Models.UserData

import scala.concurrent.Future

trait UserRepository {
  def create(data: UserData): Future[Int]

  def list(): Future[Seq[UserData]]

  def get(id: Int): Future[Option[UserData]]

}

class UserRepositoryImpl extends UserRepository {
  override def create(data: UserData): Future[Int] = ???

  override def list(): Future[Seq[UserData]] = ???

  override def get(id: Int): Future[Option[UserData]] = ???
}
