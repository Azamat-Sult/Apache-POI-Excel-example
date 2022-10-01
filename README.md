upload Excel file:

POST: localhost:8080/upload

request body: form-data (key = "filename", value = "someData.xlsx
")

get Excel report:

POST: localhost:8080/report

request body:
{
"fields": [
"string2",
"bigDecimal1",
"bigDecimal2"
]
}

Swagger UI: http://localhost:8080/swagger-ui/