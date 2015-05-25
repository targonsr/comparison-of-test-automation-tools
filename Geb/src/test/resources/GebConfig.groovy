/*
 This is the Geb configuration file.
 
 See: http://www.gebish.org/manual/current/configuration.html
*/

import org.openqa.selenium.firefox.FirefoxDriver

driver = { new FirefoxDriver() }

reportsDir = "target/geb-reports"

reportOnTestFailureOnly = true

waiting {
 timeout = 10
 retryInterval = 0.1
}