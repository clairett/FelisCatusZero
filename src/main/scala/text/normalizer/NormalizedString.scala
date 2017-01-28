package text.normalizer

import java.text.{Normalizer => JavaNormalizer}

import text.{StringNone, StringOption, StringSome}

/**
  * @author K.Sakamoto
  *         Created on 15/10/28
  */
object NormalizedString {
  def apply(str: StringOption): NormalizedString = {
    new NormalizedString(str)
  }
}

/**
  * @author K.Sakamoto
  * @param str string
  */
class NormalizedString(str: StringOption) extends AnyRef {
  private val normalizedString: StringOption = normalize

  private def normalize: StringOption = {
    Normalizer.normalize(str)
  }

  override def toString: String = {
    normalizedString.getOrElse("")
  }

  def toStringOption: StringOption = {
    normalizedString
  }

  override def hashCode: Int = {
    this.toString.toInt
  }

  override def equals(other: Any): Boolean = other match {
    case that: NormalizedString =>
      this.toString == that.toString
    case _ => false
  }

  def concat(nStr: NormalizedString): NormalizedString = {
    new NormalizedString(normalizedString.map(_.concat(nStr.toString)))
  }

  def split(regex: String): Array[NormalizedString] = {
    normalizedString match {
      case StringSome(nStr) =>
        nStr.split(regex) map {
          token: String =>
            new NormalizedString(StringOption(token))
        }
      case StringNone =>
        Array.empty[NormalizedString]
    }
  }

  def split(separator: Char): Array[NormalizedString] = {
    Normalizer.normalize(StringOption(separator.toString)) match {
      case StringSome(nSeparator) =>
        normalizedString match {
          case StringSome(nStr) =>
            nStr.split(nSeparator.head) map {
              token: String =>
                new NormalizedString(StringOption(token))
            }
          case StringNone =>
            Array.empty[NormalizedString]
        }
      case StringNone =>
        Array.empty[NormalizedString]
    }
  }

  def trim: NormalizedString = {
    normalizedString.map(_.trim)
    this
  }
}

/**
  * @author K.Sakamoto
  */
object Normalizer {
  def normalize(str: StringOption): StringOption = {
    def normalizeCharacters: StringOption = {
      CharacterNormalizerBeforeUnicodeNormalization.normalize(str) map {
        s: String =>
          CharacterNormalizerAfterUnicodeNormalization.normalize(
            StringOption(
              JavaNormalizer.normalize(s, JavaNormalizer.Form.NFKC)
            )
          ).getOrElse(null)
      }
    }

    if (str.isEmpty) {
      return StringNone
    }
    WordExpressionNormalizer.normalize(normalizeCharacters)
  }
}

/**
  * @author K.Sakamoto
  */
class NormalizedStringBuilder {
  val builder: StringBuilder = new StringBuilder()

  def append(nStr: NormalizedString): NormalizedStringBuilder = {
    builder.append(nStr.toString)
    this
  }

  def result: NormalizedString = {
    new NormalizedString(StringOption(builder.toString()))
  }

  def clear(): Unit = {
    builder.clear
  }
}

/**
  * @author K.Sakamoto
  */
class NormalizedStringBuffer {
  val buffer: StringBuffer = new StringBuffer()

  def append(nStr: NormalizedString): NormalizedStringBuffer = {
    buffer.append(nStr.toString)
    this
  }

  def result: NormalizedString = {
    new NormalizedString(StringOption(buffer.toString))
  }

  def clear(): Unit = {
    buffer.setLength(0)
  }
}