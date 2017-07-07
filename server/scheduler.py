import requests
#import schedule
import time
import pdb
import datetime
from bs4 import BeautifulSoup
   
def makeabs(a,b):
    return b;
def pp(a):
    return a.strip();
    
url = 'http://52.89.112.230/api/inshortsbengali1'
def submit(ans):
    if not ans: return
    print '[Info] Summiting data ...'
    import json
    payload = {"_cmd":"insert","_payload":ans}
    headers = {'Content-Type': 'application/json'}
    r = requests.post(url,headers=headers, data=json.dumps(payload))
    print r.text

def grabPratidin(tag, url):
    ans = []
    soup = BeautifulSoup(requests.get(url).text)	
    urls = [ makeabs(url, x.get('href')) for x in soup.select('div.article-container > article > .featured-image > a') if x.has_key('href') and x.get('href')]
    for u in urls:
        print '[Info]  Processing ',u,'...'
        soup = BeautifulSoup(requests.get(u).text)
        title = soup.select_one('#primary  article header').text
        imgurl = soup.select_one('#primary .featured-image img').get('src')
        fullstory = soup.select_one('#primary .entry-content p').text
        ans.append({'url':u, 'title':pp(title),'imgurl':pp(imgurl),'fullstory':pp(fullstory),"tag":tag})    
    submit(ans)
    
def pratidin():
    grabPratidin("kolkata", "http://www.sangbadpratidin.in/category/kolkata/")
    grabPratidin("state", "http://www.sangbadpratidin.in/category/state/")
    grabPratidin("india", "http://www.sangbadpratidin.in/category/country/")
    grabPratidin("entertainment", "http://www.sangbadpratidin.in/category/film-entertainment//")
    grabPratidin("international", "http://www.sangbadpratidin.in/category/international/")
    grabPratidin("lifestyle", "http://www.sangbadpratidin.in/category/life-style/")
    grabPratidin("siteseeing", "http://www.sangbadpratidin.in/category/toto/")
    
pratidin();