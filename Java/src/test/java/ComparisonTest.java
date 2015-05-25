import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ComparisonTest extends FirefoxDriver {

    // given
    private int timeoutInSeconds = 10;
    private int intervalInMilliseconds = 200;
    private Pattern amountRegex = Pattern.compile("(\\d{3},\\d{2})");

    @Test
    public void checkTheLowestPriceOfVenue7OnAllegro() {
        //when
        get("http://allegro.pl");

        //then
        getTitle().startsWith("Allegro.pl");

        //when
        findElement(By.id("main-search-text")).sendKeys("venue 7\n");
        findElement(By.xpath("//span[text() = 'Komputery']")).click();
        findElement(By.xpath("//span[@class='name' and contains(.,'Tablety')]")).click();

        //and
        findElement(By.cssSelector("div.toggle span.label")).click();
        waitForClickableElement(By
                .xpath("//dt[text()='cena']/following-sibling::dd[1]/descendant::a[contains(.,'od najniższej')]"))
                .click();
        waitForElementToDisplayText(By.cssSelector("div.toggle span.label"), "od najniższej");

        //then
        Optional<Double> amount = findFirstThreeDigitPrice();
        assertThat(amount.isPresent()).isTrue();
        assertThat(amount.get()).isGreaterThan(350.0);

        quit();
    }

    private WebElement waitForClickableElement(By by) {
        return new WebDriverWait(this, timeoutInSeconds, intervalInMilliseconds).until(ExpectedConditions
                .elementToBeClickable(by));
    }

    private void waitForElementToDisplayText(By by, String expectedText) {
        new WebDriverWait(this, timeoutInSeconds, intervalInMilliseconds).until(ExpectedConditions
                .textToBePresentInElementLocated(by, expectedText));
    }

    private Optional<Double> findFirstThreeDigitPrice() {
        List<WebElement> prices = findElements(By
                .xpath("//*[@id='featured-offers']/descendant::article/descendant::span[@class='buy-now dist']"));
        return prices.stream()
                .filter(price -> amountRegex.matcher(price.getText()).find()).map(price -> {
                    Matcher matcher = amountRegex.matcher(price.getText());
                    matcher.find();
                    return Double.valueOf(matcher.group(1).replace(",", "."));
                }).findFirst();
    }
}
