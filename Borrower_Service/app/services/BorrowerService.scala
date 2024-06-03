package services

import daos.BorrowerDAO
import models.Borrower
import javax.inject._
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}
import org.apache.kafka.clients.producer.ProducerRecord

@Singleton
class BorrowerService @Inject()(borrowerDAO: BorrowerDAO, ws: WSClient, config: play.api.Configuration)(implicit ec: ExecutionContext) {

  private val kafkaServiceUrl = config.get[String]("kafka.service.url")

  def allBorrowers(): Future[Seq[Borrower]] = borrowerDAO.all()

  def addBorrower(borrower: Borrower): Future[Unit] = borrowerDAO.insert(borrower)

  private def sendKafkaMessages(messages: Seq[ProducerRecord[String, String]]): Future[Unit] = {
    val json = Json.obj("messages" -> messages.map(msg => Json.obj("topic" -> msg.topic(), "key" -> msg.key(), "value" -> msg.value())))
    ws.url(s"$kafkaServiceUrl/sendMessages").post(json).map(_ => ())
  }

  def assignBooks(borrowerId: Long, bookIds: Seq[Long]): Future[Unit] = {
    for {
      _ <- borrowerDAO.updateBooks(borrowerId, bookIds)
      _ <- sendKafkaMessages(bookIds.map(id => new ProducerRecord[String, String]("book-topic", s"$id", Json.stringify(Json.obj("id" -> id, "assigned" -> true)))))
    } yield ()
  }

  def unassignBooks(borrowerId: Long, bookIds: Seq[Long]): Future[Unit] = {
    for {
      _ <- borrowerDAO.unassignBooks(borrowerId, bookIds)
      _ <- sendKafkaMessages(bookIds.map(id => new ProducerRecord[String, String]("book-topic", s"$id", Json.stringify(Json.obj("id" -> id, "assigned" -> false)))))
    } yield ()
  }
}
