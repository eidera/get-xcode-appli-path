import better.files._
import java.io.{File => JFile}
import scala.xml.{Node, XML}

// This class hold all device information of defined by device_set.plist
class DeviceHolder {
  var device = Map[String, Seq[String]]()

  def push(id: String, os: String, model: String): Unit = {
    device += (id) -> Seq(os, model)
  }

  def get(id: String): String = {
    toString(device(id))
  }

  def getIds(): Iterable[String] = {
    device.keys
  }

  private def toString(value: Seq[String]): String = {
    value(0) + " " + value(1)
  }
}

class DeviceSetPlistReader(filename: String) {
  val holder = new DeviceHolder

  def execute: DeviceHolder = {
    val osList = (XML.loadFile(filename) \ "dict" \ "dict").head \ "_"

    var osName = ""
    osList.foreach { os =>
      os.label match {
        case "key" => osName = trimming(os.text)
        case "dict" => readDeviceId(os, osName)
        case _ => // nop
      }
    }

    holder
  }

  private[this] def trimming(text: String): String = {
    text.split('.').last
  }

  private[this] def readDeviceId(os: Node, osName: String): Unit = {
    /**
     * Ex.
     *   <key>com.apple.CoreSimulator.SimDeviceType.Resizable-iPad</key>
     *   <string>DEVICE_ID1</string>
     *   <key>com.apple.CoreSimulator.SimDeviceType.Resizable-iPhone</key>
     *   <string>DEVICE_ID2</string>
     */
    var model = ""
    val modelList = os \ "_"
    modelList.foreach { element =>
      element.label match {
        case "key" => model = trimming(element.text)
        case "string" => holder.push(element.text, osName, model)
        case _ => // nop
      }
    }
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val homeDir = System.getProperty("user.home")

    val deviceSetPlist = homeDir + "/Library/Developer/CoreSimulator/Devices/device_set.plist"
    val sqliteTargetDir = homeDir + "/Library/Developer/CoreSimulator/Devices"
    val sqliteList = "**/data/Containers/Data/Application/**/Documents/*.sqlite"

    val reader = new DeviceSetPlistReader(deviceSetPlist)
    val holder = reader.execute

    var files = File(sqliteTargetDir).glob(sqliteList)
    files.foreach { file =>
      val filePath = file.pathAsString
      val dirs = filePath.split('/')
      val deviceId = dirs(dirs.length - 8) // Device ID is in the 8th from behind.
      val applicationName = dirs(dirs.length-1).replaceAll(".sqlite$", "")
      printf("%s\n  %s\n    %s\n", applicationName, holder.get(deviceId), filePath)
    }
  }
}

