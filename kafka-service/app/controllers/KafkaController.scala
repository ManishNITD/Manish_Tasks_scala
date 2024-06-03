package controllers

import javax.inject._
import play.api.mvc._
import services.KafkaProducerService
import scala.concurrent.ExecutionContext
import org.apache.kafka.clients.producer.ProducerRecord
import controllers.JsonFormats._

@Singleton
class KafkaController @Inject()(cc: ControllerComponents, kafkaProducerService: KafkaProducerService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def sendMessages: Action[AnyContent] = Action.async { implicit request =>
    val messageData = request.body.asJson.get
    val messages = (messageData \ "messages").as[Seq[ProducerRecord[String, String]]]
    kafkaProducerService.sendMessages(messages).map(_ => Ok("Messages sent"))
  }
}
