package zio.metrics

import zio.clock.Clock
import zio.console._
import zio.metrics.encoders._
import zio.{ Chunk, RIO, Runtime, Task }

object ClientTest {

  val rt = Runtime.unsafeFromLayer(Encoder.statsd ++ Console.live ++ Clock.live)

  val myudp: Chunk[Metric] => RIO[Encoder with Console, Chunk[Int]] = msgs =>
    for {
      sde <- RIO.environment[Encoder]
      opt <- RIO.foreach(msgs)(sde.get.encode(_))
      _   <- putStrLn(s"udp: $opt")
      l   <- RIO.foreach(opt.collect { case Some(msg) => msg })(s => UDPClient().use(_.send(s)))
    } yield l

  val program = {
    val messages = List(1.0, 2.2, 3.4, 4.6, 5.1, 6.0, 7.9)
    val createClient = Client.withListener[Chunk, Int] { l: Chunk[Metric] =>
      myudp(l).provideSomeLayer[Encoder](Console.live)
    }
    createClient.use { client =>
      for {
        opt <- RIO.foreach(messages)(d => Task(Counter("clientbar", d, 1.0, Seq.empty[Tag])))
        _   <- RIO.foreach(opt)(m => client.sendAsync(m))
      } yield ()
    }
  }

  def main(args: Array[String]): Unit =
    rt.unsafeRun(program *> putStrLn("Bye bye").provideSomeLayer(Console.live))

}
