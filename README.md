# android-list-app

#### About
Sample application for Marley Spoon's challenge. The app uses the API in the Contentful service.
  
#### Structure
The app was developed by Clean Architecture in a modular structure. It contains Domain, Data, Presentation and App modules.
* Domain - This is an abstraction layer and contains most of contracts
* Data - Implements API calls and application persistence.
* Presentation - Application UI layer 
* App module- Connects all of the modules and hold main configurations


#### Technology stack used:
* Coroutines - for asynchronous tasks
* Koin - for dependency injection
* Navigation Component - for navigating through screens
* Contentful SDK - for API calls
* Timber - for logging
* Glide - fo image loading

#### To run app
You can use one of the build types to run the application - **prodDebug**, **devDebug**.
