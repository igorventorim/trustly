Trustly Technical challenge - Back-End
===

API that returns the file count, the total number of lines and the total number of bytes grouped by file extension, of a given public Github repository.

* Start:
docker-compose up

End-points:

 [POST] - /info-repository/job
 parameters:
 url - Url repository github
 Example CURL: 
  curl --request POST \
  --url http://localhost/info-repository/job \
  --header 'Content-Type: multipart/form-data; boundary=---011000010111000001101001' \
  --form url=https://github.com/igorventorim/wiki

 [GET] - /info-repository/job/{id}
 Example CURL:
 curl --request GET \
 --url http://localhost/info-repository/job/1
