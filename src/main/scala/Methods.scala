import scala.util.matching.Regex._

/**
 * Created by Adam on 2015-02-05.
 */
object Methods {

  //val polishSpecialLetters = List('ą','ć','ę','ł','ń','ó','ś','ź','ż')
  val regex = "[ąćęłńóśźż]".r

  def codeCharacter(character: Char) = {
    character match {
      case 'ą' => "a#"
      case 'c' => "c#"
      case 'ę' => "e#"
      case 'ł' => "l#"
      case 'ń' => "n#"
      case 'ó' => "o#"
      case 'ś' => "s#"
      case 'ż' => "z#"
      case 'ź' => "x#"
      case _ => character
    }
  }

  def codeWord(word: String) = {
    val codedVector = for (char <- word) yield codeCharacter(char)
    codedVector.mkString("")
  }

  def decodeWord(word: String): String = {
    word.replace("a#", "ą")
      .replace("c#", "ć")
      .replace("e#", "ę")
      .replace("l#", "ł")
      .replace("n#", "ń")
      .replace("o#", "ó")
      .replace("s#", "ś")
      .replace("z#", "ż")
      .replace("x#", "ź")
  }
}
