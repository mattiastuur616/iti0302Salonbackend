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