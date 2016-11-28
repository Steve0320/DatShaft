package com.shaftware;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaftware.shaftquack.R;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        final ImageView imageView = (ImageView) findViewById(R.id.duck);
        imageView.setImageResource(R.mipmap.duck);

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.shaft_anim);
        hyperspaceJumpAnimation.setDuration(1000);

        final SplashScreen x = this;

        hyperspaceJumpAnimation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                imageView.setVisibility(View.GONE);
                startActivity(new Intent(x, MainActivity.class));
            }
        });

        imageView.startAnimation(hyperspaceJumpAnimation);

       //startActivity(new Intent(this, MainActivity.class));

    }

}
