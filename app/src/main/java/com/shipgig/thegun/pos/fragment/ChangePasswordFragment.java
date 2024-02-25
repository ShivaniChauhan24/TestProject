package com.shipgig.thegun.pos.fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shipgig.thegun.pos.MainActivity;
import com.shipgig.thegun.pos.R;
import com.shipgig.thegun.pos.activities.LoginActivity;
import com.shipgig.thegun.pos.model.FogetPasswordData;
import com.shipgig.thegun.pos.model.PassData;
import com.shipgig.thegun.pos.model.ModelChangePassword;
import com.shipgig.thegun.pos.utilities.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shipgig.thegun.pos.MainActivity.drawer;

/**
 * Created by user on 12/31/15.
 */
public class ChangePasswordFragment extends Fragment {

    View myView;
    TextView submit,go_back;
    EditText currentPassword,newPassword,repeatednewPassword,username;
    Context context;
    String current,userName,newPass,againPass;
    ImageView open_drawer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.change_password, container, false);

        context = getActivity();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        myView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.
                            INPUT_METHOD_SERVICE);
                    if (getActivity().getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }
                return true;
            }
        });

        open_drawer = myView.findViewById(R.id.open_drawer);
        open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.START);
            }
        });

        submit = myView.findViewById(R.id.submit);
        currentPassword = myView.findViewById(R.id.currentPassword);
        newPassword = myView.findViewById(R.id.newPassword);
        username = myView.findViewById(R.id.username);
        go_back = myView.findViewById(R.id.go_back);
        repeatednewPassword = myView.findViewById(R.id.repeatednewPassword);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                 current = currentPassword.getText().toString();
                 userName = username.getText().toString();
                 newPass = newPassword.getText().toString();
                 againPass = repeatednewPassword.getText().toString();


                 if (userName.equals("")){
                     username.setError("Required");
                 }
                 else if (current.equals("")){
                     currentPassword.setError("Required");
                 }
                 else if (newPass.equals("")){
                     newPassword.setError("Required");
                 }
                 else if (againPass.equals("")){
                     repeatednewPassword.setError("Required");
                 }
                 else {
                     if (newPass.equals(againPass)){
                         changePassword();
                     }
                     else {
                         Toast.makeText(getActivity(), "New password and confirm password doesn't match", Toast.LENGTH_SHORT).show();
                     }
                 }

            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return myView;
    }

    private void changePassword() {

        if (!isInternetOn()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Online Authenticating!!!");
            progressDialog.setMessage("Please wait while authenticate....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            PassData passData = new PassData();
            passData.setCurrentPass(current);
            passData.setNewPass(newPass);
            passData.setConfirmPass(againPass);
            FogetPasswordData data = new FogetPasswordData();
            data.setUserName(userName);
            data.setPassData(passData);

            Call<ModelChangePassword> call = RetrofitClient.getInstance().getApi().changePassword(data);
            call.enqueue(new Callback<ModelChangePassword>() {
                @Override
                public void onResponse(Call<ModelChangePassword> call, Response<ModelChangePassword> response) {
                    try {
                        int code = response.code();
                        if (code == 200){
                            progressDialog.dismiss();
                            String success = response.body().getMsg();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>Successfully Update!</font>"));
                            builder.setCancelable(false);
                            builder.setMessage(""+success);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else if (code == 400){
                            progressDialog.dismiss();
                            String error = "Your current password is wrong";
                            currentPassword.setError("Your current password is wrong");
                            Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                        }
                        else if (code == 401){
                            progressDialog.dismiss();
                            String error = "Your user name is wrong";
                            username.setError("Your user name is wrong");
//                            Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ModelChangePassword> call, Throwable t) {
                    progressDialog.dismiss();
                    String error = t.getMessage().toString();
                    Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getContext().getSystemService(getActivity().CONNECTIVITY_SERVICE);

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
