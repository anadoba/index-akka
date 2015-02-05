import akka.actor.{Props, ActorSystem}

/**
 * Created by Adam on 2015-02-05.
 */
object Main {
  import Messages._
  import Methods._

  def indeks(plik: String): List[(String, List[Int])] = {
    val sys = ActorSystem("system")
    val mainActor = sys.actorOf(Props[MainActor], "MainActor")

    mainActor ! Start(plik)

    List[(String, List[Int])]()
  }

  def main(args: Array[String]): Unit = {

    println(codeWord("adąś"))
    println(decodeWord("ada#s# \n------------"))

    indeks("test.txt")
  }
}
