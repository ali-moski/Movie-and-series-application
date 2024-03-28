package com.example.myapplication.Actevity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adaptor.CategoryListAdapter;
import com.example.myapplication.Adaptor.FilmListAdapter;
import com.example.myapplication.Adaptor.SliderAdaptor;
import com.example.myapplication.Domain.GenresItem;
import com.example.myapplication.Domain.ListFilm;
import com.example.myapplication.Domain.Slideritem;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
private RecyclerView.Adapter adapterBestMovies,adapterUpComming,adapterCategory;
private RecyclerView recyclerViewBestMovies,recyclerViewUpComming,recyclerViewCategory;
private RequestQueue mRequestQueue;
private StringRequest mStringRequest,mStringRequest2,mStringRequest3;
private ProgressBar loading1,loading2,loading3;
private ViewPager2 viewPager2;
private Handler slideHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
        banners();
        sendRequestBestMovies();
        sendRequestUpComming();
        sendRequestCategory();
    }

    private void sendRequestBestMovies() {
        mRequestQueue= Volley.newRequestQueue(this);
        loading1.setVisibility(View.VISIBLE);
        mStringRequest=new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", response -> {
            Gson gson=new Gson();
            loading1.setVisibility(View.GONE);
            ListFilm item=gson.fromJson(response,ListFilm.class);
            adapterBestMovies=new FilmListAdapter(item);
            recyclerViewBestMovies.setAdapter(adapterBestMovies);
        }, error -> {
            loading1.setVisibility(View.GONE);
            Log.i("UiLover", "onErrorResponse: "+error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }

    private void sendRequestUpComming() {
        mRequestQueue = Volley.newRequestQueue(this);
        loading3.setVisibility(View.VISIBLE);
        mStringRequest3 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=2", response -> {
            Gson gson=new Gson();
            loading3.setVisibility(View.GONE);
            ListFilm item=gson.fromJson(response,ListFilm.class);
            adapterUpComming=new FilmListAdapter(item);
            recyclerViewUpComming.setAdapter(adapterUpComming);
        }, error -> {
            loading3.setVisibility(View.GONE);
            Log.i("UiLover", "onErrorResponse: "+error.toString());
        });
        mRequestQueue.add(mStringRequest3);
    }
    private void sendRequestCategory() {
        mRequestQueue = Volley.newRequestQueue(this);
        loading2.setVisibility(View.VISIBLE);
        mStringRequest2 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/genres", response -> {
            Gson gson=new Gson();
            loading2.setVisibility(View.GONE);
            ArrayList<GenresItem> catList=gson.fromJson(response, new TypeToken<ArrayList<GenresItem>>(){
            }.getType());
            adapterCategory=new CategoryListAdapter(catList);
            recyclerViewCategory.setAdapter(adapterCategory);
        }, error -> {
            loading2.setVisibility(View.GONE);
            Log.i("UiLover", "onErrorResponse: "+error.toString());
        });
        mRequestQueue.add(mStringRequest2);
    }
    private void banners() {
        List<Slideritem> slideritems=new ArrayList<>();
        slideritems.add(new Slideritem(R.drawable.wide));
        slideritems.add(new Slideritem(R.drawable.wide1));
        slideritems.add(new Slideritem(R.drawable.wide2));
        slideritems.add(new Slideritem(R.drawable.wide3));
        slideritems.add(new Slideritem(R.drawable.wide4));
        slideritems.add(new Slideritem(R.drawable.wide5));
        slideritems.add(new Slideritem(R.drawable.wide6));
        slideritems.add(new Slideritem(R.drawable.wide7));
        slideritems.add(new Slideritem(R.drawable.wide8));
        slideritems.add(new Slideritem(R.drawable.wide9));
        slideritems.add(new Slideritem(R.drawable.wide10));
        slideritems.add(new Slideritem(R.drawable.wide11));
        slideritems.add(new Slideritem(R.drawable.wide12));

        viewPager2.setAdapter(new SliderAdaptor(slideritems,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(slideRunnable);
            }
        });
    }
    private Runnable slideRunnable=new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(slideRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(slideRunnable,2000);
    }

    private void initView() {
        viewPager2=findViewById(R.id.viewpagerSlider);
        recyclerViewBestMovies=findViewById(R.id.view1);
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewUpComming=findViewById(R.id.view3);
        recyclerViewUpComming.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategory=findViewById(R.id.view2);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        loading1=findViewById(R.id.progressBar1);
        loading2=findViewById(R.id.progressBar2);
        loading3=findViewById(R.id.progressBar3);
    }
}