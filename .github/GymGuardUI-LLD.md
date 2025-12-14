**GymGuardUI Test Automation Framework - Low-Level Design (LLD) for MVP**

**Executive Summary**

**GymGuardUI** is a comprehensive GUI test automation framework designed specifically for testing a strength training log application MVP. This Low-Level Design document provides detailed technical specifications, implementation guidelines, and architectural patterns for building a robust, scalable, and maintainable test automation solution.

The framework implements industry-standard design patterns including Page Object Model (POM), Factory Pattern, and Singleton Pattern while leveraging modern technologies such as Java 17+, Selenium WebDriver 4.x, TestNG 7.x, and Maven 3.x for optimal performance and maintainability.

**Application Under Test - Technical Overview**

**Core Application Architecture**

The strength training application follows a modern three-tier architecture optimized for Raspberry Pi Zero 2W deployment:

**Frontend Layer:**

- **Technology**: React 18.2+ with functional components and Hooks
- **Architecture**: Single Page Application (SPA) with responsive design
- **Communication**: RESTful API consumption with JWT authentication
- **UI Framework**: Bootstrap or Tailwind CSS for rapid prototyping

**Backend Layer:**

- **Technology**: Spring Boot 3.1+ with Java 17
- **Architecture**: RESTful API with stateless design
- **Authentication**: JWT (JSON Web Tokens) with secure token management
- **Business Logic**: Modular service layer with clear separation of concerns

**Database Layer:**

- **Technology**: PostgreSQL 15+ with optimized schema design
- **Caching**: Redis for session management (optional)
- **Performance**: Strategic indexing and query optimization for limited resources

**Application Features Requiring Test Coverage**

**Epic 1: User Account Management**

- User registration with comprehensive validation
- Secure authentication with JWT token management
- Profile management with body metrics tracking
- Session management and timeout handling

**Epic 2: Workout Logging**

- Training session creation and management
- Exercise database with predefined and custom exercises
- Set and repetition logging with weight tracking
- Workout completion and data persistence

**Epic 3: Progress Tracking**

- Workout history with filtering and search capabilities
- Personal records calculation and tracking
- Basic progression charts and data visualization
- Achievement notifications and milestone tracking

**Framework Architecture Design**

**Technology Stack Specification**

**Core Technologies:**

- **Java 17+**: Primary programming language with modern language features
- **Selenium WebDriver 4.x**: Browser automation with W3C WebDriver protocol
- **TestNG 7.x**: Test execution framework with advanced parallel execution
- **Maven 3.x**: Build automation and dependency management with standardized project structure

**Enhanced Technologies (Future Implementation):**

- **Allure Framework**: Advanced reporting with trend analysis and detailed test analytics
- **Docker**: Containerization for consistent test environments across different platforms
- **Logback**: Comprehensive logging with configurable appenders and log levels
- **GitHub Actions**: CI/CD integration for automated test execution and reporting

**Framework Design Patterns Implementation**

**1\. Page Object Model (POM) Pattern**

**Purpose**: Encapsulate UI elements and interactions within dedicated page classes to separate test logic from UI implementation details.

**Implementation Structure:**

// Base Page Class  
public abstract class BasePage {  
protected WebDriver driver;  
protected WebDriverWait wait;  
<br/>public BasePage(WebDriver driver) {  
this.driver = driver;  
this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));  
PageFactory.initElements(driver, this);  
}  
<br/>protected void clickElement(WebElement element) {  
wait.until(ExpectedConditions.elementToBeClickable(element)).click();  
}  
<br/>protected void sendKeysToElement(WebElement element, String text) {  
wait.until(ExpectedConditions.visibilityOf(element));  
element.clear();  
element.sendKeys(text);  
}  
}  
<br/>// Specific Page Implementation  
public class LoginPage extends BasePage {  
@FindBy(id = "email")  
private WebElement emailField;  
<br/>@FindBy(id = "password")  
private WebElement passwordField;  
<br/>@FindBy(css = "button\[type='submit'\]")  
private WebElement loginButton;  
<br/>@FindBy(css = ".error-message")  
private WebElement errorMessage;  
<br/>public LoginPage(WebDriver driver) {  
super(driver);  
}  
<br/>public DashboardPage loginWithValidCredentials(String email, String password) {  
sendKeysToElement(emailField, email);  
sendKeysToElement(passwordField, password);  
clickElement(loginButton);  
return new DashboardPage(driver);  
}  
<br/>public String getErrorMessage() {  
return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();  
}  
}  

**2\. Factory Pattern Implementation**

**Purpose**: Centralize object creation and configuration for WebDrivers, test data, and page objects.

**Driver Factory Implementation:**

public class DriverFactory {  
private static final String BROWSER_PROPERTY = "browser";  
private static final String HEADLESS_PROPERTY = "headless";  
<br/>public static WebDriver createDriver() {  
String browserName = ConfigReader.getProperty(BROWSER_PROPERTY, "chrome");  
boolean headless = Boolean.parseBoolean(ConfigReader.getProperty(HEADLESS_PROPERTY, "false"));  
<br/>return createDriver(browserName, headless);  
}  
<br/>public static WebDriver createDriver(String browserName, boolean headless) {  
WebDriver driver;  
<br/>switch (browserName.toLowerCase()) {  
case "chrome":  
ChromeOptions chromeOptions = getChromeOptions(headless);  
driver = new ChromeDriver(chromeOptions);  
break;  
case "firefox":  
FirefoxOptions firefoxOptions = getFirefoxOptions(headless);  
driver = new FirefoxDriver(firefoxOptions);  
break;  
case "edge":  
EdgeOptions edgeOptions = getEdgeOptions(headless);  
driver = new MicrosoftEdgeDriver(edgeOptions);  
break;  
default:  
throw new IllegalArgumentException("Unsupported browser: " + browserName);  
}  
<br/>configureDriver(driver);  
return driver;  
}  
<br/>private static ChromeOptions getChromeOptions(boolean headless) {  
ChromeOptions options = new ChromeOptions();  
options.addArguments("--disable-notifications");  
options.addArguments("--disable-popup-blocking");  
options.addArguments("--disable-extensions");  
<br/>if (headless) {  
options.addArguments("--headless=new");  
}  
<br/>return options;  
}  
<br/>private static void configureDriver(WebDriver driver) {  
driver.manage().window().maximize();  
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));  
driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));  
}  
}  

**3\. Singleton Pattern Implementation**

**Purpose**: Ensure single instances for configuration management and resource handling.

**Configuration Manager Implementation:**

public class ConfigManager {  
private static ConfigManager instance;  
private Properties properties;  
private static final String CONFIG_FILE = "config.properties";  
<br/>private ConfigManager() {  
loadProperties();  
}  
<br/>public static ConfigManager getInstance() {  
if (instance == null) {  
synchronized (ConfigManager.class) {  
if (instance == null) {  
instance = new ConfigManager();  
}  
}  
}  
return instance;  
}  
<br/>private void loadProperties() {  
properties = new Properties();  
try (InputStream input = getClass().getClassLoader()  
.getResourceAsStream(CONFIG_FILE)) {  
if (input != null) {  
properties.load(input);  
}  
} catch (IOException e) {  
throw new RuntimeException("Failed to load configuration file", e);  
}  
}  
<br/>public String getProperty(String key) {  
return properties.getProperty(key);  
}  
<br/>public String getProperty(String key, String defaultValue) {  
return properties.getProperty(key, defaultValue);  
}  
}  

**Project Structure and Organization**

**Maven Standard Directory Layout**

GymGuardUI/  
├── pom.xml # Maven configuration and dependencies  
├── testng.xml # TestNG suite configuration  
├── README.md # Project documentation  
├── .gitignore # Git ignore configuration  
├── src/  
│ ├── main/java/ # Main framework code  
│ │ ├── com/gymguard/framework/  
│ │ │ ├── base/ # Base classes and common functionality  
│ │ │ │ ├── BaseTest.java # Base test class with setup/teardown  
│ │ │ │ ├── BasePage.java # Base page class with common methods  
│ │ │ │ └── TestListener.java # TestNG listener implementation  
│ │ │ ├── pages/ # Page Object Model classes  
│ │ │ │ ├── authentication/  
│ │ │ │ │ ├── LoginPage.java  
│ │ │ │ │ └── RegistrationPage.java  
│ │ │ │ ├── dashboard/  
│ │ │ │ │ ├── DashboardPage.java  
│ │ │ │ │ └── UserProfilePage.java  
│ │ │ │ ├── workout/  
│ │ │ │ │ ├── WorkoutLoggingPage.java  
│ │ │ │ │ ├── ExerciseDatabasePage.java  
│ │ │ │ │ └── SetEntryPage.java  
│ │ │ │ └── progress/  
│ │ │ │ ├── ProgressTrackingPage.java  
│ │ │ │ └── HistoryPage.java  
│ │ │ ├── utils/ # Utility classes and helper methods  
│ │ │ │ ├── DriverManager.java  
│ │ │ │ ├── ConfigReader.java  
│ │ │ │ ├── TestDataProvider.java  
│ │ │ │ ├── WaitHelper.java  
│ │ │ │ ├── ScreenshotHelper.java  
│ │ │ │ └── DateTimeHelper.java  
│ │ │ ├── constants/ # Application constants and enums  
│ │ │ │ ├── ApplicationConstants.java  
│ │ │ │ ├── TestDataConstants.java  
│ │ │ │ └── ElementLocators.java  
│ │ │ └── factories/ # Factory pattern implementations  
│ │ │ ├── DriverFactory.java  
│ │ │ ├── PageFactory.java  
│ │ │ └── TestDataFactory.java  
│ │ └── resources/ # Main resources (if any)  
│ └── test/ # Test code and resources  
│ ├── java/ # Test classes  
│ │ └── com/gymguard/tests/  
│ │ ├── authentication/ # Authentication test classes  
│ │ │ ├── LoginTests.java  
│ │ │ ├── RegistrationTests.java  
│ │ │ └── ProfileManagementTests.java  
│ │ ├── workout/ # Workout functionality tests  
│ │ │ ├── WorkoutCreationTests.java  
│ │ │ ├── ExerciseLoggingTests.java  
│ │ │ ├── SetManagementTests.java  
│ │ │ └── WorkoutCompletionTests.java  
│ │ ├── progress/ # Progress tracking tests  
│ │ │ ├── HistoryViewTests.java  
│ │ │ ├── PersonalRecordsTests.java  
│ │ │ └── ProgressVisualizationTests.java  
│ │ └── integration/ # End-to-end integration tests  
│ │ ├── EndToEndWorkflowTests.java  
│ │ └── DataPersistenceTests.java  
│ └── resources/ # Test resources  
│ ├── config/ # Configuration files  
│ │ ├── application.properties  
│ │ ├── test-environments.properties  
│ │ └── logback.xml  
│ ├── testdata/ # Test data files  
│ │ ├── user-data.json  
│ │ ├── exercise-data.json  
│ │ ├── workout-data.json  
│ │ └── test-users.xlsx  
│ └── drivers/ # WebDriver binaries (if not using WebDriverManager)  
├── target/ # Maven build output  
├── reports/ # Test execution reports  
│ ├── testng-reports/  
│ ├── screenshots/  
│ └── logs/  
└── scripts/ # Utility scripts  
├── run-tests.sh  
└── setup-environment.sh  

**Detailed Component Specifications**

**Base Classes Implementation**

**BaseTest.java - Comprehensive Test Foundation:**

public abstract class BaseTest {  
protected WebDriver driver;  
protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);  
<br/>@BeforeMethod(alwaysRun = true)  
public void setUp(Method method) {  
logger.info("Setting up test: {}", method.getName());  
<br/>// Initialize WebDriver  
driver = DriverFactory.createDriver();  
DriverManager.setDriver(driver);  
<br/>// Navigate to application URL  
String baseUrl = ConfigManager.getInstance().getProperty("app.base.url");  
driver.get(baseUrl);  
<br/>logger.info("Test setup completed for: {}", method.getName());  
}  
<br/>@AfterMethod(alwaysRun = true)  
public void tearDown(ITestResult result) {  
if (result.getStatus() == ITestResult.FAILURE) {  
String screenshotPath = ScreenshotHelper.captureScreenshot(  
driver, result.getMethod().getMethodName()  
);  
logger.error("Test failed. Screenshot saved: {}", screenshotPath);  
}  
<br/>if (driver != null) {  
DriverManager.quitDriver();  
logger.info("Test teardown completed");  
}  
}  
<br/>@BeforeSuite  
public void suiteSetup() {  
logger.info("Starting test suite execution");  
// Suite-level setup if needed  
}  
<br/>@AfterSuite  
public void suiteTearDown() {  
logger.info("Test suite execution completed");  
// Suite-level cleanup if needed  
}  
}  

**Driver Management Implementation**

**DriverManager.java - Thread-Safe Driver Handling:**

public class DriverManager {  
private static final ThreadLocal&lt;WebDriver&gt; driverThreadLocal = new ThreadLocal<>();  
private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);  
<br/>public static void setDriver(WebDriver driver) {  
if (driver == null) {  
throw new IllegalArgumentException("Driver cannot be null");  
}  
driverThreadLocal.set(driver);  
logger.debug("WebDriver set for thread: {}", Thread.currentThread().getName());  
}  
<br/>public static WebDriver getDriver() {  
WebDriver driver = driverThreadLocal.get();  
if (driver == null) {  
throw new IllegalStateException("WebDriver not initialized for current thread");  
}  
return driver;  
}  
<br/>public static void quitDriver() {  
WebDriver driver = driverThreadLocal.get();  
if (driver != null) {  
try {  
driver.quit();  
logger.debug("WebDriver quit for thread: {}", Thread.currentThread().getName());  
} catch (Exception e) {  
logger.warn("Error occurred while quitting driver: {}", e.getMessage());  
} finally {  
driverThreadLocal.remove();  
}  
}  
}  
<br/>public static boolean isDriverInitialized() {  
return driverThreadLocal.get() != null;  
}  
}  

**Test Data Management Strategy**

**TestDataProvider Implementation**

**TestDataProvider.java - Comprehensive Data Management:**

public class TestDataProvider {  
private static final Logger logger = LoggerFactory.getLogger(TestDataProvider.class);  
private static final ObjectMapper objectMapper = new ObjectMapper();  
<br/>@DataProvider(name = "validUserData")  
public static Object\[\]\[\] getValidUserData() {  
return new Object\[\]\[\] {  
{"john.doe@example.com", "SecurePass123!", "John Doe", 75.5, 180},  
{"jane.smith@example.com", "StrongPass456!", "Jane Smith", 62.0, 165},  
{"mike.wilson@example.com", "PowerPass789!", "Mike Wilson", 85.2, 175}  
};  
}  
<br/>@DataProvider(name = "invalidLoginData")  
public static Object\[\]\[\] getInvalidLoginData() {  
return new Object\[\]\[\] {  
{"", "password123", "Email is required"},  
{"invalid-email", "password123", "Invalid email format"},  
{"user@example.com", "", "Password is required"},  
{"nonexistent@example.com", "wrongpass", "Invalid credentials"}  
};  
}  
<br/>@DataProvider(name = "workoutData")  
public static Object\[\]\[\] getWorkoutData() throws IOException {  
String workoutDataPath = "testdata/workout-data.json";  
InputStream inputStream = TestDataProvider.class.getClassLoader()  
.getResourceAsStream(workoutDataPath);  
<br/>if (inputStream == null) {  
throw new FileNotFoundException("Workout data file not found: " + workoutDataPath);  
}  
<br/>WorkoutData\[\] workouts = objectMapper.readValue(inputStream, WorkoutData\[\].class);  
Object\[\]\[\] data = new Object\[workouts.length\]\[\];  
<br/>for (int i = 0; i < workouts.length; i++) {  
data\[i\] = new Object\[\]{workouts\[i\]};  
}  
<br/>return data;  
}  
<br/>public static UserData createRandomUser() {  
String email = "testuser" + System.currentTimeMillis() + "@example.com";  
String name = "Test User " + System.currentTimeMillis();  
String password = "TestPass" + System.currentTimeMillis();  
<br/>return UserData.builder()  
.email(email)  
.name(name)  
.password(password)  
.weight(70.0 + (Math.random() \* 30))  
.height(160 + (int)(Math.random() \* 30))  
.build();  
}  
}  

**Test Data Model Classes**

**UserData.java - User Entity Representation:**

@Data  
@Builder  
@NoArgsConstructor  
@AllArgsConstructor  
public class UserData {  
private String email;  
private String name;  
private String password;  
private Double weight;  
private Integer height;  
<br/>public boolean isValidForRegistration() {  
return email != null && !email.trim().isEmpty() &&  
name != null && !name.trim().isEmpty() &&  
password != null && password.length() >= 8;  
}  
}  

**WorkoutData.java - Workout Entity Representation:**

@Data  
@Builder  
@NoArgsConstructor  
@AllArgsConstructor  
public class WorkoutData {  
private String date;  
private String startTime;  
private String endTime;  
private String notes;  
private List&lt;ExerciseSetData&gt; sets;  
<br/>@Data  
@Builder  
@NoArgsConstructor  
@AllArgsConstructor  
public static class ExerciseSetData {  
private String exerciseName;  
private Integer setNumber;  
private Double weight;  
private Integer reps;  
private Integer restSeconds;  
}  
}  

**Page Object Model Implementation Details**

**LoginPage.java - Authentication Page Implementation**

public class LoginPage extends BasePage {  
private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);  
<br/>// Page Elements  
@FindBy(id = "email")  
private WebElement emailField;  
<br/>@FindBy(id = "password")  
private WebElement passwordField;  
<br/>@FindBy(css = "button\[type='submit'\]")  
private WebElement loginButton;  
<br/>@FindBy(css = ".error-message")  
private WebElement errorMessage;  
<br/>@FindBy(linkText = "Forgot Password?")  
private WebElement forgotPasswordLink;  
<br/>@FindBy(linkText = "Sign Up")  
private WebElement signUpLink;  
<br/>// Constructor  
public LoginPage(WebDriver driver) {  
super(driver);  
logger.debug("LoginPage initialized");  
}  
<br/>// Page Actions  
public DashboardPage loginWithCredentials(String email, String password) {  
logger.info("Attempting login with email: {}", email);  
<br/>clearAndType(emailField, email);  
clearAndType(passwordField, password);  
clickElement(loginButton);  
<br/>// Wait for page transition  
waitForPageLoad();  
<br/>logger.info("Login attempt completed");  
return new DashboardPage(driver);  
}  
<br/>public LoginPage loginWithInvalidCredentials(String email, String password) {  
logger.info("Attempting login with invalid credentials");  
<br/>clearAndType(emailField, email);  
clearAndType(passwordField, password);  
clickElement(loginButton);  
<br/>// Wait for error message to appear  
waitForElement(errorMessage, 5);  
<br/>return this;  
}  
<br/>public RegistrationPage navigateToSignUp() {  
logger.info("Navigating to sign up page");  
clickElement(signUpLink);  
return new RegistrationPage(driver);  
}  
<br/>public String getErrorMessage() {  
return getElementText(errorMessage);  
}  
<br/>public boolean isErrorMessageDisplayed() {  
return isElementVisible(errorMessage);  
}  
<br/>// Validation Methods  
public boolean isLoginPageLoaded() {  
return isElementVisible(emailField) &&  
isElementVisible(passwordField) &&  
isElementVisible(loginButton);  
}  
<br/>public String getPageTitle() {  
return driver.getTitle();  
}  
<br/>public String getCurrentUrl() {  
return driver.getCurrentUrl();  
}  
}  

**WorkoutLoggingPage.java - Core Functionality Implementation**

public class WorkoutLoggingPage extends BasePage {  
private static final Logger logger = LoggerFactory.getLogger(WorkoutLoggingPage.class);  
<br/>// Page Elements  
@FindBy(id = "workout-date")  
private WebElement workoutDateField;  
<br/>@FindBy(id = "start-time")  
private WebElement startTimeField;  
<br/>@FindBy(id = "exercise-search")  
private WebElement exerciseSearchField;  
<br/>@FindBy(css = ".exercise-suggestions")  
private WebElement exerciseSuggestions;  
<br/>@FindBy(id = "add-set-button")  
private WebElement addSetButton;  
<br/>@FindBy(css = ".workout-sets")  
private WebElement setsContainer;  
<br/>@FindBy(id = "save-workout")  
private WebElement saveWorkoutButton;  
<br/>@FindBy(id = "workout-notes")  
private WebElement workoutNotesField;  
<br/>// Constructor  
public WorkoutLoggingPage(WebDriver driver) {  
super(driver);  
logger.debug("WorkoutLoggingPage initialized");  
}  
<br/>// Workout Creation Methods  
public WorkoutLoggingPage setWorkoutDate(String date) {  
logger.info("Setting workout date: {}", date);  
clearAndType(workoutDateField, date);  
return this;  
}  
<br/>public WorkoutLoggingPage setStartTime(String time) {  
logger.info("Setting start time: {}", time);  
clearAndType(startTimeField, time);  
return this;  
}  
<br/>public WorkoutLoggingPage selectExercise(String exerciseName) {  
logger.info("Selecting exercise: {}", exerciseName);  
<br/>// Type in search field  
clearAndType(exerciseSearchField, exerciseName);  
<br/>// Wait for suggestions to appear  
waitForElement(exerciseSuggestions, 5);  
<br/>// Click on the matching exercise  
WebElement exerciseOption = driver.findElement(  
By.cssSelector(String.format(".exercise-option\[data-name='%s'\]", exerciseName))  
);  
clickElement(exerciseOption);  
<br/>return this;  
}  
<br/>public WorkoutLoggingPage addSet(double weight, int reps) {  
logger.info("Adding set: {} kg x {} reps", weight, reps);  
<br/>clickElement(addSetButton);  
<br/>// Wait for new set form to appear  
List&lt;WebElement&gt; setForms = driver.findElements(By.cssSelector(".set-form"));  
WebElement latestSetForm = setForms.get(setForms.size() - 1);  
<br/>// Fill in set details  
WebElement weightField = latestSetForm.findElement(By.cssSelector(".weight-input"));  
WebElement repsField = latestSetForm.findElement(By.cssSelector(".reps-input"));  
<br/>clearAndType(weightField, String.valueOf(weight));  
clearAndType(repsField, String.valueOf(reps));  
<br/>return this;  
}  
<br/>public WorkoutLoggingPage addWorkoutNotes(String notes) {  
logger.info("Adding workout notes");  
clearAndType(workoutNotesField, notes);  
return this;  
}  
<br/>public ProgressTrackingPage saveWorkout() {  
logger.info("Saving workout");  
clickElement(saveWorkoutButton);  
<br/>// Wait for save confirmation or navigation  
waitForPageLoad();  
<br/>return new ProgressTrackingPage(driver);  
}  
<br/>// Validation Methods  
public int getSetCount() {  
List&lt;WebElement&gt; sets = driver.findElements(By.cssSelector(".set-form"));  
return sets.size();  
}  
<br/>public boolean isExerciseSelected() {  
return !exerciseSearchField.getAttribute("value").trim().isEmpty();  
}  
<br/>public List&lt;String&gt; getAvailableExercises() {  
clickElement(exerciseSearchField);  
waitForElement(exerciseSuggestions, 3);  
<br/>List&lt;WebElement&gt; exerciseOptions = exerciseSuggestions  
.findElements(By.cssSelector(".exercise-option"));  
<br/>return exerciseOptions.stream()  
.map(WebElement::getText)  
.collect(Collectors.toList());  
}  
}  

**Utility Classes Implementation**

**WaitHelper.java - Comprehensive Wait Strategies**

public class WaitHelper {  
private static final Logger logger = LoggerFactory.getLogger(WaitHelper.class);  
private static final int DEFAULT_TIMEOUT_SECONDS = 10;  
<br/>public static WebElement waitForElementToBeVisible(WebDriver driver, By locator, int timeoutSeconds) {  
logger.debug("Waiting for element to be visible: {}", locator);  
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));  
return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));  
}  
<br/>public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element, int timeoutSeconds) {  
logger.debug("Waiting for element to be clickable");  
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));  
return wait.until(ExpectedConditions.elementToBeClickable(element));  
}  
<br/>public static boolean waitForElementToBeInvisible(WebDriver driver, By locator, int timeoutSeconds) {  
logger.debug("Waiting for element to be invisible: {}", locator);  
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));  
return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));  
}  
<br/>public static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {  
logger.debug("Waiting for page to load");  
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));  
wait.until(webDriver -> ((JavascriptExecutor) webDriver)  
.executeScript("return document.readyState").equals("complete"));  
}  
<br/>public static boolean waitForUrlContains(WebDriver driver, String urlPart, int timeoutSeconds) {  
logger.debug("Waiting for URL to contain: {}", urlPart);  
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));  
return wait.until(ExpectedConditions.urlContains(urlPart));  
}  
<br/>public static void waitForAjaxToComplete(WebDriver driver, int timeoutSeconds) {  
logger.debug("Waiting for AJAX to complete");  
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));  
wait.until(webDriver -> ((JavascriptExecutor) webDriver)  
.executeScript("return jQuery.active == 0"));  
}  
}  

**ScreenshotHelper.java - Test Evidence Capture**

public class ScreenshotHelper {  
private static final Logger logger = LoggerFactory.getLogger(ScreenshotHelper.class);  
private static final String SCREENSHOTS_DIR = "reports/screenshots/";  
<br/>static {  
createScreenshotsDirectory();  
}  
<br/>public static String captureScreenshot(WebDriver driver, String testName) {  
if (driver == null) {  
logger.warn("Driver is null, cannot capture screenshot");  
return null;  
}  
<br/>try {  
TakesScreenshot takesScreenshot = (TakesScreenshot) driver;  
byte\[\] screenshotBytes = takesScreenshot.getScreenshotAs(OutputType.BYTES);  
<br/>String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")  
.format(LocalDateTime.now());  
String fileName = String.format("%s_%s.png", testName, timestamp);  
String filePath = SCREENSHOTS_DIR + fileName;  
<br/>Files.write(Paths.get(filePath), screenshotBytes);  
<br/>logger.info("Screenshot captured: {}", filePath);  
return filePath;  
<br/>} catch (Exception e) {  
logger.error("Failed to capture screenshot", e);  
return null;  
}  
}  
<br/>public static String capturePageSource(WebDriver driver, String testName) {  
try {  
String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")  
.format(LocalDateTime.now());  
String fileName = String.format("%s_pagesource_%s.html", testName, timestamp);  
String filePath = SCREENSHOTS_DIR + fileName;  
<br/>String pageSource = driver.getPageSource();  
Files.write(Paths.get(filePath), pageSource.getBytes());  
<br/>logger.info("Page source captured: {}", filePath);  
return filePath;  
<br/>} catch (Exception e) {  
logger.error("Failed to capture page source", e);  
return null;  
}  
}  
<br/>private static void createScreenshotsDirectory() {  
try {  
Files.createDirectories(Paths.get(SCREENSHOTS_DIR));  
} catch (IOException e) {  
logger.error("Failed to create screenshots directory", e);  
}  
}  
}  

**Test Implementation Examples**

**LoginTests.java - Authentication Test Suite**

public class LoginTests extends BaseTest {  
private static final Logger logger = LoggerFactory.getLogger(LoginTests.class);  
private LoginPage loginPage;  
<br/>@BeforeMethod  
@Override  
public void setUp(Method method) {  
super.setUp(method);  
loginPage = new LoginPage(driver);  
}  
<br/>@Test(groups = {"smoke", "authentication"}, priority = 1)  
public void testValidLogin() {  
logger.info("Testing valid login functionality");  
<br/>// Test data  
String email = "test@example.com";  
String password = "SecurePass123!";  
<br/>// Execute login  
DashboardPage dashboardPage = loginPage.loginWithCredentials(email, password);  
<br/>// Assertions  
Assert.assertTrue(dashboardPage.isDashboardLoaded(),  
"Dashboard should be loaded after successful login");  
Assert.assertTrue(dashboardPage.isUserLoggedIn(),  
"User should be logged in");  
<br/>logger.info("Valid login test completed successfully");  
}  
<br/>@Test(groups = {"regression", "authentication"}, dataProvider = "invalidLoginData",  
dataProviderClass = TestDataProvider.class)  
public void testInvalidLogin(String email, String password, String expectedError) {  
logger.info("Testing invalid login with email: {} and password: {}", email, password);  
<br/>// Execute invalid login  
loginPage.loginWithInvalidCredentials(email, password);  
<br/>// Assertions  
Assert.assertTrue(loginPage.isErrorMessageDisplayed(),  
"Error message should be displayed for invalid login");  
Assert.assertTrue(loginPage.getErrorMessage().contains(expectedError),  
"Error message should contain expected text");  
<br/>logger.info("Invalid login test completed for email: {}", email);  
}  
<br/>@Test(groups = {"smoke", "authentication"})  
public void testNavigationToSignUp() {  
logger.info("Testing navigation to sign up page");  
<br/>// Navigate to sign up  
RegistrationPage registrationPage = loginPage.navigateToSignUp();  
<br/>// Assertions  
Assert.assertTrue(registrationPage.isRegistrationPageLoaded(),  
"Registration page should be loaded");  
<br/>logger.info("Navigation to sign up test completed successfully");  
}  
<br/>@Test(groups = {"accessibility"})  
public void testLoginPageAccessibility() {  
logger.info("Testing login page accessibility");  
<br/>// Verify page structure  
Assert.assertTrue(loginPage.isLoginPageLoaded(),  
"Login page should be properly loaded");  
Assert.assertEquals(loginPage.getPageTitle(), "GymGuard - Login",  
"Page title should be correct");  
<br/>logger.info("Login page accessibility test completed");  
}  
}  

**WorkoutCreationTests.java - Core Functionality Test Suite**

public class WorkoutCreationTests extends BaseTest {  
private static final Logger logger = LoggerFactory.getLogger(WorkoutCreationTests.class);  
private WorkoutLoggingPage workoutPage;  
private UserData testUser;  
<br/>@BeforeMethod  
@Override  
public void setUp(Method method) {  
super.setUp(method);  
<br/>// Create test user and login  
testUser = TestDataProvider.createRandomUser();  
<br/>// Navigate through login flow  
LoginPage loginPage = new LoginPage(driver);  
DashboardPage dashboardPage = loginPage.loginWithCredentials(  
testUser.getEmail(), testUser.getPassword());  
<br/>// Navigate to workout logging  
workoutPage = dashboardPage.navigateToWorkoutLogging();  
}  
<br/>@Test(groups = {"smoke", "workout"}, priority = 1)  
public void testBasicWorkoutCreation() {  
logger.info("Testing basic workout creation");  
<br/>// Test data  
String workoutDate = DateTimeHelper.getCurrentDate();  
String exerciseName = "Barbell Squat";  
double weight = 80.0;  
int reps = 10;  
<br/>// Create workout  
ProgressTrackingPage progressPage = workoutPage  
.setWorkoutDate(workoutDate)  
.selectExercise(exerciseName)  
.addSet(weight, reps)  
.addWorkoutNotes("Great workout session")  
.saveWorkout();  
<br/>// Assertions  
Assert.assertTrue(progressPage.isWorkoutSaved(),  
"Workout should be saved successfully");  
Assert.assertTrue(progressPage.getWorkoutHistory().contains(exerciseName),  
"Workout history should contain the exercise");  
<br/>logger.info("Basic workout creation test completed successfully");  
}  
<br/>@Test(groups = {"regression", "workout"}, dataProvider = "workoutData",  
dataProviderClass = TestDataProvider.class)  
public void testWorkoutCreationWithMultipleSets(WorkoutData workoutData) {  
logger.info("Testing workout creation with multiple sets");  
<br/>// Set workout metadata  
workoutPage.setWorkoutDate(workoutData.getDate());  
<br/>// Add each exercise and its sets  
for (WorkoutData.ExerciseSetData setData : workoutData.getSets()) {  
workoutPage.selectExercise(setData.getExerciseName())  
.addSet(setData.getWeight(), setData.getReps());  
}  
<br/>// Save workout  
ProgressTrackingPage progressPage = workoutPage  
.addWorkoutNotes(workoutData.getNotes())  
.saveWorkout();  
<br/>// Assertions  
Assert.assertTrue(progressPage.isWorkoutSaved(),  
"Multi-set workout should be saved successfully");  
Assert.assertEquals(progressPage.getLatestWorkoutSetCount(),  
workoutData.getSets().size(),  
"Set count should match input data");  
<br/>logger.info("Multi-set workout creation test completed successfully");  
}  
<br/>@Test(groups = {"negative", "workout"})  
public void testWorkoutCreationWithoutExercise() {  
logger.info("Testing workout creation without selecting exercise");  
<br/>// Try to save workout without selecting exercise  
workoutPage.setWorkoutDate(DateTimeHelper.getCurrentDate())  
.saveWorkout();  
<br/>// Assertions  
Assert.assertTrue(workoutPage.isValidationErrorDisplayed(),  
"Validation error should be displayed");  
Assert.assertTrue(workoutPage.getValidationMessage()  
.contains("Please select at least one exercise"),  
"Error message should indicate missing exercise");  
<br/>logger.info("Workout creation without exercise test completed");  
}  
}  

**Configuration Management**

**application.properties - Environment Configuration**

\# Application Configuration  
app.base.url=http://localhost:3000  
app.api.base.url=http://localhost:8080/api  
app.name=GymGuard  
app.version=1.0.0  
<br/>\# Browser Configuration  
browser.default=chrome  
browser.headless=false  
browser.maximize=true  
browser.incognito=false  
<br/>\# Timeout Configuration (in seconds)  
timeout.implicit=5  
timeout.explicit=10  
timeout.page.load=30  
timeout.script=30  
<br/>\# Test Data Configuration  
testdata.base.path=src/test/resources/testdata  
testdata.users.file=user-data.json  
testdata.exercises.file=exercise-data.json  
testdata.workouts.file=workout-data.json  
<br/>\# Reporting Configuration  
reports.base.path=reports  
reports.screenshots.enabled=true  
reports.page.source.enabled=false  
reports.video.enabled=false  
<br/>\# Environment Configuration  
environment.name=development  
database.url=jdbc:postgresql://localhost:5432/gymguard  
database.username=${DB_USERNAME:testuser}  
database.password=${DB_PASSWORD:testpass}  
<br/>\# Test Execution Configuration  
parallel.execution.enabled=false  
thread.count=3  
retry.failed.tests=2  
retry.count.on.failure=1  
<br/>\# Logging Configuration  
logging.level.root=INFO  
logging.level.com.gymguard=DEBUG  
logging.file.name=logs/gymguard-tests.log  

**TestNG Suite Configuration**

**testng.xml - Test Execution Configuration:**

&lt;?xml version="1.0" encoding="UTF-8"?&gt;  
&lt;suite name="GymGuardUI_Test_Suite" parallel="methods" thread-count="3"&gt;  
<br/>&lt;parameter name="browser" value="chrome"/&gt;  
&lt;parameter name="environment" value="development"/&gt;  
<br/>&lt;listeners&gt;  
&lt;listener class-name="com.gymguard.framework.base.TestListener"/&gt;  
&lt;/listeners&gt;  
<br/>&lt;test name="Smoke_Tests" group-by-instances="true"&gt;  
&lt;groups&gt;  
&lt;run&gt;  
&lt;include name="smoke"/&gt;  
&lt;/run&gt;  
&lt;/groups&gt;  
<br/>&lt;classes&gt;  
&lt;class name="com.gymguard.tests.authentication.LoginTests"/&gt;  
&lt;class name="com.gymguard.tests.workout.WorkoutCreationTests"/&gt;  
&lt;class name="com.gymguard.tests.progress.ProgressTrackingTests"/&gt;  
&lt;/classes&gt;  
&lt;/test&gt;  
<br/>&lt;test name="Regression_Tests"&gt;  
&lt;groups&gt;  
&lt;run&gt;  
&lt;include name="regression"/&gt;  
&lt;/run&gt;  
&lt;/groups&gt;  
<br/>&lt;classes&gt;  
&lt;class name="com.gymguard.tests.authentication.LoginTests"/&gt;  
&lt;class name="com.gymguard.tests.authentication.RegistrationTests"/&gt;  
&lt;class name="com.gymguard.tests.authentication.ProfileManagementTests"/&gt;  
&lt;class name="com.gymguard.tests.workout.WorkoutCreationTests"/&gt;  
&lt;class name="com.gymguard.tests.workout.ExerciseLoggingTests"/&gt;  
&lt;class name="com.gymguard.tests.workout.SetManagementTests"/&gt;  
&lt;class name="com.gymguard.tests.workout.WorkoutCompletionTests"/&gt;  
&lt;class name="com.gymguard.tests.progress.HistoryViewTests"/&gt;  
&lt;class name="com.gymguard.tests.progress.PersonalRecordsTests"/&gt;  
&lt;class name="com.gymguard.tests.progress.ProgressVisualizationTests"/&gt;  
&lt;/classes&gt;  
&lt;/test&gt;  
<br/>&lt;test name="Integration_Tests"&gt;  
&lt;groups&gt;  
&lt;run&gt;  
&lt;include name="integration"/&gt;  
&lt;/run&gt;  
&lt;/groups&gt;  
<br/>&lt;classes&gt;  
&lt;class name="com.gymguard.tests.integration.EndToEndWorkflowTests"/&gt;  
&lt;class name="com.gymguard.tests.integration.DataPersistenceTests"/&gt;  
&lt;/classes&gt;  
&lt;/test&gt;  
<br/>&lt;/suite&gt;  

**Maven Configuration**

**pom.xml - Project Dependencies and Build Configuration**

&lt;?xml version="1.0" encoding="UTF-8"?&gt;  
<project xmlns="http://maven.apache.org/POM/4.0.0"  
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0  
http://maven.apache.org/xsd/maven-4.0.0.xsd">  
<br/>&lt;modelVersion&gt;4.0.0&lt;/modelVersion&gt;  
<br/>&lt;!-- Project Information --&gt;  
&lt;groupId&gt;com.gymguard&lt;/groupId&gt;  
&lt;artifactId&gt;gymguardui-test-framework&lt;/artifactId&gt;  
&lt;version&gt;1.0.0&lt;/version&gt;  
&lt;packaging&gt;jar&lt;/packaging&gt;  
<br/>&lt;name&gt;GymGuardUI Test Automation Framework&lt;/name&gt;  
&lt;description&gt;Comprehensive GUI test automation framework for GymGuard application&lt;/description&gt;  
<br/>&lt;!-- Properties --&gt;  
&lt;properties&gt;  
&lt;maven.compiler.source&gt;17&lt;/maven.compiler.source&gt;  
&lt;maven.compiler.target&gt;17&lt;/maven.compiler.target&gt;  
&lt;project.build.sourceEncoding&gt;UTF-8&lt;/project.build.sourceEncoding&gt;  
<br/>&lt;!-- Dependency Versions --&gt;  
&lt;selenium.version&gt;4.15.0&lt;/selenium.version&gt;  
&lt;testng.version&gt;7.8.0&lt;/testng.version&gt;  
&lt;webdrivermanager.version&gt;5.6.2&lt;/webdrivermanager.version&gt;  
&lt;lombok.version&gt;1.18.30&lt;/lombok.version&gt;  
&lt;jackson.version&gt;2.16.0&lt;/jackson.version&gt;  
&lt;slf4j.version&gt;2.0.9&lt;/slf4j.version&gt;  
&lt;logback.version&gt;1.4.14&lt;/logback.version&gt;  
&lt;apache.poi.version&gt;5.2.5&lt;/apache.poi.version&gt;  
&lt;allure.version&gt;2.24.0&lt;/allure.version&gt;  
<br/>&lt;!-- Plugin Versions --&gt;  
&lt;maven.compiler.plugin.version&gt;3.11.0&lt;/maven.compiler.plugin.version&gt;  
&lt;maven.surefire.plugin.version&gt;3.2.2&lt;/maven.surefire.plugin.version&gt;  
&lt;allure.maven.plugin.version&gt;2.12.0&lt;/allure.maven.plugin.version&gt;  
&lt;/properties&gt;  
<br/>&lt;!-- Dependencies --&gt;  
&lt;dependencies&gt;  
&lt;!-- Selenium WebDriver --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;org.seleniumhq.selenium&lt;/groupId&gt;  
&lt;artifactId&gt;selenium-java&lt;/artifactId&gt;  
&lt;version&gt;${selenium.version}&lt;/version&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- TestNG --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;org.testng&lt;/groupId&gt;  
&lt;artifactId&gt;testng&lt;/artifactId&gt;  
&lt;version&gt;${testng.version}&lt;/version&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- WebDriverManager --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;io.github.bonigarcia&lt;/groupId&gt;  
&lt;artifactId&gt;webdrivermanager&lt;/artifactId&gt;  
&lt;version&gt;${webdrivermanager.version}&lt;/version&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- Lombok --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;org.projectlombok&lt;/groupId&gt;  
&lt;artifactId&gt;lombok&lt;/artifactId&gt;  
&lt;version&gt;${lombok.version}&lt;/version&gt;  
&lt;scope&gt;provided&lt;/scope&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- Jackson for JSON Processing --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;com.fasterxml.jackson.core&lt;/groupId&gt;  
&lt;artifactId&gt;jackson-databind&lt;/artifactId&gt;  
&lt;version&gt;${jackson.version}&lt;/version&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- SLF4J Logging --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;org.slf4j&lt;/groupId&gt;  
&lt;artifactId&gt;slf4j-api&lt;/artifactId&gt;  
&lt;version&gt;${slf4j.version}&lt;/version&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- Logback Implementation --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;ch.qos.logback&lt;/groupId&gt;  
&lt;artifactId&gt;logback-classic&lt;/artifactId&gt;  
&lt;version&gt;${logback.version}&lt;/version&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- Apache POI for Excel --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;org.apache.poi&lt;/groupId&gt;  
&lt;artifactId&gt;poi-ooxml&lt;/artifactId&gt;  
&lt;version&gt;${apache.poi.version}&lt;/version&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- Allure TestNG --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;io.qameta.allure&lt;/groupId&gt;  
&lt;artifactId&gt;allure-testng&lt;/artifactId&gt;  
&lt;version&gt;${allure.version}&lt;/version&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- PostgreSQL Driver for Database Testing --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;org.postgresql&lt;/groupId&gt;  
&lt;artifactId&gt;postgresql&lt;/artifactId&gt;  
&lt;version&gt;42.7.1&lt;/version&gt;  
&lt;scope&gt;test&lt;/scope&gt;  
&lt;/dependency&gt;  
<br/>&lt;!-- REST Assured for API Testing --&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;io.rest-assured&lt;/groupId&gt;  
&lt;artifactId&gt;rest-assured&lt;/artifactId&gt;  
&lt;version&gt;5.4.0&lt;/version&gt;  
&lt;scope&gt;test&lt;/scope&gt;  
&lt;/dependency&gt;  
<br/>&lt;/dependencies&gt;  
<br/>&lt;!-- Build Configuration --&gt;  
&lt;build&gt;  
&lt;plugins&gt;  
&lt;!-- Maven Compiler Plugin --&gt;  
&lt;plugin&gt;  
&lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;  
&lt;artifactId&gt;maven-compiler-plugin&lt;/artifactId&gt;  
&lt;version&gt;${maven.compiler.plugin.version}&lt;/version&gt;  
&lt;configuration&gt;  
&lt;source&gt;17&lt;/source&gt;  
&lt;target&gt;17&lt;/target&gt;  
&lt;encoding&gt;UTF-8&lt;/encoding&gt;  
&lt;/configuration&gt;  
&lt;/plugin&gt;  
<br/>&lt;!-- Maven Surefire Plugin --&gt;  
&lt;plugin&gt;  
&lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;  
&lt;artifactId&gt;maven-surefire-plugin&lt;/artifactId&gt;  
&lt;version&gt;${maven.surefire.plugin.version}&lt;/version&gt;  
&lt;configuration&gt;  
&lt;suiteXmlFiles&gt;  
&lt;suiteXmlFile&gt;testng.xml&lt;/suiteXmlFile&gt;  
&lt;/suiteXmlFiles&gt;  
&lt;argLine&gt;  
\-javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/1.9.20.1/aspectjweaver-1.9.20.1.jar"  
&lt;/argLine&gt;  
&lt;systemProperties&gt;  
&lt;property&gt;  
&lt;name&gt;allure.results.directory&lt;/name&gt;  
&lt;value&gt;${project.build.directory}/allure-results&lt;/value&gt;  
&lt;/property&gt;  
&lt;/systemProperties&gt;  
&lt;/configuration&gt;  
&lt;dependencies&gt;  
&lt;dependency&gt;  
&lt;groupId&gt;org.aspectj&lt;/groupId&gt;  
&lt;artifactId&gt;aspectjweaver&lt;/artifactId&gt;  
&lt;version&gt;1.9.20.1&lt;/version&gt;  
&lt;/dependency&gt;  
&lt;/dependencies&gt;  
&lt;/plugin&gt;  
<br/>&lt;!-- Allure Maven Plugin --&gt;  
&lt;plugin&gt;  
&lt;groupId&gt;io.qameta.allure&lt;/groupId&gt;  
&lt;artifactId&gt;allure-maven&lt;/artifactId&gt;  
&lt;version&gt;${allure.maven.plugin.version}&lt;/version&gt;  
&lt;configuration&gt;  
&lt;resultsDirectory&gt;${project.build.directory}/allure-results&lt;/resultsDirectory&gt;  
&lt;reportDirectory&gt;${project.build.directory}/allure-reports&lt;/reportDirectory&gt;  
&lt;/configuration&gt;  
&lt;/plugin&gt;  
<br/>&lt;/plugins&gt;  
&lt;/build&gt;  
<br/>&lt;!-- Profiles for Different Environments --&gt;  
&lt;profiles&gt;  
&lt;profile&gt;  
&lt;id&gt;development&lt;/id&gt;  
&lt;activation&gt;  
&lt;activeByDefault&gt;true&lt;/activeByDefault&gt;  
&lt;/activation&gt;  
&lt;properties&gt;  
&lt;env&gt;development&lt;/env&gt;  
&lt;app.base.url&gt;http://localhost:3000&lt;/app.base.url&gt;  
&lt;/properties&gt;  
&lt;/profile&gt;  
<br/>&lt;profile&gt;  
&lt;id&gt;staging&lt;/id&gt;  
&lt;properties&gt;  
&lt;env&gt;staging&lt;/env&gt;  
&lt;app.base.url&gt;http://staging.gymguard.com&lt;/app.base.url&gt;  
&lt;/properties&gt;  
&lt;/profile&gt;  
<br/>&lt;profile&gt;  
&lt;id&gt;production&lt;/id&gt;  
&lt;properties&gt;  
&lt;env&gt;production&lt;/env&gt;  
&lt;app.base.url&gt;http://gymguard.com&lt;/app.base.url&gt;  
&lt;/properties&gt;  
&lt;/profile&gt;  
<br/>&lt;profile&gt;  
&lt;id&gt;headless&lt;/id&gt;  
&lt;properties&gt;  
&lt;browser.headless&gt;true&lt;/browser.headless&gt;  
&lt;/properties&gt;  
&lt;/profile&gt;  
&lt;/profiles&gt;  
<br/>&lt;/project&gt;  

**Error Handling and Validation Strategies**

**Exception Handling Implementation**

**Custom Exception Classes:**

// Base Framework Exception  
public class FrameworkException extends RuntimeException {  
public FrameworkException(String message) {  
super(message);  
}  
<br/>public FrameworkException(String message, Throwable cause) {  
super(message, cause);  
}  
}  
<br/>// Element Not Found Exception  
public class ElementNotFoundException extends FrameworkException {  
public ElementNotFoundException(String message) {  
super("Element not found: " + message);  
}  
}  
<br/>// Page Load Exception  
public class PageLoadException extends FrameworkException {  
public PageLoadException(String pageName) {  
super("Failed to load page: " + pageName);  
}  
<br/>public PageLoadException(String pageName, Throwable cause) {  
super("Failed to load page: " + pageName, cause);  
}  
}  
<br/>// Test Data Exception  
public class TestDataException extends FrameworkException {  
public TestDataException(String message) {  
super("Test data error: " + message);  
}  
<br/>public TestDataException(String message, Throwable cause) {  
super("Test data error: " + message, cause);  
}  
}  

**Validation Utilities**

**ValidationHelper.java - Comprehensive Validation Methods:**

public class ValidationHelper {  
private static final Logger logger = LoggerFactory.getLogger(ValidationHelper.class);  
<br/>public static void validateNotNull(Object object, String parameterName) {  
if (object == null) {  
throw new IllegalArgumentException(parameterName + " cannot be null");  
}  
}  
<br/>public static void validateNotEmpty(String string, String parameterName) {  
if (string == null || string.trim().isEmpty()) {  
throw new IllegalArgumentException(parameterName + " cannot be null or empty");  
}  
}  
<br/>public static void validateEmailFormat(String email) {  
String emailRegex = "^\[a-zA-Z0-9.\_%+-\]+@\[a-zA-Z0-9.-\]+\\\\.\[a-zA-Z\]{2,}$";  
if (!email.matches(emailRegex)) {  
throw new IllegalArgumentException("Invalid email format: " + email);  
}  
}  
<br/>public static void validatePositiveNumber(double number, String parameterName) {  
if (number <= 0) {  
throw new IllegalArgumentException(parameterName + " must be positive");  
}  
}  
<br/>public static void validateDateFormat(String date, String format) {  
try {  
DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);  
LocalDate.parse(date, formatter);  
} catch (DateTimeParseException e) {  
throw new IllegalArgumentException("Invalid date format: " + date +  
". Expected format: " + format);  
}  
}  
}  

**Performance Optimization Strategies**

**Test Execution Performance**

**Parallel Execution Configuration:**

// TestNG Configuration for Parallel Execution  
@Test(threadPoolSize = 3, invocationCount = 1)  
public class ParallelExecutionTests extends BaseTest {  
<br/>@BeforeMethod  
@Override  
public void setUp(Method method) {  
super.setUp(method);  
logger.info("Thread: {} - Setting up test: {}",  
Thread.currentThread().getName(), method.getName());  
}  
<br/>@Test(groups = "parallel")  
public void testParallelExecution1() {  
// Test implementation  
}  
<br/>@Test(groups = "parallel")  
public void testParallelExecution2() {  
// Test implementation  
}  
}  

**Resource Management:**

public class ResourceManager {  
private static final int MAX_DRIVER_INSTANCES = 5;  
private static final BlockingQueue&lt;WebDriver&gt; driverPool =  
new ArrayBlockingQueue<>(MAX_DRIVER_INSTANCES);  
<br/>static {  
initializeDriverPool();  
}  
<br/>private static void initializeDriverPool() {  
for (int i = 0; i < MAX_DRIVER_INSTANCES; i++) {  
driverPool.offer(DriverFactory.createDriver());  
}  
}  
<br/>public static WebDriver getDriver() {  
try {  
return driverPool.take(); // Blocks if no driver available  
} catch (InterruptedException e) {  
Thread.currentThread().interrupt();  
throw new RuntimeException("Interrupted while waiting for driver", e);  
}  
}  
<br/>public static void releaseDriver(WebDriver driver) {  
if (driver != null) {  
// Reset driver state  
driver.manage().deleteAllCookies();  
driverPool.offer(driver);  
}  
}  
}  

**Reporting and Analytics**

**TestNG Listener Implementation**

**TestListener.java - Comprehensive Test Event Handling:**

public class TestListener implements ITestListener, ISuiteListener, IInvokedMethodListener {  
private static final Logger logger = LoggerFactory.getLogger(TestListener.class);  
<br/>@Override  
public void onStart(ISuite suite) {  
logger.info("Starting test suite: {}", suite.getName());  
}  
<br/>@Override  
public void onFinish(ISuite suite) {  
logger.info("Finished test suite: {}", suite.getName());  
}  
<br/>@Override  
public void onTestStart(ITestResult result) {  
logger.info("Starting test: {}.{}",  
result.getTestClass().getName(),  
result.getMethod().getMethodName());  
}  
<br/>@Override  
public void onTestSuccess(ITestResult result) {  
logger.info("Test passed: {}.{}",  
result.getTestClass().getName(),  
result.getMethod().getMethodName());  
}  
<br/>@Override  
public void onTestFailure(ITestResult result) {  
logger.error("Test failed: {}.{}",  
result.getTestClass().getName(),  
result.getMethod().getMethodName());  
<br/>// Capture screenshot for failed test  
WebDriver driver = DriverManager.getDriver();  
if (driver != null) {  
String screenshotPath = ScreenshotHelper.captureScreenshot(  
driver, result.getMethod().getMethodName());  
if (screenshotPath != null) {  
// Attach screenshot to Allure report  
attachScreenshotToAllure(screenshotPath);  
}  
}  
}  
<br/>@Override  
public void onTestSkipped(ITestResult result) {  
logger.warn("Test skipped: {}.{}",  
result.getTestClass().getName(),  
result.getMethod().getMethodName());  
}  
<br/>private void attachScreenshotToAllure(String screenshotPath) {  
try {  
byte\[\] screenshot = Files.readAllBytes(Paths.get(screenshotPath));  
Allure.addAttachment("Screenshot", "image/png",  
new ByteArrayInputStream(screenshot), ".png");  
} catch (IOException e) {  
logger.error("Failed to attach screenshot to Allure report", e);  
}  
}  
}  

**Quality Assurance and Best Practices**

**Code Quality Guidelines**

**Coding Standards:**

- Use meaningful class, method, and variable names
- Implement proper exception handling with custom exceptions
- Follow DRY (Don't Repeat Yourself) principle
- Use design patterns appropriately
- Maintain comprehensive logging
- Implement proper wait strategies (no Thread.sleep())

**Test Design Principles:**

- Each test should be independent and atomic
- Tests should be deterministic and repeatable
- Use appropriate test data management
- Implement proper assertions with meaningful messages
- Follow AAA pattern (Arrange, Act, Assert)
- Use groups and priorities for test organization

**Maintenance Guidelines**

**Framework Maintenance:**

- Regular dependency updates
- Code refactoring for technical debt reduction
- Performance monitoring and optimization
- Documentation updates
- Test suite health monitoring

**Test Maintenance:**

- Regular review of test coverage
- Flaky test identification and resolution
- Test data cleanup and management
- Assertion refinement and improvement
- Cross-browser compatibility validation

**Deployment and CI/CD Integration**

**GitHub Actions Workflow Example**

**.github/workflows/test-execution.yml:**

name: GymGuardUI Test Execution  
<br/>on:  
push:  
branches: \[ main, develop \]  
pull_request:  
branches: \[ main \]  
schedule:  
\- cron: '0 2 \* \* \*' # Daily at 2 AM  
<br/>jobs:  
test:  
runs-on: ubuntu-latest  
<br/>strategy:  
matrix:  
browser: \[chrome, firefox\]  
<br/>steps:  
\- name: Checkout code  
uses: actions/checkout@v4  
<br/>\- name: Set up Java 17  
uses: actions/setup-java@v4  
with:  
java-version: '17'  
distribution: 'temurin'  
<br/>\- name: Cache Maven dependencies  
uses: actions/cache@v3  
with:  
path: ~/.m2  
key: ${{ runner.os }}-m2-${{ hashFiles('\*\*/pom.xml') }}  
<br/>\- name: Install Chrome  
uses: browser-actions/setup-chrome@latest  
<br/>\- name: Install Firefox  
uses: browser-actions/setup-firefox@latest  
<br/>\- name: Run tests  
run: mvn clean test -Dbrowser=${{ matrix.browser }} -Dheadless=true  
env:  
DB_USERNAME: ${{ secrets.DB_USERNAME }}  
DB_PASSWORD: ${{ secrets.DB_PASSWORD }}  
<br/>\- name: Generate Allure Report  
if: always()  
run: mvn allure:report  
<br/>\- name: Upload Test Results  
if: always()  
uses: actions/upload-artifact@v3  
with:  
name: test-results-${{ matrix.browser }}  
path: |  
target/allure-results  
target/allure-reports  
reports/screenshots  

**Conclusion**

This Low-Level Design document provides a comprehensive blueprint for implementing the GymGuardUI test automation framework. The design emphasizes:

1.  **Scalability**: Modular architecture supporting easy expansion
2.  **Maintainability**: Clear separation of concerns and standardized patterns
3.  **Reliability**: Robust error handling and validation strategies
4.  **Performance**: Optimized execution with parallel testing capabilities
5.  **Quality**: Comprehensive reporting and continuous improvement processes

The framework follows industry best practices while being specifically tailored for the strength training application's unique requirements. The implementation provides immediate value through the MVP approach while maintaining extensibility for future enhancements and feature additions.

The detailed technical specifications, code examples, and configuration templates serve as a practical guide for development teams to build a robust, scalable, and maintainable test automation solution that will grow with the application and testing requirements over time.