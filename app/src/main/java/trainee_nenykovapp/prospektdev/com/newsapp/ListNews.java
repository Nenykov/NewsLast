package trainee_nenykovapp.prospektdev.com.newsapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trainee_nenykovapp.prospektdev.com.newsapp.Adapter.ListNewsAdapter;
import trainee_nenykovapp.prospektdev.com.newsapp.Common.Common;
import trainee_nenykovapp.prospektdev.com.newsapp.Interface.NewsService;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.Article;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.News;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.Source;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.WebSite;

public class ListNews extends AppCompatActivity{

    private KenBurnsView kbv;
    private AlertDialog dialog;
    private NewsService mServise;
    private TextView top_author, top_title;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DiagonalLayout diagonalLayout;
    private Toolbar toolbar;
    private String source = "" , sortBy="", webHotURL = "", name = "";

    private ListNewsAdapter adapter;
    private RecyclerView lstNews;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_app_news);

        mServise = Common.getNewsService();

        dialog = new SpotsDialog(this);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source,true);
            }
        });

        diagonalLayout = findViewById(R.id.diagonallayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(getBaseContext(),DetailArticle.class);
                detail.putExtra("webURL", webHotURL);
                startActivity(detail);
            }
        });
        kbv = findViewById(R.id.top_image);
        top_author = findViewById(R.id.top_author);
        top_title = findViewById(R.id.top_title);

        lstNews = findViewById(R.id.lastNews);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);


        if (getIntent() !=null) {
            source = getIntent().getStringExtra("source");
            sortBy = getIntent().getStringExtra("sortBy");
            name = getIntent().getStringExtra("nameNews");
            if (!source.isEmpty() && !sortBy.isEmpty()){
                loadNews(source, false);
            }
        }

        getSupportActionBar().setTitle(name);

    }

    private void loadNews(String source, boolean isRefresh) {
        if (!isRefresh){
            dialog.show();
            mServise.getNewestArticles(Common.getAPIUrl(source,sortBy, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();
                            try {
                                Picasso.with(getBaseContext())
                                        .load(response.body().getArticles().get(0).getUrlToImage())
                                        .into(kbv);
                            } catch (IllegalArgumentException aEx) {

                            }


                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> removeFirstItem = response.body().getArticles();
                            removeFirstItem.remove(0);
                            adapter = new ListNewsAdapter(removeFirstItem,getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        }
        else {
            dialog.show();
            mServise.getNewestArticles(Common.getAPIUrl(source,sortBy,Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();

                            try {
                                Picasso.with(getBaseContext())
                                        .load(response.body().getArticles().get(0).getUrlToImage())
                                        .into(kbv);
                            } catch (IllegalArgumentException aEx) {

                            }

                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> removeFirstItem = response.body().getArticles();
                            removeFirstItem.remove(0);

                            adapter = new ListNewsAdapter(removeFirstItem,getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
