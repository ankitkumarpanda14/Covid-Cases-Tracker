import bs4 as BeautifulSoup
import pandas as pd
from requests import get
import codecs
from twilio.rest import Client
from firebase import firebase

FBConn = firebase.FirebaseApplication('https://covid19data-4cccb.firebaseio.com/',None)


url = 'https://www.mygov.in/covid-19/'

response = get(url)
fh = codecs.open("covid.html","w","utf8")
fh.write(response.text)
fh.close()

with open("covid.html",encoding = "utf8") as html_file:
	 soup = BeautifulSoup.BeautifulSoup(html_file,'lxml');

covidData = []
for covidTimeClass in soup.find_all('div',class_ = 'information_row'):
	covidTime = covidTimeClass.div.span.text 

for covidClass in soup.find_all('span',class_="icount"):
	covidClassText = covidClass.text
	covidData.append(covidClassText)

print(covidTime)
print(covidData)
# account_sid = 'ACf928388e4f82370250e94b1996e9e5ba'
# auth_token = 'cd3ac77993b2b80f1050664d2840fe5e'
# client = Client(account_sid, auth_token)


# message = client.messages.create(
#                               body = covidTime,
#                               from_='whatsapp:+14155238886',
#                               to='whatsapp:+919938734697'
#                           )

# text1 = ''.join(["Active:",covidData[0]])
# message = client.messages.create(
#                               body = text1,
#                               from_='whatsapp:+14155238886',
#                               to='whatsapp:+919938734697'
#                           )

# text2 = ''.join(["Cured:",covidData[1]])
# message = client.messages.create(

#                               body = text2,
#                               from_='whatsapp:+14155238886',
#                               to='whatsapp:+919938734697'
#                           )

# text3 = ''.join(["Deaths:",covidData[2]])
# message = client.messages.create(

#                               body = text3,
#                               from_='whatsapp:+14155238886',
#                               to='whatsapp:+919938734697'
#                           )
# text4 = ''.join(["Migrated:",covidData[3]])
# message = client.messages.create(
#                               body = text4,
#                               from_='whatsapp:+14155238886',
#                               to='whatsapp:+919938734697'
#                           )

# print(message.sid)
count = 1
while count:
	
	data_to_upload = {
		'Time': covidTime,
		 'Active': str(covidData[0]),
		'Cured': str(covidData[1]),
		'Deaths': str(covidData[2]),
		'Migrated': str(covidData[3])
	}

	result = FBConn.post('/MyTestData',data_to_upload)
	print(result)
	count = count - 1
	