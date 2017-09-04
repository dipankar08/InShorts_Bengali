import requests
#import schedule
import time
import pdb
import datetime
from bs4 import BeautifulSoup

####################### Config need to be added at front #############################
#http://52.89.112.230/api/inshortsbengali?_cmd=constraint&url=unique
#http://52.89.112.230/api/inshortsbengali?_cmd=delete&CONFIRM_MSG=***I%20HEREBY%20DECLARE%20THAT%20I%20WANT%20TO%20DELETE%20THE%20TABLE%20WITH%20NAME%20inshortsbengali*****
CONFIG ={
    'news18' : {
        'url':'http://bengali.news18.com/',
        'tag': 'firstpage',
        'link_selector_firstpage':['.top_storys li > a','.section_widget  li > a','#trendingSlider li > a'],
        'link_selector_catpage':['.top_storys li > a','.section_widget  li > a','#trendingSlider li > a'],
        'artical_limit':30,
        'title_selector':'.article_box h1',
        'image_selector':'.article_box .articleimg img',
        'video_selector':'',
        'content_selector':'#article_body p',
    },
    'pratidin' : {
        'url':'http://www.sangbadpratidin.in/',
        'tag': 'firstpage',
        'link_selector_firstpage':['.slider-featured-image a','.first-post .single-article figure > a'],
        'link_selector_catpage':['div.article-container > article > .featured-image > a'],
        'artical_limit':20,
        'title_selector':'#primary  article header',
        'image_selector':'#primary .featured-image img',
        'video_selector':'',
        'content_selector':'#primary .entry-content p',
    },
    'eisamay' : {
        'url':'http://eisamay.indiatimes.com/',
        'tag': 'firstpage',
        'link_selector_firstpage':['.latestnews > a','.other_main_news1 li > a','.section_article .first > a'],
        'link_selector_catpage':['div.mainarticle1> h1 > a','div.other_main_news1 > ul > li > a' ],
        'artical_limit':20,
        'title_selector':'div.leftmain h1',
        'image_selector':'div.leftmain .article img',
        'video_selector':'',
        'content_selector':'div.leftmain .article arttextxml .Normal',
    },
    'zeenews' : {
        'url':'http://zeenews.india.com/bengali/',
        'tag': 'firstpage',
        'link_selector_firstpage':['.view-content h3 > a','.sub-leadarea li > a'],
        'link_selector_catpage':['div.lead-health-nw > a' ],
        'artical_limit':20,
        'title_selector':'div.connrtund .full-story-head > h1',
        'image_selector':'div.connrtund .article-image img',
        'video_selector':'',
        'content_selector':'div.connrtund div.field-type-text-with-summary .field-item > p',
    },
    'ebela' : {
        'url':'https://ebela.in/',
        'tag': 'firstpage',
        'link_selector_firstpage':['.carousel-inner .image_rollover_placeholder > a', '.home_individual_blocks  .mn_img_bot > a'],
        'link_selector_catpage':['div.container_main  .carousel-inner a.mrf-article-image' ],
        'artical_limit':20,
        'title_selector':'div.container_main .ebela-new-story-section h1',
        'image_selector':'div.container_main .ebela-new-story-section img',
        'video_selector':'',
        'content_selector':'div.container_main .ebela-new-story-section > p',
    },
    'songbaad' : {
        'url':'http://bengali.news18.com/',
        'tag': 'firstpage',
        'link_selector_firstpage':[],
        'link_selector_catpage':['.content .latestnews1 .fimage1 > a' ],
        'artical_limit':20,
        'title_selector':'article  > h1',
        'image_selector':'article .entry img',
        'video_selector':'',
        'content_selector':'article .entry > p',
    },
    'bartaman' : {
        'url':'http://www.bartamanpatrika.com/',
        'tag': 'firstpage',
        'link_selector_firstpage':['.pageLeftNewsFirst a.bisad'],
        'link_selector_catpage':['.firstSection  a.bisad' ],
        'artical_limit':20,
        'title_selector':'.firstSection .head-news  > h4 strong',
        'image_selector':'.firstSection .head-news  img',
        'video_selector':'',
        'content_selector':'.firstSection .head-news  .content',
    },
    'anandabazar' : {
        'url':'http://www.anandabazar.com/',
        'tag': 'firstpage',
        'link_selector_firstpage':['.topphotostory a.PQTopStories','.container .leadstoryheading > a'],
        'link_selector_catpage':['.sectionstoryinside-sub > div > a' ],
        'artical_limit':20,
        'title_selector':'#story_container h1',
        'image_selector':'#story_container  img.img-responsive',
        'video_selector':'',
        'content_selector':'#story_container  .articleBody p',
    },
}

##########################################################################

def BuildSoup(u):
    #We can try 3 times.
    for i in range(3):
        try:
            return BeautifulSoup(requests.get(u).text,"html.parser")
        except:
            print("Connection refused by the server..")
            print("Let me sleep for 5 seconds")
            print("ZZzzzz...")
            time.sleep(1)
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
    #pdb.set_trace()
    if not ans: return
    print '[Info] Summiting data ...'
    import json
    for a in ans:
        a['date']= time.strftime('%d/%m/%Y %H:%M:%S',time.gmtime()) #GMT time
    payload = {"_cmd":"insert","_payload":ans,"_ignore_error":True} #If some entry is duplicate just igonore it
    headers = {'Content-Type': 'application/json'}
    r = requests.post(url,headers=headers, data=json.dumps(payload))
    print r.json().get('msg')

def CommonParse(config):
    #pdb.set_trace()
    ans = []
    url = config['url']
    soup = BuildSoup(config['url'])
    if not soup: return;
    urls = [ ]
    if config['tag'] == 'firstpage':
        sel = config['link_selector_firstpage']
    else:
        sel = config['link_selector_catpage']
    for t in sel:
        urls += [makeabs(url, x.get('href')) for x in soup.select(t) if x.has_key('href') and x.get('href')]
    urls = list(set(urls))
    urls = urls[:config['artical_limit']]
    tag = config['tag']+','+ gd(config['url'])
    for u in urls:
        try:
            print '[Info]  Processing ',u,'...'
            soup = BuildSoup(u)
            if not soup: continue;
            #pdb.set_trace()
            title = soup.select_one(config['title_selector']).text
            if soup.select_one(config['image_selector']):
                imgurl = soup.select_one(config['image_selector']).get('src')
            fullstory = ''.join([x.text for x in soup.select(config['content_selector']) if x ])
            preview = fullstory[:500]
            ans.append({'url':u, 'title':pp(title),'imgurl':makeabs(url,pp(imgurl)),'fullstory':pp(fullstory), "tag":tag,'preview':preview})
        except:
            pass
    submit(ans[::-1])

"""" >> This nees to be cleanred  
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

"""

def job():
    try:
        CommonParse(CONFIG['news18']) 
        CommonParse(CONFIG['pratidin'])
        CommonParse(CONFIG['eisamay'])
        CommonParse(CONFIG['zeenews'])
        CommonParse(CONFIG['ebela'])
        CommonParse(CONFIG['bartaman'])
        CommonParse(CONFIG['anandabazar'])
    except:
        pass
job();

import schedule
import time
schedule.every(90).minutes.do(job)

while True:
    schedule.run_pending()
    time.sleep(1)

