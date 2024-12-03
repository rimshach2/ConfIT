package com.fyp.confit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

//1 FOR FB. 2 FOR GOOGLE
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class LoginPage extends AppCompatActivity {


    EditText edtEmail;
    EditText edtPassword;
    ImageView toggleBtn;
    Boolean show = false;
    ImageView signInWithGoogle;
    ImageView simpleSignIn;
    TextView txtError;
    LoginButton signInWithFacebook;
    ProgressDialog progress;
    User sessionUser;
    //Sign In with google
    int RC_SIGN_IN=0;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Boolean flagButtonClicked = false;
    String email, name, phone, password;
    int isActive=0;

    //Sign In with fb
    private static final String EMAIL = "email";
    int flag_method=-1;
    CallbackManager callbackManager;

    //recording audio permissions
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;


    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        progress= new ProgressDialog(this);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        pref  = getApplicationContext().getSharedPreferences("Pref",MODE_PRIVATE);

        setContentView(R.layout.activity_login_page);



        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        signInWithGoogle= findViewById(R.id.google);
        signInWithFacebook=findViewById(R.id.fb);
        edtEmail= findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        toggleBtn = findViewById(R.id.img_eye);
        simpleSignIn=findViewById(R.id.signInbtn);
        txtError=findViewById(R.id.error_message_display_txt_login);

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_method=1;
                goWithGoogle();

            }
        });

        //simple
        simpleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagButtonClicked = true;
                SignInWithSimpleEmail();
            }
        });


        //fbbb
        signInWithFacebook.setReadPermissions(Arrays.asList(EMAIL, "public_profile"));

        // Callback registration
        signInWithFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                loginResult.getAccessToken();
                flag_method=0;
                Toast.makeText(LoginPage.this, "came ehre", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        callbackManager = CallbackManager.Factory.create();
        signInWithFacebook.setLoginBehavior(LoginBehavior.WEB_ONLY);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        flag_method=0;
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        if(!pref.getString("source", "").equals(null))
        {
            //Toast.makeText(getApplicationContext(), pref.getString("source", ""), Toast.LENGTH_SHORT).show();
            if(pref.getString("source", "").equals("facebook"))
            {
                LogOutFacebook();
            }
            else if(pref.getString("source", "").equals("google"))
            {
                LogOutGoogle();
            }
        }

        SignInWithSimpleEmail();

    }


    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if(flag_method==0) {
                if (currentAccessToken == null ) {
                    Toast.makeText(getApplicationContext(), "User logged out", Toast.LENGTH_LONG).show();
                } else {
                    loaduserProfile(currentAccessToken);
                }
            }
        }
    };

    private void loaduserProfile(AccessToken newAccessToken)
    {
        GraphRequest request= GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name= object.getString("first_name");
                    String last_name= object.getString("last_name");
                    email= object.getString("email");
                    String id= object.getString("id");
                    String image_url= "https://graph.facebook.com/"+id+"/pictures?type=normal";

                    name=first_name+" "+last_name;

                    //Glide.with(LoginPage.this).load(image_url).into(ImageViewName);

                    //here store data in db and move to next activity with this info
                    Toast.makeText(LoginPage.this, email, Toast.LENGTH_LONG).show();

                    if(isActive==1)
                        SignInWithGoogleOrFb();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters= new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id" );
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void goWithGoogle()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && flag_method==1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else
        {
            Toast.makeText(LoginPage.this, "Error while connecting..", Toast.LENGTH_SHORT).show();
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            email= account.getEmail();
            name= account.getDisplayName();
            SignInWithGoogleOrFb();
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginPage.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        if(flag_method==1) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                Toast.makeText(getApplicationContext(), account.getEmail(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginPage.this, MainActivity.class));
            }

        }
        super.onStart();
    }

    public void eyeClicked(View v){
        if (!show){
            // show password
            edtPassword.setTransformationMethod( HideReturnsTransformationMethod.getInstance());
            toggleBtn.setImageResource(R.drawable.ic_eye_black_24dp);
            show = true;
        } else {
            // hide password
            edtPassword.setTransformationMethod( PasswordTransformationMethod.getInstance());
            toggleBtn.setImageResource(R.drawable.hide_password);
            show = false;
        }
    }

    public void GoToSignUpPage(View view)
    {
        Intent i1= new Intent(this, SignupPage.class);
        startActivity(i1);
    }


    public void SignInWithSimpleEmail() {

        if (!pref.getBoolean("LoggedIn", false) && !flagButtonClicked)
            return;

        if (pref.getBoolean("LoggedIn", false)) {
            progress.setMessage("Loading, Please Wait..");
            progress.show();
            progress.setCanceledOnTouchOutside(false);

            email = pref.getString("email", null);
            password = pref.getString("password", null);
        } else if (flagButtonClicked) {

            flagButtonClicked = false;

            email = edtEmail.getText().toString();
            password = edtPassword.getText().toString();

            if (email.length() == 0) {
                txtError.setText(getResources().getString(R.string.email_missing_error_message));
                return;
            } else if (password.length() == 0) {
                txtError.setText(getResources().getString(R.string.password_missing_error_message));
                return;
            } else {

                progress.setMessage("Signing In, Please Wait..");
                progress.show();
                progress.setCanceledOnTouchOutside(false);

            }
        }

        hitAPI();
    }

    public void hitAPI()
    {
        final JSONObject params = new JSONObject();
        JSONObject header = new JSONObject();

        try {
            params.put("password", password);
            params.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = (getResources().getString(R.string.url)) + "sessions";

        HTTPrequest.callAPI("Post", url, params, header, new HTTPrequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                progress.dismiss();
                try {

                    JSONObject response = new JSONObject(result);
                    Boolean success = response.getBoolean("success");


                    if (success) {
                        JSONObject Data1 = response.getJSONObject("data");
                        JSONObject Data = Data1.getJSONObject("user");

                        sessionUser = new User(
                                Data.getInt("id"),
                                Data.getString("name"),
                                Data.getString("email"),
                                Data.getString("phone"),
                                Data.getString("authentication_token"),
                                Data.getString("source")
                        );

                        progress.dismiss();

                        if (!Data.getString("dob").equals(null)) {
                            sessionUser.setDate_of_birth(Data.getString("dob"));
                        }
                        if (!(Data.getString("city").equals(null))) {
                            sessionUser.setCity(Data.getString("city"));
                        }
                        if (!Data.getString("occupation").equals(null)) {
                            sessionUser.setOccupation(Data.getString("occupation"));
                        }

                        //  STORING LOCALLY THE DATA OF TEACHER FOR SESSION MANAGEMENT

                        editor = pref.edit();

                        editor.putBoolean("LoggedIn", true);
                        editor.putString("email", sessionUser.getEmail());
                        editor.putString("password", params.getString("password"));
                        editor.putString("token", sessionUser.getAuthentication_token());
                        editor.putString("source", sessionUser.getSource());


                        editor.apply();

                        //Toast.makeText(LogIn_REBORN.this, "HO GAYA LOGIN", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginPage.this, MainActivity.class);

                        Bundle b = new Bundle();
                        b.putSerializable("sessionUser", sessionUser);
                        intent.putExtras(b);

                        startActivity(intent);
                    }

                    if (!success) {
                        progress.dismiss();
                        if (!pref.getBoolean("LoggedIn", false)) {
                            txtError.setText(getResources().getString(R.string.invalid_credentials));
                            Toast.makeText(LoginPage.this, "Unable to log in", Toast.LENGTH_LONG).show();
                        } else {
                            editor = pref.edit();

                            editor.putBoolean("LoggedIn", false);
                            editor.putString("email", null);
                            editor.putString("token", null);
                            editor.putString("source", null);

                            editor.apply();
                        }

                        Log.i("Failed", "you Failed");
                    }

                } catch (JSONException e) {
                    System.out.println("you had" + e);
                }
            }

            @Override
            public void onFaliure(String faliure) {
                progress.dismiss();
                if (!pref.getBoolean("LoggedIn", false)) {
                    txtError.setText(getResources().getString(R.string.invalid_credentials));
                    Toast.makeText(LoginPage.this, "Unable to log in", Toast.LENGTH_LONG).show();
                } else {
                    editor = pref.edit();

                    editor.putBoolean("LoggedIn", false);
                    editor.putString("email", null);
                    editor.putString("token", null);
                    editor.putString("source", null);

                    editor.apply();
                }
            }
        }, LoginPage.this);

    }

    @Override
    protected void onResume() {
        isActive=1;
        super.onResume();
    }
    @Override
    protected void onPause() {
        isActive=0;
        super.onPause();
    }

    public void SignInWithGoogleOrFb()
    {
            progress.setMessage("Signing In, Please Wait..");
            progress.show();
            progress.setCanceledOnTouchOutside(false);

            JSONObject params = new JSONObject();
            JSONObject header = new JSONObject();

        try {
            params.put("name", name);
            params.put("email", email);
            params.put("phone", "03211111235");
            params.put("password", "password");
            params.put("password_confirmation", "password");
            if(flag_method==0)
                params.put("source", 1);
            else if(flag_method==1)
                params.put("source", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPrequest.callAPI("Post", (getResources().getString(R.string.url) + "sessions/signup"), params, header, new HTTPrequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject res = new JSONObject(result);

                    Boolean success = res.getBoolean("success");
                    if (success){
                        JSONObject Data1 = res.getJSONObject("data");

                        JSONObject Data = Data1.getJSONObject("user");

                        sessionUser = new User(
                                Data.getInt("id"),
                                Data.getString("name"),
                                Data.getString("email"),
                                Data.getString("phone"),
                                Data.getString("authentication_token"),
                                Data.getString("source")
                        );


                        if(!Data.getString("dob").equals(null))
                        {
                            sessionUser.setDate_of_birth(Data.getString("dob"));
                        }
                        if(!(Data.getString("city").equals(null)))
                        {
                            sessionUser.setCity(Data.getString("city"));
                        }
                        if(!Data.getString("occupation").equals(null))
                        {
                            sessionUser.setOccupation(Data.getString("occupation"));
                        }
                        progress.dismiss();


                        //  STORING LOCALLY THE DATA OF TEACHER FOR SESSION MANAGEMENT

                        editor = pref.edit();

                        editor.putBoolean("LoggedIn", true);
                        editor.putString("email", sessionUser.getEmail());
                        editor.putString("token", sessionUser.getAuthentication_token());
                        editor.putString("source", sessionUser.getSource());


                        editor.apply();

                        //Toast.makeText(LogIn_REBORN.this, "HO GAYA LOGIN", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginPage.this, MainActivity.class);

                        Bundle b = new Bundle();
                        b.putSerializable("sessionUser", sessionUser);
                        intent.putExtras(b);

                        startActivity(intent);
                    }

                    if (!success) {
                        progress.dismiss();
                        if (!pref.getBoolean("LoggedIn", false)) {
                            txtError.setText(getResources().getString(R.string.invalid_credentials));
                        } else {
                            editor = pref.edit();

                            editor.putBoolean("LoggedIn", false);
                            editor.putString("email", null);
                            editor.putString("token", null);
                            editor.putString("source", null);

                            editor.apply();
                        }


                        Log.i("Failed", "you Failed");
                    }

                } catch (JSONException e) {
                    progress.dismiss();
                    System.out.println("you had" + e);
                }
            }

            @Override
            public void onFaliure(String faliure) {
                Toast.makeText(LoginPage.this, "Sign In Error", Toast.LENGTH_SHORT).show();
                txtError.setText("Failed. Unable to sign in "+ faliure);
                progress.dismiss();
                return;
            }
        },this);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        //if (!permissionToRecordAccepted );

    }

    public void GoToForgotPassword(View view) {
        Intent forgot_password_intent = new Intent(this, ForgotPassword.class);
        startActivity(forgot_password_intent);
    }

    public void GoToHomePage(View view) {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
    }

    public void LogOutFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    public void LogOutGoogle() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Logged out (Google)", Toast.LENGTH_SHORT).show();
                    }
                });

            mGoogleSignInClient.revokeAccess()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(LoginPage.this, "Should revoke", Toast.LENGTH_SHORT).show();
                        }
                    });
    }
}