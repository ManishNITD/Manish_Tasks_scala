package com.example

import akka.actor.typed.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{FileIO, Sink}
import akka.util.ByteString
import com.typesafe.config.ConfigFactory
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.PutObjectRequest

import java.nio.file.Paths
import scala.concurrent.{ExecutionContext, Future}

object Implicits {
  // Implicit conversion for Java CompletableFuture to Scala Future
  import scala.compat.java8.FutureConverters._

  implicit class RichJavaFuture[T](val javaFuture: java.util.concurrent.CompletableFuture[T]) extends AnyVal {
    def toScala: Future[T] = javaFuture.toScala
  }
}

class S3Uploader(system: ActorSystem[_]) {
  implicit val ec: ExecutionContext = system.executionContext
  implicit val materializer: Materializer = Materializer(system)

  private val config = ConfigFactory.load()
  private val accessKeyId = config.getString("aws.accessKeyId")
  private val secretAccessKey = config.getString("aws.secretAccessKey")
  private val region = config.getString("aws.region")

  private val s3Client = S3AsyncClient.builder()
    .region(Region.of(region))
    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
    .build()

  import Implicits._

  def uploadFile(filePath: String, bucketName: String, key: String): Future[Unit] = {
    val fileSource = FileIO.fromPath(Paths.get(filePath))
    val request = PutObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .build()

    val sink = Sink.fold[ByteString, ByteString](ByteString.empty)(_ ++ _)

    val resultFuture = fileSource.runWith(sink).flatMap { byteString =>
      s3Client.putObject(request, AsyncRequestBody.fromBytes(byteString.toArray)).toScala
    }

    resultFuture.map(_ => println(s"File uploaded to S3 with key: $key"))
  }
}
