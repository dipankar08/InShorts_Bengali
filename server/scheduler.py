import requests
#import schedule
import time
import pdb
import datetime
from bs4 import BeautifulSoup
   
def makeabs(a,b):
    #pdb.set_trace()
    b = b.replace('\\\\','//')
    if b.startswith('//'):
        b = 'http:'+ b
    
    if b[0] != '/':
        b =  b
    else:
        b = a[:a.find('/',10)] + b
    return b

def pp(a):
    return a.strip();
    
def gd(a):
    a= a.replace('http://','')
    a= a.replace('https://','')
    a= a.replace('www.','')
    return a[:a.find('.')]
    
url = 'http://52.89.112.230/api/inshortsbengali1'
def submit(ans):
    if not ans: return
    print '[Info] Summiting data ...'
    import json
    for a in ans:
        a['date']= datetime.date.today().strftime("%d/%m/%Y")
    payload = {"_cmd":"insert","_payload":ans}
    headers = {'Content-Type': 'application/json'}
    r = requests.post(url,headers=headers, data=json.dumps(payload))
    print r.text

def grabPratidin(tag, url):
    ans = []
    soup = BeautifulSoup(requests.get(url).text,"html.parser")	
    urls = [ makeabs(url, x.get('href')) for x in soup.select('div.article-container > article > .featured-image > a') if x.has_key('href') and x.get('href')]
    tag += ','+ gd(url)
    for u in urls[::-1]:
        print '[Info]  Processing ',u,'...'
        soup = BeautifulSoup(requests.get(u).text,"html.parser")
        title = soup.select_one('#primary  article header').text
        imgurl = soup.select_one('#primary .featured-image img').get('src')
        fullstory = soup.select_one('#primary .entry-content p').text
        preview = fullstory[:500]
        ans.append({'url':u,  'title':pp(title),'imgurl':makeabs(url,pp(imgurl)),'fullstory':pp(fullstory),"tag":tag,'preview':preview})    
    submit(ans)

def grabEiSamay(tag, url):
    ans = []
    soup = BeautifulSoup(requests.get(url).text,"html.parser")	
    urls = [ makeabs(url, x.get('href')) for x in soup.select('div.mainarticle1> h1 > a') if x.has_key('href') and x.get('href')]
    urls += [ makeabs(url, x.get('href')) for x in soup.select('div.other_main_news1 > ul > li > a') if x.has_key('href') and x.get('href')]   
    tag += ','+ gd(url)
    for u in urls[::-1]:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BeautifulSoup(requests.get(u).text,"html.parser")
            #pdb.set_trace()
            title = soup.select_one('div.leftmain h1').text
            imgurl = soup.select_one('div.leftmain .article img').get('src')
            fullstory = soup.select_one('div.leftmain .article arttextxml .Normal').text
            preview = fullstory[:500]
            ans.append({'url':u,  'title':pp(title),'imgurl':makeabs(url,pp(imgurl)),'fullstory':pp(fullstory),"tag":tag,'preview':preview})
            
        except:
            pass
    submit(ans)

def grabzeenews(tag, url):
    ans = []
    soup = BeautifulSoup(requests.get(url).text,"html.parser")
    #pdb.set_trace()
    urls = [ makeabs(url, x.get('href')) for x in soup.select('div.lead-health-nw > a') if x.has_key('href') and x.get('href')]
    tag += ','+ gd(url)
    for u in urls[::-1]:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BeautifulSoup(requests.get(u).text,"html.parser")
            #pdb.set_trace()
            title = soup.select_one('div.connrtund .full-story-head > h1').text
            imgurl = soup.select_one('div.connrtund .article-image img').get('src')
            fullstory = soup.select_one('div.connrtund div.field-type-text-with-summary p').text
            preview = fullstory[:500]
            ans.append({'url':u, 'title':pp(title),'imgurl':makeabs(url,pp(imgurl)),'fullstory':pp(fullstory), "tag":tag,'preview':preview})
            
        except:
            pass
    submit(ans)
    
def grabebela(tag, url):
    ans = []
    soup = BeautifulSoup(requests.get(url).text,"html.parser")
    #pdb.set_trace()
    urls = [ makeabs(url, x.get('href')) for x in soup.select('div.container_main  .carousel-inner a.mrf-article-image') if x.has_key('href') and x.get('href')]
    tag += ','+ gd(url)
    for u in urls[::-1]:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BeautifulSoup(requests.get(u).text,"html.parser")
            #pdb.set_trace()
            title = soup.select_one('div.container_main .ebela-new-story-section h1').text
            imgurl = soup.select_one('div.container_main .ebela-new-story-section img').get('src')
            fullstory = ''.join([x.text for x in soup.select('div.container_main .ebela-new-story-section > p') if x ])
            preview = fullstory[:500]
            ans.append({'url':u, 'title':pp(title),'imgurl':makeabs(url,pp(imgurl)),'fullstory':pp(fullstory), "tag":tag,'preview':preview})
            
        except:
            pass
    submit(ans)
    
def pratidin():
    grabPratidin("kolkata", "http://www.sangbadpratidin.in/category/kolkata/")
    grabPratidin("state", "http://www.sangbadpratidin.in/category/state/")
    grabPratidin("india", "http://www.sangbadpratidin.in/category/country/")
    grabPratidin("entertainment", "http://www.sangbadpratidin.in/category/film-entertainment//")
    grabPratidin("international", "http://www.sangbadpratidin.in/category/international/")
    grabPratidin("lifestyle", "http://www.sangbadpratidin.in/category/life-style/")
    grabPratidin("siteseeing", "http://www.sangbadpratidin.in/category/toto/")
def eisamay():
    grabEiSamay("kolkata", "http://eisamay.indiatimes.com/city/articlelist/15819618.cms")
    grabEiSamay("india", "http://eisamay.indiatimes.com/nation/articlelist/15819599.cms")
    grabEiSamay("state", "http://eisamay.indiatimes.com/state/articlelist/15819609.cms")
    grabEiSamay("entertainment", "http://eisamay.indiatimes.com/entertainment/articlelist/15819570.cms")
    grabEiSamay("international", "http://eisamay.indiatimes.com/world/articlelist/15819594.cms")
    grabEiSamay("lifestyle", "http://eisamay.indiatimes.com/lifestyle/articlelist/15992436.cms")
    grabEiSamay("game", "http://eisamay.indiatimes.com/sports/articlelist/23000116.cms")
def zeenews():
    grabzeenews("kolkata", "http://zeenews.india.com/bengali/kolkata?pfrom=top-nav")
    grabzeenews("state", "http://zeenews.india.com/bengali/state?pfrom=top-nav")
    grabzeenews("india", "http://zeenews.india.com/bengali/nation?pfrom=top-nav")
    grabzeenews("entertainment", "http://zeenews.india.com/bengali/entertainment?pfrom=top-nav")
    grabzeenews("international", "http://zeenews.india.com/bengali/world?pfrom=top-nav")
    grabzeenews("lifestyle", "http://zeenews.india.com/bengali/lifestyle?pfrom=top-nav")
    grabzeenews("science", "http://zeenews.india.com/bengali/technology?pfrom=top-nav")

def ebela():
    #grabebela("kolkata", "http://zeenews.india.com/bengali/kolkata?pfrom=top-nav")
    grabebela("state", "https://ebela.in/state?ref=entertainment-TopNav")
    grabebela("india", "https://ebela.in/national?ref=strydtl-state-TopNav")
    grabebela("entertainment", "https://ebela.in/entertainment?ref=international-TopNav")
    grabebela("international", "https://ebela.in/international?ref=national-TopNav")
    grabebela("lifestyle", "https://ebela.in/lifestyle?ref=entertainment-TopNav")
    grabebela("science", "https://ebela.in/technology?ref=lifestyle-TopNav")    
pratidin();
eisamay();
zeenews();
ebela();
