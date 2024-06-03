package controllers

import play.api.libs.json._
import org.apache.kafka.clients.producer.ProducerRecord

object JsonFormats {
  implicit val producerRecordReads: Reads[ProducerRecord[String, String]] = new Reads[ProducerRecord[String, String]] {
    def reads(json: JsValue): JsResult[ProducerRecord[String, String]] = for {
      topic <- (json \ "topic").validate[String]
      key <- (json \ "key").validate[String]
      value <- (json \ "value").validate[String]
    } yield new ProducerRecord[String, String](topic, key, value)
  }
}

