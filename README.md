# Android App - Social Search
-----------------------------------------------------------------------
## Introduction

**A twitter search client**. User can search for tweets matching their entered search criteria. This app has the following features.
* Prominent **text** content of tweets
* Support **paging** of search results. 
* Provide **search text suggestions** based on user's search history
* **Links** in the content text are made prominent using different text color. 
* Links are **clickable**, and a **WebActivity** is provided to handle the url. 
* The WebActivity have a mechanism to handle **no internet** situation and support **pull to refresh**.
* A LaunchActivity that has a **flip animation** using my own **library-FlipView**

And for **bonus points**, this App provides three more additional search functionality.-- **Video, Near and Until**.
* Video is to search for tweets that have **video links**. 
* Near is to search for tweets that have been tweeted **near user's position**
* Unitl is to search for tweets published **until a certain date**.
* Provides a **user interface** to easliy exploit these functions.

For **code structure**:
* The App is based on **MVP model**.
* Using Daggar2 and Rx to achieve **Dependency Injection**
* Using Fragments and ViewPager so this App is **Expandable**
--------------------------------------------------------------
## Features
### Search for keywords and load more search result by scrolling down

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/normal_search.gif)

----------------------------------------------------------------------------------------

### Launch animation using my own library [FlipView](https://github.com/fredliao123/FlipView)

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/launchactivity.gif)

----------------------------------------------------------------------------------------

### Search for video

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/search_for_video.gif)

----------------------------------------------------------------------------------------

### Search for Near tweets

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/search_near.gif)

----------------------------------------------------------------------------------------

### Search for tweets unitl a certain time

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/sarch_for_time.gif)

----------------------------------------------------------------------------------------

### Click url link and launch WebActivity

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/click_web.gif)

----------------------------------------------------------------------------------------

### Search suggestions based on search history

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/suggestion.gif)

----------------------------------------------------------------------------------------

### Ask for ACCESS_FINE_LOCATION after API 23. And ask for enable GPS for location function used in Search Near feature

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/ask_for_permission.gif.gif)

----------------------------------------------------------------------------------------

### Show empty view when the search result is empty

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/empty_view.gif)

----------------------------------------------------------------------------------------

### Show no internet view when there is no internet connection

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/no_net.gif)

----------------------------------------------------------------------------------------

### A build-in no internet view in WebActivity(Otherwise only shows 404, which is hideous)

![Gif example](https://github.com/fredliao123/SocialSearcher/blob/master/gif/web_empty.gif)

-------------------------------------------------------------------------------------------------
## Download 
[app-release.apk](https://github.com/fredliao123/SocialSearcher/blob/master/app_release/app-release.apk)


-------------------------------------------------------------------------------------------------
## Compile instruction
Clone the project and sync and build in Android Studio. The Key and Token for Twitter are in gradle.properties which I shouldn't put them there publicly. But for code review and a quick build of the project, I think it's all right to public it for now.

-------------------------------------------------------------------------------------------
## Reference
When working on this project, I refered to some code from project [SearchTwitter](https://github.com/pwittchen/SearchTwitter) for saving time. 
I largely reconstructed the code and added more features.

-----------------------------------------------------------------------------------------
## Others
### Stuff I learn during working on the project
* When I was working on the project, I find a bug that the fragments get a null context when its wrapper ViewPager is set to Visibility.NONE. So I looked into it. The flow of ViewPage populate fragments is as follows:
ViewPager : populate() ->
PagerAdapter : instantiateItem() ->
FragmentPagerAapter : instantiateItem() ->
FragmentTransaction : attach()    **This is where Fragment be attached**
So the main problem is that populate is not properly called.
  - In normal condition, ie, the visibility is VISIBLE or INVISIBLE. The populate() is called in setOffScreenPageLimit() and onMeasure(); 
  - But when the visibility is set to GONE. the populate will not be called in onMeasure(), so the Fragments are not attached to Activity. So that there Context will be null.

