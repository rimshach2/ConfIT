package com.fyp.confit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        pd=new ProgressDialog(this);
    }

    public void goBack(View view) {
        Intent i = new Intent(this, LoginPage.class);
        startActivity(i);
    }

    public void onClick(View v)
    {
        EditText ed= (EditText) findViewById(R.id.edt_email);

        final String email;
        email= ed.getText().toString();

        if(email.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please Enter your Email", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> session = new HashMap<>( );
        Map<String, Map> requestData = new HashMap<>( );
        Map<String, String> headers = new HashMap<>( );

        session.put( "email", email);

        pd.setMessage("Sending Sms...");
        pd.show();

        HTTPrequest.placeRequest( (getResources().getString(R.string.url))+"sessions/forgot_password?email="+email, "Post", session, headers, new HTTPrequest.VolleyCallback( ) {
            @Override
            public void onSuccess( String result ) {

                try {
                    JSONObject studentRes = new JSONObject( result );
                    Boolean success = studentRes.getBoolean( "success" );


                    if (success) {
                        JSONObject student = studentRes.getJSONObject( "data" );
                        JSONObject studentData= student.getJSONObject("user");
                        pd.dismiss();
                        Intent intent = new Intent( getApplicationContext(), ForgotPassword1.class );

                        Bundle b = new Bundle(  );
                        b.putSerializable( "user_token", studentData.getString("authentication_token") );
                        b.putSerializable( "email", email );
                        b.putSerializable( "phone", studentData.getString("phone") );
                      //  b.putSerializable("password", studentData.getString("password"));
                        intent.putExtras( b );

                        startActivity( intent );

                    }

                    if (!success) {

                        pd.dismiss();
                        JSONObject errorRes = new JSONObject( result );
                        Toast.makeText(getApplicationContext(), errorRes.getString("errors"), Toast.LENGTH_SHORT ).show();

                        Log.i( "Failed", "you Failed" );
                    }

                } catch (JSONException e) {
                    pd.dismiss();

                    System.out.println( "you had" + e );
                }

                System.out.println( result );

            }

            @Override
            public void onFaliure( String faliure ) {

                pd.dismiss();
                System.out.println( "failed bro" );
            }
        }, ForgotPassword.this );

    }

}
