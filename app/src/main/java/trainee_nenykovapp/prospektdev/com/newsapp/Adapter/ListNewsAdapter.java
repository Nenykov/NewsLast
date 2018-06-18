package trainee_nenykovapp.prospektdev.com.newsapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import trainee_nenykovapp.prospektdev.com.newsapp.Common.ISO8601Parse;
import trainee_nenykovapp.prospektdev.com.newsapp.DetailArticle;
import trainee_nenykovapp.prospektdev.com.newsapp.Interface.ItemClickListener;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.Article;
import trainee_nenykovapp.prospektdev.com.newsapp.R;

class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemClickListener itemClickListener;

    TextView article_title;
    RelativeTimeTextView article_time;
    CircleImageView article_image;
    private ImageButton shareBtn;
    public ListNewsViewHolder(View itemView) {
        super(itemView);
        article_image = itemView.findViewById(R.id.article_image);
        article_title = itemView.findViewById(R.id.article_title);
        article_time = itemView.findViewById(R.id.article_time);
        shareBtn = itemView.findViewById(R.id.share_btn);
        shareBtn.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);

    }
}

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder>{

    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_layout, parent, false);
        return new ListNewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNewsViewHolder holder, int position) {
        try {
            Picasso.with(context)
                    .load(articleList.get(position).getUrlToImage())
                    .into(holder.article_image);
        } catch (IllegalArgumentException aEx){

        }

        if (articleList.get(position).getTitle().length() > 65) {
            holder.article_title.setText(articleList.get(position).getTitle().substring(0,65) + "...");
        } else {
            holder.article_title.setText(articleList.get(position).getTitle());
        }

        Date date = null;
        try {
            if (articleList.get(position).getPublishedAt() == null) {
                Log.d("devnv","No time publishing");
            } else {
                date = ISO8601Parse.parse(articleList.get(position).getPublishedAt());
            }
        } catch (ParseException ex){
            ex.printStackTrace();
        }
        try {
                holder.article_time.setReferenceTime(date.getTime());
        } catch (NullPointerException ex){

        }


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                switch (view.getId()){
        case R.id.share_btn:
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String link = articleList.get(position).getUrl();
            shareIntent.putExtra(Intent.EXTRA_TEXT,link);
            context.startActivity(Intent.createChooser(shareIntent, "Share the news"));
            break;
        default:
            String linkUrl = articleList.get(position).getUrl();
            Intent detail = new Intent(context,DetailArticle.class);
            detail.putExtra("webURL", articleList.get(position).getUrl());
            detail.putExtra("link", linkUrl);
            context.startActivity(detail);
            break;
    }
}
        });
                }
    @Override
    public int getItemCount() {
        return articleList.size();
    }


}
