package entities

case class Company(id: String, name: String, createdAt: DateTime, updatedAt: DateTime) extends SortableTrait
