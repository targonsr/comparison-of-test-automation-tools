import org.scalatest._
import org.scalatest.selenium.Firefox
import org.scalatest.concurrent.Eventually._
import org.scalatest.time.{Millis, Seconds, Span}

class ComparisonTest extends FlatSpec with Firefox with Matchers with GivenWhenThen {

  implicit val patienceConfig =
    PatienceConfig(timeout = scaled(Span(10, Seconds)), interval = scaled(Span(200, Millis)))
  val pattern = "\\d{3},\\d{2}"

  "The lowest price of Venue 7 tablet on Allegro" should "be higher than 350,00 PLN" in {
    When("Going to Allegro Page")
    go to "http://allegro.pl"

    Then("title should start with Allegro.pl")
    pageTitle should startWith("Allegro.pl")

    When("We display list of Venue 7 offers in Tablets category")
    textField("main-search-text").value = "venue 7\n"
    click on xpath("//span[text() = 'Komputery']")
    click on xpath("//span[@class='name' and contains(.,'Tablety')]")

    And("sort results by lowest price")
    click on cssSelector("div.toggle span.label")
    eventually {
      click on xpath("//dt[text()='cena']/following-sibling::dd[1]/descendant::a[contains(.,'od najniższej')]")
    }
    eventually {
      xpath("//span[@class='label' and contains(.,'od najniższej')]").element.isDisplayed should be(true)
    }

    Then("the lowest price of device should be higher than 350,00 PLN")
    val priceStrings = findFirstThreeDigitPrice
    priceStrings should not be empty
    val amount = priceStrings.get.replace(",", ".").toDouble
    amount should be > 350.00

    quit()
  }

  private def findFirstThreeDigitPrice: Option[String] = {
    findAll(xpath("//*[@id='featured-offers']/descendant::article/descendant::span[@class='buy-now dist']")).map {
      case node => pattern.r.findFirstIn(node.text)
    }.filter(_.isDefined).toList.head
  }
}
