package com.example.myapplication.Adaptor;

import android.app.DownloadManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Domain.Slideritem;
import com.example.myapplication.R;

import java.util.List;

public class SliderAdaptor extends RecyclerView.Adapter<SliderAdaptor.SliderViewHolde> {
    private List<Slideritem> slideritems;
    private ViewPager2 viewPager2;
    private Context context;

    public SliderAdaptor(List<Slideritem> slideritems, ViewPager2 viewPager2) {
        this.slideritems = slideritems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdaptor.SliderViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new SliderViewHolde(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.slider_item_container,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdaptor.SliderViewHolde holder, int position) {
       holder.setImage(slideritems.get(position));
       if (position==slideritems.size()-2){
        viewPager2.post(runnable);
       }
    }

    @Override
    public int getItemCount() {
        return slideritems.size();
    }

    public class SliderViewHolde extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public SliderViewHolde(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlide);
        }
        void setImage(Slideritem slideritem){
            RequestOptions requestOptions=new RequestOptions();
            requestOptions=requestOptions.transforms(new CenterCrop(),new RoundedCorners(60));

            Glide.with(context)
                    .load(slideritem.getImage())
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            slideritems.addAll(slideritems);
            notifyDataSetChanged();
        }
    };
}
