package text.kanji

/**
  * @author K.Sakamoto
  *         Created on 2016/07/26
  */
object PrimarySchool5thGradeKanjiCharacter extends KanjiCharacter {
  override val kanji: Seq[String] = readKanjiCSV("primary_school_5th_grade")
}
