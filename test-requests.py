import requests

for i in range(0,5000):
	r = requests.post('http://localhost:8080/info-repository/job', data={'url':'https://github.com/igorventorim/api_ventorim'})
	r = requests.get('http://localhost:8080/info-repository/job/46')
	r = requests.post('http://localhost:8080/info-repository/job', data={'url':'https://github.com/igorventorim/wiki'})
	r = requests.get('http://localhost:8080/info-repository/job/46')
	r = requests.post('http://localhost:8080/info-repository/job', data={'url':'https://github.com/igorventorim/dimension-reduction'})
	r = requests.get('http://localhost:8080/info-repository/job/46')
	

