package com.example.flashcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.List;

public class OnBoardActivity extends AppCompatActivity {


    int[] testImages = {R.drawable.avt, R.drawable.image2};
    private List<SlideItem> sliderItem;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private ImageView iv1, iv2, iv3, iv4;
    private Button signUpBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboard_layout);

        linkSetup();

        viewPager2 = findViewById(R.id.viewPager);

        sliderItem = new ArrayList<>();
        sliderItem.add(new SlideItem(R.drawable.avt));
        sliderItem.add(new SlideItem(R.drawable.image2));
        sliderItem.add(new SlideItem(R.drawable.chemes));

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);

        viewPager2.setAdapter(new SlideAdapter(sliderItem, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(5);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                slideHandler.removeCallbacks(sliderRunnale);
                slideHandler.postDelayed(sliderRunnale, 2000);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                ChangeIndicatorColor();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                ChangeIndicatorColor();
            }
        });

        signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(OnBoardActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(OnBoardActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    private Runnable sliderRunnale = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnale);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnale, 3000);
    }

    private void ChangeIndicatorColor(){
        switch (viewPager2.getCurrentItem() % 4){
            case 0:
                iv1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.primary_indicator));
                iv2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                iv3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                iv4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                break;
            case 1:
                iv1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                iv2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.primary_indicator));
                iv3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                iv4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                break;
            case 2:
                iv1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                iv2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                iv3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.primary_indicator));
                iv4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                break;
            case 3:
                iv1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                iv2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                iv3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.secondary_indicator));
                iv4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.primary_indicator));
                break;
            default:
                break;
        }
    }

    private void linkSetup(){
        Link termsLink = new Link("Terms of service")
                .setTextColor(Color.parseColor("#0000FF"))
                .setHighlightAlpha(.4f)
                .setUnderlined(false)
                .setBold(true)
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText) {

                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {

                    }
                });

        Link policyLink = new Link("Privacy policy")
                .setTextColor(Color.parseColor("#0000FF"))
                .setHighlightAlpha(.4f)
                .setUnderlined(false)
                .setBold(true)
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText) {

                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {

                    }
                });

        LinkBuilder.on(findViewById(R.id.tvLink))
                .addLink(termsLink)
                .addLink(policyLink)
                .build();
    }

}