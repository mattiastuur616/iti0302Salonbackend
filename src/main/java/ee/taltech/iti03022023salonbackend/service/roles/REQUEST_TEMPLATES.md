# Request templates
 Different templates of JSON file formats to use when adding a new
 instances of objects into the database.

### Adding the service
Example JSON file, which will add a new service to the database:
 - {
   "name": "Hair cutting",
   "price": 50,
   "serviceType": {
   "typeId": 5
   },
   "duration": 30,
   "startingTime": "2023-07-19",
   "serviceStatus": {
   "statusId": 1
   },
   "cosmetic": {
   "cosmeticId": 1
   }
   }

### Adding a new client
Example JSON file, which will add a new client to the database:
 - {
   "firstName": "Mattias",
   "lastName": "Tüür",
   "money": 500,
   "phoneNumber": "5361 8143",
   "email": "mattiastuur@gmail.com",
   "idCode": "50205160274",
   "dateOfBirth": "2002-05-16",
   "homeAddress": "Makrilli tee 9-1"
   }

### Adding a new cosmetic
Example JSON file, which will add a new cosmetic to the database:
- {
  "firstName": "Carmen",
  "lastName": "Karjane",
  "phoneNumber": "1445 6789",
  "email": "carmen.karjane@gmail.com"
  }
