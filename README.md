## How to Run

- Make sure you are using JDK 11 and Maven 3.x
- mvn clean package

## About

- API documentation:

```sh
http://localhost:8080/swagger-ui.html
```

### EndPoints
Provides 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints:
- POST <host>/v1/diff/{id}/left
- POST <host>/v1/diff/{id}/right

```sh
Accept: application/json
Content-Type: application/json
{
  "data": "4RDgRXhpZgAATU0AKgAAAAgABAE7AAIAAAAHAAAISo"
}
```
  
Result EndPoint - provide the following info in JSON format
GET <host>/v1/diff/{id}:
 
```sh
Accept: application/json
Content-Type: application/json
{
  "id": 1,
  "areEqual": false,
  "sizesAreEqual": true,
  "diffs": [
    {
      "offset": 1,
      "length": 1
    }
  ]
}
```
The result shows if files are equal, if files have same sizes and if files are not equal highlights where there differences are. In example above files with id 1 are not equal, have same sizes and difference is in 1 character with index 1.

### Implementation details

In-memory H2 database used (console: http://localhost:8080/h2-console credentials in application.properties)
