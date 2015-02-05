import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import akka.actor._
import akka.actor.Actor.Receive
import akka.util.Timeout
import scala.util.Failure
import scala.util.Success

/**
 * Created by Adam on 2015-02-05.
 */

class MainActor extends Actor {
  import Methods._
  import Messages._
  implicit val system = context.system
  import system.dispatcher // pula wątków
/*
aktor glowny czyta tekst i zbiera dane od tych potomnych
*/
var lineCounter: AtomicInteger = new AtomicInteger(0)


  override def receive = StartIndexing()

  def StartIndexing(): Receive = {
    case Start(filename) =>
      val txtfile = scala.io.Source.fromFile(filename, "UTF-8")


      for (line <- txtfile.getLines) {
        //println(line.split("\\W+").toList)
        //val wordsInLine = line.split("[a-zA-Ząćęłńóśźż]+").toList
        val wordsInLine = line.split("\\W+").toList
        val resultFromLine = for (word <- wordsInLine) yield word.toLowerCase

        for (word <- resultFromLine) {

           // Timeout for the resolveOne call
          implicit val timeout = Timeout(3, TimeUnit.SECONDS)

          system.actorSelection("akka://system/user/MainActor/" + codeWord(word)).resolveOne().onComplete {

            case Success(actor) =>
              // if it exists then add occurence
              actor ! WordFound(lineCounter.get())
              println("Actor exists for: " + actor.path)

            case Failure(ex) =>
              // if actor doesnt exist then add it
              val countingActor = context.actorOf(Props[Counter], codeWord(word))
              countingActor ! InitCounter(word, lineCounter.get())
              println("Actor created for: " + countingActor.path)
          }

        }

        // increment line counter
        lineCounter.incrementAndGet()
      }

      txtfile.close()



      // request Results from all actors
      for (actor <- context.children) {
        println("request result from: " + actor.path.name)
        actor ! GetResults
      }

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