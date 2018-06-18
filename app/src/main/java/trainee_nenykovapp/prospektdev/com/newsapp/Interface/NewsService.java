package trainee_nenykovapp.prospektdev.com.newsapp.Interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.News;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.WebSite;

public interface NewsService {

    @GET ("v1/sources?language=en")
    Call<WebSite> getSources();

    @GET
    Call<News> getNewestArticles(@Url String url);
}
