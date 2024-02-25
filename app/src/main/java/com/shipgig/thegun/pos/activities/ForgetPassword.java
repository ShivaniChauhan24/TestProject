package com.shipgig.thegun.pos.activities;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.model.ForgotPassModel;
import com.shipgig.thegun.pos.utilities.RetrofitClient;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends Activity implements View.OnClickListener {

    private ViewGroup emailIconContainer;
    private ImageView email_icon;
    private TextView email_txt;
    private ProgressBar progressBar;
    TextView alreadyaccount;
    Button reset;
    EditText editTextMailId;
    String editReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forget_password);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"roboto_regular.ttf",true);

        init();
        
        editTextMailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                String input = s.toString();
                checkInputs(input);
            }
        });
    }

    private boolean checkInputs(String email) {

        if (email.length() < 4 || email.length() > 30) {
//            Toast.makeText(this, "Email Must consist of 4 to 30 characters", Toast.LENGTH_SHORT).show();
            editTextMailId.setError("Email Must consist of 4 to 30 characters");
            return false;
        } else if (!email.matches("^[A-za-z0-9.@]+")) {
            editTextMailId.setError("Only . and @ characters allowed");
//            Toast.makeText(this, "Only . and @ characters allowed", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.contains("@") || !email.contains(".")) {
            editTextMailId.setError("Email must contain @ and .");
//            Toast.makeText(this, "Email must contain @ and .", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void init() {
        reset =  findViewById(R.id.reset);
        editTextMailId =  findViewById(R.id.username);
        alreadyaccount =  findViewById(R.id.alreadyuser);
        email_icon = findViewById(R.id.email_icon);
        email_txt = findViewById(R.id.email_txt);
        progressBar = findViewById(R.id.ordered_packed_progress);
        emailIconContainer = findViewById(R.id.forget_password_email_icon_container);
        reset.setOnClickListener(this);
        alreadyaccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reset:
                isValidate();
             break;

                case R.id.alreadyuser:
                    Intent intentlogin = new Intent(ForgetPassword.this,LoginActivity.class);
                    startActivity(intentlogin);
                    finish();
                    break;
        }
    }

    private void isValidate() {
        editReset = editTextMailId.getText().toString().trim();
        if(!editReset.equals("")){
            resetPassword();
        }
        else{
            Toast.makeText(this,"Please enter register email address ",Toast.LENGTH_SHORT).show();
        }
    }

    private void resetPassword() {

        if (!isInternetOn()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
            builder.setTitle("Alert !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Network Error, check your network connection.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        else {

            TransitionManager.beginDelayedTransition(emailIconContainer);
            email_txt.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition(emailIconContainer);
            email_icon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Call<ForgotPassModel> call = RetrofitClient.getInstance().getApi().forgotPassword(editReset);
            call.enqueue(new Callback<ForgotPassModel>() {
                @Override
                public void onResponse(Call<ForgotPassModel> call, Response<ForgotPassModel> response) {

                    try{

                        if (response.body() != null){
                            String message = response.body().getMsg();
                            if (message.equals("mail has been sent")){
                                ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,email_icon.getWidth()/2,email_icon.getHeight()/2);
                                scaleAnimation.setDuration(100);
                                scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                scaleAnimation.setRepeatMode(Animation.REVERSE);
                                scaleAnimation.setRepeatCount(1);

                                scaleAnimation.setAnimationListener(new Animation.AnimationListener(){
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {

                                        email_txt.setText("Recovery email sent successfully ! check your inbox");
                                        email_txt.setTextColor(getResources().getColor(R.color.success));
                                        TransitionManager.beginDelayedTransition(emailIconContainer);
                                        email_txt.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                        email_icon.setImageResource(R.drawable.green);
                                    }

                                });

                                email_icon.startAnimation(scaleAnimation);

                            }else {
                                String error = response.body().getMsg().toString();
                                progressBar.setVisibility(View.GONE);
                                email_txt.setText(error);
                                email_txt.setTextColor(getResources().getColor(R.color.login_welcome_color));
                                TransitionManager.beginDelayedTransition(emailIconContainer);
                                email_icon.setImageResource(R.drawable.gmail);
                                email_txt.setVisibility(View.VISIBLE);
                            }

                        }
                        else {
                            Toast.makeText(ForgetPassword.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ForgotPassModel> call, Throwable t) {

                    String error = t.getMessage().toString();
                    Toast.makeText(ForgetPassword.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgetPassword.this,LoginActivity.class);
        startActivity(intent);
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}
