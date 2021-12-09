import ru.tinkoff.phobos.decoding.{ElementDecoder, XmlDecoder}
import ru.tinkoff.phobos.derivation.semiauto.{deriveElementDecoder, deriveElementEncoder, deriveXmlDecoder, deriveXmlEncoder}
import ru.tinkoff.phobos.encoding.{ElementEncoder, XmlEncoder}
import java.nio.file.Files
import java.nio.file.Paths

object Main {
  case class Destination(name: String, country: String)

  object Destination {
    implicit val destinationEncoder: ElementEncoder[Destination] = deriveElementEncoder
    implicit val destinationDecoder: ElementDecoder[Destination] = deriveElementDecoder
  }

  case class Visa(kind: String, fee: Double, currency: String)

  object Visa {
    implicit val visaEncoder: ElementEncoder[Visa] = deriveElementEncoder
    implicit val visaDecoder: ElementDecoder[Visa] = deriveElementDecoder
  }

  case class Travel(destination: Destination, visa: Visa)

  object Travel {
    implicit val travelXmlEncoder: XmlEncoder[Travel] = deriveXmlEncoder("travel")
    implicit val travelXmlDecoder: XmlDecoder[Travel] = deriveXmlDecoder("travel")
  }

  val travel: Travel = Travel(destination = Destination("Dubrovnik", "Croatia"), Visa("e-visa", 50, "EUR"))

  def main(args: Array[String]): Unit = {

    //case class example
    val xml: String = XmlEncoder[Travel].encode(travel)
    println(xml)

    val decodedTravel = XmlDecoder[Travel].decode(xml)
    println(decodedTravel)

    assert(Right(travel) == decodedTravel)

    //string example
    val xml2 = XmlDecoder[Travel].decode("<travel><destination><name>Dubrovnik</name><country>Croatia</country></destination><visa><kind>e-visa</kind><fee>50.0</fee><currency>EUR</currency></visa></travel>")
    assert(Right(travel) == xml2)

//    read from file example

    val string = new String(Files.readAllBytes(Paths.get("src/main/scala/travel.xml")))
    val xml3 = XmlDecoder[Travel].decode(string)

    assert(Right(travel) == xml3)

  }
}
