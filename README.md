# Guard Logger

Libraries Used

 - Unit Test
	 - MockK (https://mockk.io/)
	 - JUnit4
- Database
	- Room
- HTTP
	- Retrofit

Architecture
- Clean Architecture
	- Data Layer - Composed of the DB and Web Service layer
	- Domain Layer - Composed of the Business Logics. Gateway is the dependency of repositories.
	- Presentation Layer - All of the screens

App Screens

 1. Login Screen - Requires the employee id and PIN
 2. Signup Screen - Requires the employee id, full name and PIN
 3. Home Screen - Shows the current weather of your current location.
 4. Log Book Screen
		 - Lists the check in logs of all the employees sorted in descending order
		 - Allows you to add a log
 5. Route Plan Screen
		 - Shows the default locations available
		 - Allows you to drag and drag location upon before starting the route
		 - Allows you to add a log for a location
		 - Allows you to reset the sorting of the locations

For future development:
- Logout feature
- Duty feature

> Written with [StackEdit](https://stackedit.io/).