# SauceDemo UI Test Automation Framework

![UI Tests](https://github.com/Janhavi2802/saucedemo-selenium-tests/actions/workflows/ui-tests.yml/badge.svg)

A Selenium WebDriver framework in Java that automates the login, product,
cart and checkout flows of [SauceDemo](https://www.saucedemo.com/), built with
the Page Object Model and TestNG.

## Highlights

- **Page Object Model**: one class per page plus a shared `HeaderComponent`
  (cart badge, menu, logout). Tests read like user journeys, and a locator
  change touches only one file.
- **BasePage with explicit waits** for click, type, text and visibility. No `Thread.sleep`.
- **Thread-safe DriverFactory** (`ThreadLocal<WebDriver>`) for parallel runs, supporting
  Chrome, Firefox and Edge in normal or headless mode. Selenium Manager downloads drivers automatically.
- **TestNG suites**: smoke, full, and a cross-browser regression suite, organised with groups.
- **Data providers** for test data sets: 8 invalid-login cases, 4 product combinations,
  4 missing-field and 4 valid checkout inputs (including special characters and Unicode).
- **Screenshot on failure** through a TestNG listener, saved to `target/screenshots`
  and linked in the TestNG report.
- **Config** in `config.properties`, overridable with `-D` flags for CI.
- **CI/CD**: GitHub Actions runs the suite headless on every push and uploads reports and screenshots.

## Project structure

```
src/main/java/com/janhavi/saucedemo/
  config/ConfigReader.java         config.properties + -D overrides
  driver/DriverFactory.java        ThreadLocal WebDriver, browser options
  pages/                           BasePage, LoginPage, InventoryPage, HeaderComponent,
                                   CartPage, CheckoutInfoPage, CheckoutOverviewPage,
                                   CheckoutCompletePage
  utils/ScreenshotUtil.java
src/test/java/com/janhavi/saucedemo/
  base/BaseTest.java               fresh browser per test, opens login page
  listeners/TestListener.java      logging + screenshot on failure
  data/TestDataProvider.java       @DataProvider test data sets
  tests/                           LoginTest, ProductTest, CartTest, CheckoutTest
src/test/resources/
  config.properties
  testng.xml                       full suite (smoke + regression)
  testng-smoke.xml                 critical paths only
  testng-regression.xml            Chrome full + Firefox critical flows
```

## Running the tests

Requires Java 17+, Maven, and Chrome (Firefox for the cross-browser suite).

```bash
mvn test                                   # full suite
mvn test -Dsuite=testng-smoke.xml          # smoke suite
mvn test -Dsuite=testng-regression.xml     # regression, Chrome + Firefox
mvn test -Dheadless=true                   # no browser window
mvn test -Dbrowser=edge                    # another browser
```

Reports: `target/surefire-reports/index.html`. Failure screenshots: `target/screenshots/`.

## Test coverage

| Area | Tests | Covers |
| --- | --- | --- |
| Login | 12 | Valid login, 8 invalid/empty/locked-out cases, dismiss error, logout, direct URL without login |
| Products | 5 | 4 sort options, name and price present |
| Cart | 9 | Add, badge and button state, 4 product combinations, remove from both pages, persistence |
| Checkout | 12 | End-to-end order, subtotal/tax/total maths, 4 valid inputs, 4 missing-field errors, cancel paths |
| **Total** | **38** | 4 tagged smoke |

## Tech stack

Java 17, Selenium WebDriver 4, TestNG, Maven, GitHub Actions
