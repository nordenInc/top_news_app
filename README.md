It is required to create an application that receives through the news portal API a list of relevant news and displays to the console the 10 most frequently encountered words from the news headlines. Data is taken from the following sources:

1. Lenta.ru API (https://api.lenta.ru/lists/latest)
2. RSS AIF (http://www.aif.ru/rss/news.php)

The counting process is initiated by the user of the application with the help of the console command “news: stats”. The user can pass the source as a parameter. If the command is entered without parameters, the user is prompted to select a data source (one of those available in the system, or all at once). The list of sources is displayed on the console. If a user selects an unregistered (unavailable) data source, no sources are registered in the system, or the network is unavailable, a corresponding message is displayed to the user. The implementation of receiving data from each source should be a separate implementation of a common service for all sources (interface).
