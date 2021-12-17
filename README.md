# AndroidClient
Android client for greenhouse project 
## Architecture
The architectural pattern used for project development was MVVM architecture, used to separate the development of the user interface from the development of the back-end logic . The MVVM was chosen because it is the official architecture with first party Android libraries and for the ease of understanding reading and maintaining.
## Technologies
The framework chosen for developing the client application was Android as per course requirements and to provide a native application performance and user experience.
For the communication between the client and the REST-ful web service HTTP protocol was used as the client is not state dependent and a stateless architecture is well suited. The exchange of information was done through JSON objects that are being deserialized into Java Objects 
The project requirements specify a caching procedure to allow use in offline mode, for that, the Room ORM together with SQLite database were used for the ease of implementation and a great support on android devices.
## Design Patterns
During the development design patterns were use, the most notable being:

Builder design pattern was used for the creation of the Client WebAPI classes passing the url, the converter factory and the http interceptor for passing the api-key for the data web service as well as passing the JWT for the android one .

Adapter design pattern was used to transform Java Objects into UI elements such as DeviceListAdapter which transforms a list of devices into a scrollable list of UI cardboards, adapter pattern was also used to display items in a spinner.

Repository pattern is found in the Data Access Objects which provides the access to the user data, as if it was in a local list, while in the back side, the repository changes between local and remote storage as well as makes all the required web-service calls.

Observer is the pattern used to trigger the rerender of UI elements on the data change providing a callback with the modified data source that can be used for update or reinitialisation of the UI components.

## User Interface Sketches
User interface sketches were done in Figma, a free tool for designing user interfaces, 
The team was then following the agreed upon design when implementing the native android layout files in xml format.

