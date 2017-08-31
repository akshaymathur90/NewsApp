MinSDK Version: 15
TargetSDK Version: 25


Activities:
	HomeActivity: Launcher Activity which displays top stories sections using tab fragment layout. It also displays the Search View and handles input of the query string.
	NewsArticle Activity: This activity hosts the fragment which displays the complete news article in a web view.
Fragments:
	HomeActivityFragment: This fragment displays the search results based on the query string. The fragment uses Volley to make request to NYTimes api and displays the result in paginated recycler view.
	NewsDetailFragment: The fragment hosts a web view which renders the news article.
	SingleNewsTabFragment: Common fragment which hosts the different sections of the top stories by making a call to the respective section of the API.
	VisibleFragment: Abstract fragment which requires all implementing classes to provide a Broadcast Receiver.
	
Adapters:
	NewsTabAdapter: Extension of the Fragment State Pager Adapter. It displays and manages all the top stories fragments in a tab layout.
	QueryStoriesRecyclerViewAdapter: Extension of RecyclerView Adapter which displays query results with support for pagination.
	TopStoriesRecyclerViewAdapter: Extension of RecyclerView Adapter to display top stories of the respective section.

Broadcast Receivers:
	InternetCheckReceiver: This Broadcast receiver listens for internet connectivity change events and informs the user when internet is not available.

