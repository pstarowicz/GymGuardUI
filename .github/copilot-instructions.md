## Purpose

This file contains guidance for Copilot and contributors working on the GymGuardUI test automation framework. It synthesizes the project Analysis, High-Level Design (HLD) and Low-Level Design (LLD) artifacts to present a concise onboarding, coding and contribution guide focused on the Page Object Model (POM) TestNG + Selenium Java framework described in the docs.

## Quick summary (one-liner)

GymGuardUI is a Java + Selenium WebDriver + TestNG test automation framework implemented with Maven and organized around POM, Factory and Singleton patterns; this file explains how to add pages/tests, run the suite locally, and what Copilot should prefer when generating code.

### Cross-repo locator conventions (important)

GymGuardUI is the test framework for the `GymSetTracker` application that lives in this workspace. To produce stable, reliable tests the framework MUST reference app-provided stable attributes (for example `data-test-id`) when constructing locators.

- Source of truth: the application's DOM and frontend components. There is no canonical database file containing selector ids. Look for attributes the app renders (e.g., `data-test-id`) in `GymSetTracker/frontend/src/` or inspect the running UI. If a stable attribute is missing, coordinate with the app team to add `data-test-id` attributes following the `<entity>-<id>` pattern.
- Preferred locator strategy: `data-test-id` attributes with the pattern `<entity>-<id>`. Example: `data-test-id="exercise-42"`.
- If a required `data-test-id` is missing in the app markup, open a small PR to `GymSetTracker` adding `data-test-id` using the `<entity>-<id>` pattern. Tests should avoid brittle structural selectors.

Example usage in generated Java page objects:

```java
// helper inside a BasePage or LocatorUtils
public By testId(String entity, String id) {
	return By.cssSelector("[data-test-id='" + entity + "-" + id + "']");
}

// usage
By exerciseRow = testId("exercise", exerciseId);
driver.findElement(exerciseRow).click();
```

Guidance for Copilot when generating code:

 - Always use the attribute provided by the app's markup when possible. If the id/attribute is not provided in the user's prompt, ask for it or request the canonical id from the app team or inspect the running UI.
 - Keep locator builders small and reusable to avoid duplication.

## Quickstart (prereqs)

- Java 17+ installed and configured on PATH
- Maven 3.x
- Chrome (or another supported browser) for local interactive runs
- Browser driver (use WebDriverManager or place binaries in `src/test/resources/drivers`)—WebDriverManager is recommended

Run the full suite locally (default TestNG suite):

```bash
# run tests defined in testng.xml
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml
```

Build only (no tests):

```bash
mvn -DskipTests package
```

If you prefer a single-test run during development use TestNG include/exclude or the surefire -Dtest property:

```bash
# run a single TestNG test class
mvn -Dtest=LoginTests test
```

Notes:
- CI workflows should run `mvn test` and publish reports (Allure integration is a future enhancement from HLD/LLD).

## Project layout (short)

- `src/main/java` — framework core (base, pages, utils, factories, constants)
- `src/test/java` — TestNG test classes and suites
- `src/test/resources` — config, testdata, drivers
- `testng.xml` — default TestNG suite descriptor
- `.github/` — design docs and CI workflows

Refer to the HLD and LLD in `.github/` for comprehensive design rationale and examples.

## Design and coding rules (for Copilot & contributors)

When generating or modifying code, prefer the following conventions and patterns that come from the HLD/LLD:

- Page Objects: encapsulate locators and interactions inside a page class. Keep assertions in tests, not page objects. Page constructors must call `PageFactory.initElements(driver, this)` (or equivalent) and extend a `BasePage` providing helper methods like `clickElement`, `clearAndType`, `waitForElement`.
- Driver handling: use a `DriverFactory` for browser options and a `DriverManager` with ThreadLocal<WebDriver> for thread-safety.
- Config: read environment and browser properties from a central `ConfigManager` or properties file under `src/test/resources/config`.
- Tests: use TestNG. Use `@DataProvider` for parameterized tests and keep tests atomic (one behavior/assertion per test). Test classes should extend `BaseTest` which handles setup/teardown.
- Timeouts and waits: prefer explicit waits (`WebDriverWait` + `ExpectedConditions`). Avoid global long implicit waits except for a short default.
- Naming: classes in `pages` use `*Page` suffix. Tests use `*Tests` suffix. Test methods use `should...` or `test...` style and include expected behavior in the name.

Example file/class names:

- `LoginPage.java`, `RegistrationPage.java`
- `WorkoutLoggingPage.java`
- `DriverFactory.java`, `DriverManager.java`
- `BaseTest.java`, `TestListener.java`

## How Copilot should generate tests and page objects

When asked to generate code, Copilot should:

1. Produce a complete class with package declaration matching the repository structure (e.g., `com.gymguard.framework.pages.authentication`).
2. Use constructor injection for `WebDriver` and call the base page constructor.
3. Use `@FindBy` annotations for locators and private `WebElement` fields.
4. Implement small, reusable page methods (e.g., `login(email, password)` returns `DashboardPage`). Keep method size minimal (single responsibility).
5. For tests, include `@Test` and if needed `@DataProvider` with simple example data. Use assertions from TestNG (`Assert.assertEquals`, `Assert.assertTrue`).
6. Add logging (slf4j) at info/debug level for important actions.
7. For flaky actions (AJAX loads), use explicit wait helper methods from `WaitHelper`.

Prefer code patterns and small examples from the LLD (BasePage, DriverFactory, DriverManager). If you cannot find a specific helper in the repo, implement the helper in `src/main/java/.../utils` with minimal dependencies and a brief JavaDoc.

## Adding a new Page Object (step-by-step)

1. Create the page class under `src/main/java/.../pages/<area>/`.
2. Extend `BasePage` and include a constructor that accepts `WebDriver` and calls `super(driver)`.
3. Add private `@FindBy` elements and public action methods (return other Page objects where navigation happens).
4. Add unit-like TestNG tests under `src/test/java/.../tests/<area>/` that exercise the page methods.
5. Update `testng.xml` when adding integration-level suites if needed.

## Adding a new Test (step-by-step)

1. Create a test class in `src/test/java/com/gymguard/tests/<area>/` with suffix `Tests`.
2. Extend `BaseTest` to inherit driver setup/teardown.
3. Use `@BeforeMethod` only for per-test setup beyond BaseTest, `@AfterMethod` for cleanups.
4. Keep test data external (JSON under `src/test/resources/testdata`) and use `TestDataProvider` where appropriate.

## Test data strategy

- Static data: `src/test/resources/testdata/*.json` for exercises, users, workouts.
- Dynamic data: use `TestDataProvider` factory methods to generate unique users and avoid collisions.
- Environment-specific config: `src/test/resources/config/application.properties` with keys like `app.base.url`, `browser.default`, `headless`.

## Failure handling & diagnostics

- Tests must take screenshots on failure via a `ScreenshotHelper` invoked in `@AfterMethod` (see LLD). Save screenshots under `reports/screenshots` with clear names.
- Tests should produce useful log messages and include the page screenshot path in logs.

## CI and reporting (notes)

- Add a GitHub Actions workflow that runs `mvn test` and publishes TestNG results. Allure reporting is a recommended enhancement from the HLD/LLD.
- Keep test runs idempotent: tests should not rely on a specific persisted dataset unless the CI resets test data before runs.

## Code review checklist (for PRs)

- Does the code follow POM and not contain assertions inside page objects?
- Are locators stable and not overly brittle (prefer data-* attributes or IDs)?
- Are waits explicit and not arbitrary sleeps? (no Thread.sleep for production tests)
- Are tests deterministic and independent of execution order?
- Are new utilities documented and placed under `utils` with JavaDoc?
- Is test data isolated and unique per test run where required?

## Small engineering contract (2–4 bullets)

- Inputs: `WebDriver`, configuration from `application.properties`, optional test data JSONs.
- Outputs: TestNG reports, screenshots on failure, logs under `reports/logs`.
- Error modes: network/unavailable UI elements (use retries/waits), authentication failures (fail fast), data collisions (use dynamic users).

## Edge cases & hints for Copilot

- Always consider empty or missing elements (null pointer safe checks with waits).
- For large forms use smaller helper methods per section to improve readability and maintainability.
- Prefer explicit exception messages that include the page and action (e.g., "LoginPage: failed to click login button").

## Where to find the design docs

- The repo includes analysis, HLD and LLD under `.github/` (see `gymguardui-analysis.md`, `GymGuardUI-HLD.md`, `GymGuardUI-LLD.md`). Use those as the canonical design references when making structural changes.

## Follow-ups / TODOs

- Integrate WebDriverManager to remove driver binaries from source.
- Add an example TestNG/Allure pipeline workflow to `.github/workflows`.
- Add a CONTRIBUTING.md describing PR and test run expectations.