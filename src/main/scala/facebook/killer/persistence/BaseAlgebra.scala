package facebook.killer.persistence

import cats.effect.Effect
import facebook.killer.data.Models.{Model, NewModel}

abstract class BaseAlgebra[F[_]: Effect, M <: Model, N <: NewModel] {
  def get(id: Long): F[Option[M]]

  def list(): F[Seq[M]]

  def insert(entity: N): F[Either[String, M]]

  def delete(id: Long): F[Int]
}
