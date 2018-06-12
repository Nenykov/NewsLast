package trainee_nenykovapp.prospektdev.com.newsapp.Common;

import trainee_nenykovapp.prospektdev.com.newsapp.Interface.IconBetterIdeaService;
import trainee_nenykovapp.prospektdev.com.newsapp.Interface.NewsService;
import trainee_nenykovapp.prospektdev.com.newsapp.Remote.IconBetterIdeaClient;
import trainee_nenykovapp.prospektdev.com.newsapp.Remote.RetrofitClient;

public class Common {

    private static final String BASE_URL = "https://newsapi.org/";

    public static final String API_KEY = "4c7e33976dab4382b5f25129e39f9081";


    public static NewsService getNewsService() {
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconBetterIdeaService getIconService() {
        return IconBetterIdeaClient.getClient().create(IconBetterIdeaService.class);
    }

    public static String getAPIUrl(String source, String sortBy, String apiKEY) {
        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v1/articles?source=");
        return apiUrl.append(source)
                .append("&sortBy=")
                .append(sortBy)
                .append("&apiKey=")
                .append(apiKEY)
                .toString();
    }
}
