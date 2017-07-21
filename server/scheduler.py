import requests
#import schedule
import time
import pdb
import datetime
from bs4 import BeautifulSoup

def BuildSoup(u):
    #We can try 3 times.
    for i in range(3):
        try:
            return BeautifulSoup(requests.get(u).text,"html.parser")
        except:
            print("Connection refused by the server..")
            print("Let me sleep for 5 seconds")
            print("ZZzzzz...")
            time.sleep(5)
            print("Was a nice sleep, now let me continue...")
            continue
    return None
   
def makeabs(a,b):
    #pdb.set_trace()
    b = b.replace('\\\\','//')
    if b.startswith('//'):
        b = 'http:'+ b
    
    if b.startswith('http'):
        b =  b
    else:
        if b[0] != '/':
            b = '/'+b
        b = a[:a.find('/',10)] + b
    return b

def pp(a):
    return a.strip();
    
def gd(a):
    a= a.replace('http://','')
    a= a.replace('https://','')
    a= a.replace('www.','')
    return a[:a.find('.')]
    
url = 'http://52.89.112.230/api/inshortsbengali'
def submit(ans):
    if not ans: return
    print '[Info] Summiting data ...'
    import json
    for a in ans:
        a['date']= time.strftime('%d/%m/%Y %H:%M:%S',time.gmtime()) #GMT time
    payload = {"_cmd":"insert","_payload":ans}
    headers = {'Content-Type': 'application/json'}
    r = requests.post(url,headers=headers, data=json.dumps(payload))
    print r.text

def grabPratidin(tag, url):
    ans = []
    soup = BuildSoup(url)	
    if not soup: return;
    urls = [ makeabs(url, x.get('href')) for x in soup.select('div.article-container > article > .featured-image > a') if x.has_key('href') and x.get('href')]
    tag += ','+ gd(url)
    for u in urls[::-1][:10]:
        print '[Info]  Processing ',u,'...'
        soup = BuildSoup(u)
        if not soup: continue;
        title = soup.select_one('#primary  article header').text
        imgurl = soup.select_one('#primary .featured-image img').get('src')
        fullstory = soup.select_one('#primary .entry-content p').text
        preview = fullstory[:500]
        ans.append({'url':u,  'title':pp(title),'imgurl':makeabs(url,pp(imgurl)),'fullstory':pp(fullstory),"tag":tag,'preview':preview})    
    submit(ans)

def grabEiSamay(tag, url):
    ans = []
    soup = BuildSoup(url)
    if not soup: return;	
    urls = [ makeabs(url, x.get('href')) for x in soup.select('div.mainarticle1> h1 > a') if x.has_key('href') and x.get('href')]
    urls += [ makeabs(url, x.get('href')) for x in soup.select('div.other_main_news1 > ul > li > a') if x.has_key('href') and x.get('href')]   
    tag += ','+ gd(url)
    for u in urls[::-1][:10]:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BuildSoup(u)
            if not soup: continue;
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
    soup = BuildSoup(url)
    if not soup: return;
    #pdb.set_trace()
    urls = [ makeabs(url, x.get('href')) for x in soup.select('div.lead-health-nw > a') if x.has_key('href') and x.get('href')]
    tag += ','+ gd(url)
    for u in urls[::-1][:10]:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BuildSoup(u)
            if not soup: continue;
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
    soup = BuildSoup(url)
    if not soup: return;
    #pdb.set_trace()
    urls = [ makeabs(url, x.get('href')) for x in soup.select('div.container_main  .carousel-inner a.mrf-article-image') if x.has_key('href') and x.get('href')]
    tag += ','+ gd(url)
    for u in urls[::-1][:10]:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BuildSoup(u)
            if not soup: continue;
            #pdb.set_trace()
            title = soup.select_one('div.container_main .ebela-new-story-section h1').text
            imgurl = soup.select_one('div.container_main .ebela-new-story-section img').get('src')
            fullstory = ''.join([x.text for x in soup.select('div.container_main .ebela-new-story-section > p') if x ])
            preview = fullstory[:500]
            ans.append({'url':u, 'title':pp(title),'imgurl':makeabs(url,pp(imgurl)),'fullstory':pp(fullstory), "tag":tag,'preview':preview})
            
        except:
            pass
    submit(ans)

def grabsongbaad(tag, url):
    ans = []
    soup = BuildSoup(url)
    if not soup: return;
    #pdb.set_trace()
    urls = [ makeabs(url, x.get('href')) for x in soup.select('.content .latestnews1 .fimage1 > a') if x.has_key('href') and x.get('href')]
    tag += ','+ gd(url)
    for u in urls[::-1][:10]:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BuildSoup(u)
            if not soup: continue;
            #pdb.set_trace()
            title = soup.select_one('article  > h1').text
            imgurl = soup.select_one('article .entry img').get('src')
            fullstory = ''.join([x.text for x in soup.select('article .entry > p') if x ])
            preview = fullstory[:500]
            ans.append({'url':u, 'title':pp(title),'imgurl':makeabs(url,pp(imgurl)),'fullstory':pp(fullstory), "tag":tag,'preview':preview})
            
        except:
            pass
    submit(ans)
    
def grabbartamanpatrika(tag, url):
    ans = []
    soup = BuildSoup(url)
    if not soup: return;
    #pdb.set_trace()
    urls = [ makeabs(url, x.get('href')) for x in soup.select('.firstSection  a.bisad') if x.has_key('href') and x.get('href')]
    tag += ','+ gd(url)
    for u in urls[::-1][:10]:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BuildSoup(u)
            if not soup: continue;
            #pdb.set_trace()
            title = soup.select_one('.firstSection .head-news  > h4 strong').text
            if soup.select_one('.firstSection .head-news  img'):
                imgurl = soup.select_one('.firstSection .head-news  img').get('src')
            fullstory = ''.join([x.text for x in soup.select('.firstSection .head-news  .content') if x ])
            preview = fullstory[:500]
            ans.append({'url':u, 'title':pp(title),'imgurl':makeabs(url,pp(imgurl)),'fullstory':pp(fullstory), "tag":tag,'preview':preview})
            
        except:
            pass
    submit(ans)

def grabanandabazar(tag, url):
    ans = []
    soup = BuildSoup(url)
    if not soup: return;
    #pdb.set_trace()
    urls = [ makeabs(url, x.get('href')) for x in soup.select('.sectionstoryinside-sub > div > a') if x.has_key('href') and x.get('href')]
    tag += ','+ gd(url)
    for u in urls[::-1][:10]:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BuildSoup(u)
            if not soup: continue;
            #pdb.set_trace()
            title = soup.select_one('#story_container h1').text
            if soup.select_one('#story_container  img.img-responsive'):
                imgurl = soup.select_one('#story_container  img.img-responsive').get('src')
            fullstory = ''.join([x.text for x in soup.select('#story_container  .articleBody p') if x ])
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
    grabEiSamay("sports", "http://eisamay.indiatimes.com/sports/articlelist/23000116.cms")
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
	
def songbaad():
    #grabebela("kolkata", "http://zeenews.india.com/bengali/kolkata?pfrom=top-nav")
    #grabsongbaad("state", "https://ebela.in/state?ref=entertainment-TopNav")
    #grabsongbaad("india", "https://ebela.in/national?ref=strydtl-state-TopNav")
    grabsongbaad("entertainment", "http://songbaad.com/entertainment/")
    grabsongbaad("international", "http://songbaad.com/world/")
    grabsongbaad("lifestyle", "http://songbaad.com/lifestyle/")
    grabsongbaad("science", "http://songbaad.com/science-and-technology/")  
    grabsongbaad("sports", "http://songbaad.com/sports/")  
    
def bartamanpatrika():
    grabbartamanpatrika("kolkata", "http://bartamanpatrika.com/section.php?cID=12")
    grabbartamanpatrika("state", "http://bartamanpatrika.com/section.php?cID=13")
    grabbartamanpatrika("india", "http://bartamanpatrika.com/section.php?cID=14")
    grabbartamanpatrika("international", "http://bartamanpatrika.com/section.php?cID=15")
    grabbartamanpatrika("entertainment", "http://bartamanpatrika.com/section.php?cID=45")    
    grabbartamanpatrika("movie", "http://bartamanpatrika.com/section.php?cID=41")    
    grabbartamanpatrika("story", "http://bartamanpatrika.com/section.php?cID=33")

def anandabazar():
    grabanandabazar("kolkata", "http://www.anandabazar.com/calcutta?ref=hm-topnav")
    grabanandabazar("state", "http://www.anandabazar.com/state?ref=hm-topnav")
    grabanandabazar("india", "http://www.anandabazar.com/national?ref=hm-topnav")
    grabanandabazar("international", "http://www.anandabazar.com/international?ref=hm-topnav")
    grabanandabazar("entertainment", "http://www.anandabazar.com/entertainment?ref=hm-topnav")    
    grabanandabazar("lifestyle", "http://www.anandabazar.com/lifestyle?ref=hm-topnav")    
    grabanandabazar("science", "http://www.anandabazar.com/others/science?ref=hm-topnav")    
    grabanandabazar("sports", "http://www.anandabazar.com/sport?ref=hm-topnav")
    grabanandabazar("business", "http://www.anandabazar.com/business?ref=hm-topnav")

def job():
    try:
          anandabazar() 
          bartamanpatrika();
          songbaad();
          pratidin();
          eisamay();
          zeenews();
    except:
        pass
job();

import schedule
import time
schedule.every(90).minutes.do(job)

while True:
    schedule.run_pending()
    time.sleep(1)

