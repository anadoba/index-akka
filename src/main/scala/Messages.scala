/**
 * Created by Adam on 2015-02-05.
 */
object Messages {
  case class Start(filename: String)
  case class InitCounter(word: String, line: Int)
  case class WordFound(line: Int)
  case class Result(pair: List[(String,List[Int])])
  object GetResults
}
