package org.home.garbage

import org.home.garbage.Refactoring.Message

object Refactoring {
  type Message = String
}

trait MessageSender {
  def send(m: Message): Unit
}

trait MessageSerializer {
  def serialize(message: Message): Array[Byte]
}

object JsonMessageSerializer extends MessageSerializer {
  override def serialize(message: Message): Array[Byte] = ???
}

class MyApp(messageSender: MessageSender) {

  def doSomethingUseful(input: String): Unit = {

    //to something here
    val message: Message = ???
    messageSender.send(message)
  }
}

object Version0 {

  class RabbitMessageSender(serializer: MessageSerializer) extends MessageSender {

    override def send(message: Message): Unit = {
      val bytes = serializer.serialize(message)
      //send them over
    }
  }

  object AppFactory {
    def build(messageSerializer: MessageSerializer): MyApp =  {
      val messageSender = new RabbitMessageSender(messageSerializer)
      new MyApp(messageSender)
    }
  }

  val myApp = AppFactory.build(JsonMessageSerializer)

}

object Version1 {

  trait ByteArrayMessageSender {
    def send(message: Array[Byte]): Unit
  }

  class RabbitMessageSender() extends ByteArrayMessageSender {
    override def send(message: Array[Byte]): Unit = {
      //send them over
    }
  }

  class ActiveMQMessageSender() extends ByteArrayMessageSender {
    override def send(message: Array[Byte]): Unit = {
      //send them over
    }
  }

  class MessageSenderImpl(
      byteArrayMessageSender: ByteArrayMessageSender,
      messageSerializer: MessageSerializer) extends MessageSender {

    override def send(message: Message): Unit = {
      val bytes = messageSerializer.serialize(message)
      byteArrayMessageSender.send(bytes)
    }
  }

  object AppFactory {
    def build(
        byteArrayMessageSender: ByteArrayMessageSender,
        messageSerializer: MessageSerializer): MyApp =  {

      val messageSender = new MessageSenderImpl(byteArrayMessageSender, JsonMessageSerializer)
      new MyApp(messageSender)
    }
  }

}