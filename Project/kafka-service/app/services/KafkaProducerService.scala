package services

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import javax.inject._
import org.apache.kafka.clients.producer.{ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class KafkaProducerService @Inject()(implicit system: ActorSystem, ec: ExecutionContext, mat: Materializer) {
  private val kafkaProducerSettings: ProducerSettings[String, String] =
    ProducerSettings(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers("34.47.143.23:9092")
      .withProperty(ProducerConfig.ACKS_CONFIG, "all")

  def sendMessages(messages: Seq[ProducerRecord[String, String]]): Future[Unit] = {
    Source(messages.toList)
      .runWith(Producer.plainSink(kafkaProducerSettings))
      .map(_ => ())
  }
}
