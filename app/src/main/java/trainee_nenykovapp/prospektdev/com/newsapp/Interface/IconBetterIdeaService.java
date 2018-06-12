package trainee_nenykovapp.prospektdev.com.newsapp.Interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.IconBetterIdea;

public interface IconBetterIdeaService {
    @GET
    Call<IconBetterIdea> getIconUrl(@Url String url);
}
