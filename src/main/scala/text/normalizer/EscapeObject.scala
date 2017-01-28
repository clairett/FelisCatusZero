package text.normalizer

import java.nio.file.Paths

import text.{StringNone, StringOption, StringSome}
import util.Config

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * @author K.Sakamoto
  *         Created on 2016/08/06
  */
class EscapeObject(objectFileNameOpt: StringOption) {

  val objects: Seq[String] = initialize()

  private def initialize(): Seq[String] = {
    if (objectFileNameOpt.isEmpty) {
      return Nil
    }

    val buffer: ListBuffer[String] = ListBuffer.empty[String]
    Source.fromFile(
      Paths.get(Config.resourcesDir, "normalizer", objectFileNameOpt.get).toAbsolutePath.toFile
    ).getLines foreach {
      line: String =>
        NormalizedString(StringOption(line.trim)).toStringOption match {
          case StringSome(l) =>
            buffer += l
          case StringNone =>
            //Do nothing
        }
    }

    buffer.result
  }
}
