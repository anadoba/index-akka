import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{ActorPath, ActorRef, Props, Actor}
import akka.actor.Actor.Receive

/**
 * Created by Adam on 2015-02-05.
 */

class MainActor extends Actor {
  import Messages._

/*
aktor glowny czyta tekst i zbiera dane od tych potomnych
*/
var lineCounter: AtomicInteger = new AtomicInteger(0);
var actors: List[ActorRef] = List[ActorRef]()


  override def receive = StartIndexing()

  def StartIndexing(): Receive = {
    case Start(filename) =>
      val txtfile = scala.io.Source.fromFile(filename, "UTF-8")
      // val words = txtfile.getLines.flatMap(_.split("\\W+")).toList


      for (line <- txtfile.getLines) {
        //println(line.split("\\W+").toList)
        val wordsInLine = line.split("\\W+").toList

        val resultFromLine = for (word <- wordsInLine) yield word.toLowerCase

        for (word <- resultFromLine) {
          lineCounter.incrementAndGet()

          // if actor doesnt exist then add it
          if (context.actorFor(ActorPath.fromString("akka://system/user/MainActor/" + word)) == null) {
            val counter = context.actorOf(Props[Counter], word)
            counter ! InitCounter(word, lineCounter.get())
            println("///////////////\nactor created for: " + word)
            actors :+ counter
          }
          // if it exists then add occurence
          else {
            val existingActor = context.actorFor(ActorPath.fromString("akka://system/user/MainActor/" + word))
            existingActor ! WordFound(lineCounter.get())
            println(existingActor.path)

          }
        }
      }

      txtfile.close()

      // request Results from all actors
      for (actor <- actors)
        actor ! GetResults

      // wait for upcoming results
      context.become(WaitingForResults())
  }

  def WaitingForResults(): Receive = {
    case Result(pair) =>
      context.become(WaitingForMore(pair))
  }

  def WaitingForMore(results: List[(String, List[Int])] ): Receive = {
    case Result(pair) =>
      context.become(WaitingForMore(results ++ pair))
  }
}

/// aktor czyta tekst
// i zbiera od tych potomnych

// glowny aktor wysyla numery linii do odpowiednich aktorow (gdzie dane slowo sie znajduje