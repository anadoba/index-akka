import akka.actor.Actor
import akka.actor.Actor.Receive

/**
 * Created by Adam on 2015-02-05.
 */
class Counter extends Actor {
  import Messages._

/*
kazdy aktor jest odpowiedzialny za jedno slowo i podaje wyzej numery wystapien
 */

  override def receive = init

  def init: Receive = {
    case InitCounter(word, line) =>
      context.become(countingForWord(word, List[Int](line)))
  }

  def countingForWord(word: String, occurences: List[Int]): Receive = {
    case WordFound(line) =>
      context.become(countingForWord(word, occurences :+ line))

    case GetResults =>
      sender() ! Result(List[(String,List[Int])]((word, occurences)))
  }
}
