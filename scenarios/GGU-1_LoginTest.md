# Title

Login Test

**Reference:** GGU-1

## Description

Verify that a registered user can successfully log in using valid credentials. The test checks that the login form is present, accepts input, and that a successful login redirects the user to a protected/dashboard page.

## Preconditions

- Test user ("alamakota@gmail.com") exists with valid credentials.
- Application is running and reachable.

## Test Steps

1. Navigate to the application base URL.
	 - Action: Open the application's base URL in the browser.
	 - Assertions:
		 - The login page is displayed.
		 - The email and password input fields are visible and enabled.
		 - The login/submit button is visible.

2. Enter valid credentials.
	 - Action: Populate the email and password fields with valid credentials from the test data.
	 - Assertions:
		 - The email field contains the expected email.
		 - The password field contains hidden value.

3. Submit the login form.
	 - Action: Click the login/submit button.
	 - Assertions:
		 - The user is redirected to the dashboard or another authenticated page (URL contains `/dashboard` or equivalent).
		 - A user-specific element is visible - user name in the upper-right corner of navigation bar.

4. Verify session persistence.
	 - Action: Refresh the page or navigate to a protected route.
	 - Assertions:
		 - The user remains authenticated and the dashboard (or protected content) is still accessible.

Notes:
- Use canonical selectors exposed by the application (for example `data-test-id`) when available to locate elements.
