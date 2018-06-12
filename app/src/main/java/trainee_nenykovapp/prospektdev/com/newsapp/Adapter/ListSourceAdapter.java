package trainee_nenykovapp.prospektdev.com.newsapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import trainee_nenykovapp.prospektdev.com.newsapp.Common.Common;
import trainee_nenykovapp.prospektdev.com.newsapp.Interface.IconBetterIdeaService;
import trainee_nenykovapp.prospektdev.com.newsapp.Interface.ItemClickListener;
import trainee_nenykovapp.prospektdev.com.newsapp.ListNews;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.IconBetterIdea;
import trainee_nenykovapp.prospektdev.com.newsapp.Model.WebSite;
import trainee_nenykovapp.prospektdev.com.newsapp.R;

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    ItemClickListener itemClickListener;

    TextView source_title;
    CircleImageView source_image;

    public ListSourceViewHolder(View itemView) {
        super(itemView);

        source_image = itemView.findViewById(R.id.source_image);
        source_title = itemView.findViewById(R.id.source_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder>{

    private Context context;
    private WebSite webSite;

    private IconBetterIdeaService mservice;


    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;

        mservice = Common.getIconService();
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.souce_layout, parent, false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder holder, int position) {

        StringBuilder iconBetterAPI = new StringBuilder("http://i.olsh.me/allicons.json?url=");
        iconBetterAPI.append(webSite.getSources().get(position).getUrl());

        mservice.getIconUrl(iconBetterAPI.toString())
                .enqueue(new Callback<IconBetterIdea>() {
                    @Override
                    public void onResponse(Call<IconBetterIdea> call, Response<IconBetterIdea> response) {
                        try {
                            if (response.body().getIcons().size() > 0) {
                                Picasso.with(context)
                                        .load(response.body().getIcons().get(0).getUrl())
                                        .into(holder.source_image);

                            }
                        } catch (NullPointerException ex){

                        }
                    }

                    @Override
                    public void onFailure(Call<IconBetterIdea> call, Throwable t) {

                    }
                });

        holder.source_title.setText(webSite.getSources().get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, ListNews.class);
                intent.putExtra("source", webSite.getSources().get(position).getId());
                intent.putExtra("sortBy", webSite.getSources().get(position).getSortBysAvailable().get(0));
                intent.putExtra("nameNews", webSite.getSources().get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return webSite.getSources().size();
    }
}
