package services

import daos.BookDAO

import javax.inject._
import models.Book


import scala.concurrent.Future

@Singleton
class BookService @Inject()(bookDAO: BookDAO) {
  

  def allBooks(): Future[Seq[Book]] = bookDAO.all()

  def addBook(book: Book): Future[Unit] = bookDAO.insert(book)

  def deleteBook(id: Long): Future[Boolean] = bookDAO.delete(id)
}
