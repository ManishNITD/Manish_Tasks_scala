package com.example

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem(Behaviors.empty, "S3UploaderSystem")
    val s3Uploader = new S3Uploader(system)
    s3Uploader.uploadFile("/Users/manishawasthi/Documents/Manish_Tasks_scala/s3_file_akka/hellos3.txt", "s3-manishnitd", "hellofrommanish.txt")
  }
}
