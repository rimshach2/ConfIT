package com.fyp.confit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {

    ProgressDialog pd;
    String email, user_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Intent i1= getIntent();
        Bundle b= i1.getExtras();

        email= (String) b.getSerializable("email");
        user_token= (String) b.getSerializable("user_token");

        pd=new ProgressDialog(this);
    }


    public void onClickReset(View v)
    {
        EditText p1= (EditText) findViewById(R.id.Rpass);
        EditText p2= (EditText) findViewById(R.id.Rpass1);

        String pass1= p1.getText().toString();
        String pass2= p2.getText().toString();
        if(pass1.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter a valid password",Toast.LENGTH_LONG).show();
            return;
        }
        if(pass2.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please Re-enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(!pass1.equals(pass2))
        {
            Toast.makeText(getApplicationContext(),"Passwords do not match !!!",Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, String> session = new HashMap<>( );
        Map<String, Map> requestData = new HashMap<>( );
        Map<String, String> headers = new HashMap<>( );

        session.put("email", email);
        session.put( "password", pass1);
        session.put("password_confirmation", pass2);

        pd.setMessage("Resetting Password, please wait...");
        pd.show();

        HTTPrequest.placeRequest( (getResources().getString(R.string.url) + "sessions/forgot_password_update"), "Post", session, headers, new HTTPrequest.VolleyCallback( ) {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject res = new JSONObject(result);

                    Boolean success = res.getBoolean("success");
                    if (success) {
                        pd.dismiss();
                        Toast.makeText(ResetPassword.this, "Password has been updated successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent( getApplicationContext(), LoginPage.class );

                        startActivity( intent );
                        //  STORING LOCALLY THE DATA OF TEACHER FOR SESSION MANAGEMENT
                    }

                    if (!success) {
                        pd.dismiss();
                        Toast.makeText(ResetPassword.this, "Password has NOT been updated", Toast.LENGTH_LONG).show();
                        Log.i("Failed", "you Failed");
                    }

                } catch (JSONException e) {
                    pd.dismiss();
                    System.out.println("you had" + e);
                }
            }

            @Override
            public void onFaliure(String faliure) {
                Toast.makeText(ResetPassword.this, "Server Error", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
        }, this);



    }
    public void goBack2(View v)
    {
        Intent intent = new Intent( getApplicationContext(), LoginPage.class );

        startActivity( intent );
    }


    @Override
    public void onBackPressed()
    {
        Intent i= new Intent(this, LoginPage.class);
        startActivity(i);
    }
}
