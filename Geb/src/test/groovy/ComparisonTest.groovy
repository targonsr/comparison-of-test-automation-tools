import geb.spock.GebReportingSpec

class ComparisonTest extends GebReportingSpec {

    def pattern = /\d{3},\d{2}/

    def "Check the lowest price of Venue 7 on allegro"() {
        when: "Going to Allegro Page"
        go "http://allegro.pl/"

        then: "title should start with Allegro.pl"
        title.startsWith("Allegro.pl")

        when: "We display list of Venue 7 offers in Tablets category"
        $("#main-search-text") << "venue 7\n"
        $("span.name", text: "Komputery").click()
        $("span.name", text: contains("Tablety")).click()

        and: "sort results by lowest price"
        $("div.toggle span.label").click()
        waitFor { $("div.options dt", text: "cena").next("dd").find("a", text: contains("od najniższej"))
                .click() }
        waitFor { $("span.label", text: contains("od najniższej")).displayed }

        then: "the lowest price of device should be higher than 350,00 PLN"
        def priceStrings = $("#featured-offers article span.buy-now.dist", text: contains(~/\d{3},\d{2}/)).text()
        !priceStrings.empty
        def amount = priceStrings.find(pattern).replace(",", ".").toDouble()
        amount > 350.00
    }
}
