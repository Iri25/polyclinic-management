# polyclinic-management
Mobile client application developed in Kotlin with server developed in Node.js.

To run the server, execute:
```
npm install
npm start
```

Mobile client application for polyclinic management. A polyclinic uses a mobile application to manage patient bookings. The secretariat confirms the reservations and patients can register a new reservation.

## The following details are stored on the server side:
- Id - Identifier of the reservation. An integer value greater than zero.
- Name - Name of the patient. A string value.
- Doctor - A string representing the name of the doctor.
- Date - An integer that represents the day of the reservation.
- Time - An integer that represents the time of the reservation.
- Details - A string representing the details specified by the patient.
- Status - A Boolean value that specifies whether the reservation is confirmed or not.

## The application offers the following features:

● Secretary section (separate screen).
1. View reservations, in a list. Using the GET/open call, the staff will access all unconfirmed reservations in the system. The elements in the list show: the id, name, doctor, date and time of a reservation that are ordered breeder by doctor, date and time. If the application is offline, and we do not have reservations in local database, a corresponding message and a button to retry the call will be displayed. Once the first call succeeds, only the reservations from the offline/local database will be used, no more such calls are made to the server.
2. Confirm reservation. By selecting a reservation from the previous list, using the POST/confirm call specifying only the id of a reservation, confirmation of a reservation will be requested. Available both online and offline.

● Patient section (separate screen) - only available online.
1. Reservation registration. Using the POST/create call, specifying all the attributes of a reservation. As a response from the server, an entire representative of the reservation id is received. A confirmation message with the response from the server is displayed.
