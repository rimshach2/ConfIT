package com.fyp.confit;
/*HI NIMRAAAAA*/
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SignupPage extends AppCompatActivity {

    EditText edtName;
    EditText edtEmail;
    EditText edtPhone;
    EditText edtPassword;
    ImageView toggleBtn;
    Boolean show = false;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    User sessionUser;
    ProgressDialog progress;
    ImageView signInWithGoogle;
    LoginButton signInWithFacebook;

    //Sign In with google
    int RC_SIGN_IN=0;
    GoogleSignInClient mGoogleSignInClient;
    String email, name, phone;
    int isActive=0;

    //Sign In with fb
    private static final String EMAIL = "email";
    int flag_method=-1;
    CallbackManager callbackManager;




    EditText edtPassword1;
    ImageView toggleBtn1;
    Boolean show1 = false;

    TextView txtError;

    Intent i1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        edtName = findViewById(R.id.SUname);
        edtEmail = findViewById(R.id.SUemail);
        edtPhone = findViewById(R.id.SUphone);

        edtPassword = findViewById(R.id.SUedt_password1);
        toggleBtn = findViewById(R.id.eye1);

        edtPassword1 = findViewById(R.id.SUedt_password2);
        toggleBtn1 = findViewById(R.id.eye2);

        signInWithGoogle= findViewById(R.id.google);
        signInWithFacebook=findViewById(R.id.fb);

        txtError = findViewById(R.id.error_message_display_txt_signup);

        pref  = getApplicationContext().getSharedPreferences("Pref",MODE_PRIVATE);
        progress= new ProgressDialog(this);
        i1 = new Intent(this, com.fyp.confit.LoginPage.class);


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goWithGoogle();
                flag_method=1;
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
                Toast.makeText(SignupPage.this, "came ehre", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(SignupPage.this, "error connecting to facebook", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(SignupPage.this, email, Toast.LENGTH_LONG).show();

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
            Toast.makeText(SignupPage.this, "Error while connecting..", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SignupPage.this, "Failed", Toast.LENGTH_LONG).show();
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
                startActivity(new Intent(SignupPage.this, MainActivity.class));
            }

        }
        super.onStart();
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
            params.put("phone", null);
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
                        Intent intent = new Intent(SignupPage.this, MainActivity.class);

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
                Toast.makeText(SignupPage.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                txtError.setText("Failed. Unable to sign up "+ faliure);
                progress.dismiss();
                return;
            }
        },this);


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

    public void eyeClicked1(View v){
        if (!show1){
            // show password
            edtPassword1.setTransformationMethod( HideReturnsTransformationMethod.getInstance());
            toggleBtn1.setImageResource(R.drawable.ic_eye_black_24dp);
            show1 = true;
        } else {
            // hide password
            edtPassword1.setTransformationMethod( PasswordTransformationMethod.getInstance());
            toggleBtn1.setImageResource(R.drawable.hide_password);
            show1 = false;
        }
    }

    public void GoToSignInPage(View view)
    {
        startActivity(i1);
    }

    public void signUp(View view) {
        if (edtName.getText().length()==0) {
            txtError.setText(getResources().getString(R.string.name_missing_error_message));
        } else if (edtEmail.getText().length()==0){
            txtError.setText(getResources().getString(R.string.email_missing_error_message));
        } else if (edtPhone.getText().length()==0) {
            txtError.setText(getResources().getString(R.string.number_missing_error_message));
        } else if(edtPhone.getText().toString().length()!=10) {
            txtError.setText("Length of phone number should be 10");
        } else if(!edtPhone.getText().toString().matches("[0-9]+")) {
            txtError.setText("invalid characters in phone number");
        } else if (edtPassword.getText().length()==0){
            txtError.setText(getResources().getString(R.string.password_missing_error_message));
        } else if (edtPassword1.getText().length()==0){
            txtError.setText(getResources().getString(R.string.Re_password_missing_error_message));
        } else if (!edtPassword.getText().toString().equals(edtPassword1.getText().toString())){
            txtError.setText(getResources().getString(R.string.passwords_dont_match));
        } else{

            JSONObject params = new JSONObject();
            JSONObject header = new JSONObject();

            try {
                params.put("name", edtName.getText());
                params.put("email", edtEmail.getText());
                params.put("phone", edtPhone.getText());
                params.put("password", edtPassword.getText());
                params.put("password_confirmation", edtPassword1.getText());
                params.put("source", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progress.setMessage("Signing up and Signing in...");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
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

                            progress.dismiss();


                            //  STORING LOCALLY THE DATA OF TEACHER FOR SESSION MANAGEMENT

                            editor = pref.edit();

                            editor.putBoolean("LoggedIn", true);
                            editor.putString("email", sessionUser.getEmail());
                            editor.putString("token", sessionUser.getAuthentication_token());
                            editor.putString("source", sessionUser.getSource());


                            editor.apply();

                            //Toast.makeText(LogIn_REBORN.this, "HO GAYA LOGIN", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupPage.this, MainActivity.class);

                            Bundle b = new Bundle();
                            b.putSerializable("sessionUser", sessionUser);
                            intent.putExtras(b);

                            startActivity(intent);
                        }

                        if (!success) {
                            progress.dismiss();
                            String err= res.getString("errors");
                            if (!pref.getBoolean("LoggedIn", false)) {
                                txtError.setText(err);
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
                    Toast.makeText(SignupPage.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                    txtError.setText("Failed. Unable to sign up "+ faliure);
                    progress.dismiss();
                    return;
                }
            },this);

        }
    }

    @Override
    public void onBackPressed(){
        startActivity(i1);
    }
}
