package services

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import javax.inject._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import play.api.libs.json.Json

import java.sql.{Connection, DriverManager, PreparedStatement}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class KafkaConsumer @Inject()(implicit system: ActorSystem, mat: Materializer) {

  private val kafkaConsumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers("34.47.143.23:9092")
    .withGroupId("library")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")

  private val dbUrl = "jdbc:mysql://35.244.19.79:3306/manish"
  private val dbUser = "root"
  private val dbPassword = "Password@12345"

  Consumer
    .plainSource(kafkaConsumerSettings, Subscriptions.topics("book-topic"))
    .mapAsync(1) { msg =>
      Future {
        val json = Json.parse(msg.value())
        val bookId = (json \ "id").as[Long]
        val assigned = (json \ "assigned").as[Boolean]

        var connection: Connection = null
        var preparedStatement: PreparedStatement = null

        try {

          connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)

          val updateSql = "UPDATE books SET assigned_status = ? WHERE id = ?"

          preparedStatement = connection.prepareStatement(updateSql)
          preparedStatement.setBoolean(1, assigned)
          preparedStatement.setLong(2, bookId)

          preparedStatement.executeUpdate()
        } catch {
          case e: Exception =>
            e.printStackTrace()
        } finally {

          if (preparedStatement != null) preparedStatement.close()
          if (connection != null) connection.close()
        }
      }
    }
    .runWith(Sink.ignore)
}
