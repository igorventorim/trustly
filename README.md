Trustly Technical challenge - Back-End
===

API that returns the file count, the total number of lines and the total number of bytes grouped by file extension, of a given public Github repository.

* Start project:
docker-compose up

End-points:

 /info-repository/job  [POST]
 description:
  Send job request to the API.
 parameters:
  url - Url repository github
 Example CURL: 
  curl --request POST \
  --url http://localhost/info-repository/job \
  --header 'Content-Type: multipart/form-data; boundary=---011000010111000001101001' \
  --form url=https://github.com/igorventorim/wiki

 /info-repository/job/{id}  [GET]
 description:
  Consult result of submitted job.
 Example CURL:
 curl --request GET \
 --url http://localhost/info-repository/job/1
