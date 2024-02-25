package com.shipgig.thegun.pos.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shipgig.thegun.pos.R;

public class TransactionSuccessful extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.transaction_successful);

        Glide.with(this).load(R.drawable.check1)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.check1)
                        .fitCenter())
                        .into((ImageView)findViewById(R.id.gifimage));
    }
}
