package com.fyp.confit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateProfilePage extends AppCompatActivity {

    DatePickerDialog picker;
    EditText name, email, phone, dob, city, old_pass, new_pass, new_pass1;
    Spinner occupation;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ProgressDialog progress;
    User sessionUser;
    ImageView cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_page);

        Intent i= getIntent();
        Bundle b= new Bundle();
        b=i.getExtras();
        sessionUser= new User();
        sessionUser= (User)b.getSerializable("sessionUser");
        progress=new ProgressDialog(this);

        pref  = getApplicationContext().getSharedPreferences("Pref",MODE_PRIVATE);
         name= findViewById(R.id.Uname);
         email= findViewById(R.id.Uemail);
         phone= findViewById(R.id.Uphone);
         dob= findViewById(R.id.Udob);
         city= findViewById(R.id.Ucity);
         occupation= findViewById(R.id.Uoccupation);
         old_pass=findViewById(R.id.Uoldpassword);
        new_pass=findViewById(R.id.Unewpassword);
        new_pass1=findViewById(R.id.Unewpassword1);

        if(sessionUser.source.equals("google") || sessionUser.source.equals("facebook"))
        {
            old_pass.setEnabled(false);
            new_pass.setEnabled(false);
            new_pass1.setEnabled(false);

        }

        cal=findViewById(R.id.cal);

        //call api and get data here
        setValues();

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(UpdateProfilePage.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    public void setValues() {

        name.setText(sessionUser.getName());
        email.setText(sessionUser.getEmail());
        phone.setText(sessionUser.getPhone());


        if (sessionUser.getDate_of_birth() != null) {
            dob.setText(sessionUser.getDate_of_birth());
        }
        if (sessionUser.getCity() != null) {
            city.setText(sessionUser.getCity());
        }
        if (sessionUser.getOccupation() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.occupation_arr, android.R.layout.simple_spinner_item);


            for (int i = 0; i < adapter.getCount(); i++) {
                if (sessionUser.getOccupation().equals(adapter.getItem(i).toString())) {
                    occupation.setSelection(i);
                    break;
                }
            }
        }
    }

    public void Update(View v) throws ParseException {


        JSONObject params = new JSONObject();
        JSONObject header = new JSONObject();

        if(phone.getText().toString().length()!=10)
        {
            Toast.makeText(getApplicationContext(), "Length of phone number should be 10", Toast.LENGTH_LONG).show();
            return;
        }
        if(!phone.getText().toString().matches("[0-9]+"))
        {
            Toast.makeText(getApplicationContext(), "invalid characters in phone number", Toast.LENGTH_LONG).show();
            return;
        }
        if(dob.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "please enter date of birth", Toast.LENGTH_LONG).show();
            return;
        }
        Date date=new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date= dateFormatter.parse(dob.getText().toString());
            //date=dateFormatter.format(date);

        }catch (ParseException e)
        {
            e.printStackTrace();
        }
        //SimpleDateFormat curr= new SimpleDateFormat("dd-MM-yyyy");

        Date todayDate = new Date();
        if(date.compareTo(todayDate)>0 || date.compareTo(todayDate)==0)
        {
            Toast.makeText(getApplicationContext(), "Date of birth can not be greater or equal to today's date", Toast.LENGTH_LONG).show();
            return;
        }


        if(dob.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "please enter date of birth", Toast.LENGTH_LONG).show();
            return;
        }

        if(city.getText().toString().equals("") || city.getText().toString().equals(" ") || city.getText().toString().matches("[0-9]+"))
        {
            Toast.makeText(getApplicationContext(), "Invalid city", Toast.LENGTH_LONG).show();
            return;
        }
        progress.setMessage("Updating, Please Wait..");
        progress.show();
        progress.setCanceledOnTouchOutside(false);

        try {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.occupation_arr, android.R.layout.simple_spinner_item);

            params.put("name", name.getText().toString());
            params.put("user_token", sessionUser.getAuthentication_token());
            params.put("phone", phone.getText().toString());
            params.put("city", city.getText().toString());
            params.put("dob", dob.getText().toString());
            params.put("occupation", adapter.getItem(occupation.getSelectedItemPosition()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPrequest.callAPI("Post", (getResources().getString(R.string.url) + "sessions/edit_profile"), params, header, new HTTPrequest.VolleyCallback() {
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


                        sessionUser.setDate_of_birth(Data.getString("dob"));
                        sessionUser.setCity(Data.getString("city"));
                        sessionUser.setOccupation(Data.getString("occupation"));

                        progress.dismiss();
                        Toast.makeText(UpdateProfilePage.this, "Successfully updated profile", Toast.LENGTH_SHORT).show();
                        MainActivity a= new MainActivity();
                        a.sessionUser=sessionUser;

                        //  STORING LOCALLY THE DATA OF TEACHER FOR SESSION MANAGEMENT

                        editor = pref.edit();

                        editor.putBoolean("LoggedIn", true);
                        editor.putString("email", sessionUser.getEmail());
                        editor.putString("token", sessionUser.getAuthentication_token());
                        editor.putString("source", sessionUser.getSource());


                        editor.apply();

                    }

                    if (!success) {
                        progress.dismiss();
                        if (!pref.getBoolean("LoggedIn", false)) {
                            Toast.makeText(UpdateProfilePage.this,getResources().getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show();
                        } else {
                            editor = pref.edit();

                            editor.putBoolean("LoggedIn", false);
                            editor.putString("email", null);
                            editor.putString("token", null);
                            editor.putString("source", null);

                            editor.apply();
                        }

                        Toast.makeText(UpdateProfilePage.this,"Unsuccessful attempt", Toast.LENGTH_SHORT).show();
                        Log.i("Failed", "you Failed");
                    }

                } catch (JSONException e) {
                    progress.dismiss();
                    System.out.println("you had" + e);
                }
            }

            @Override
            public void onFaliure(String faliure) {
                Toast.makeText(UpdateProfilePage.this, "Backend Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                return;
            }
        },this);


    }

    public void UpdatePassword(View v)
    {
        if(sessionUser.source.equals("google") || sessionUser.source.equals("facebook"))
        {
            Toast.makeText(this, "You can not change password when logged in using google or facebook", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            if (old_pass.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter your old password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (new_pass.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter your new password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (new_pass1.getText().toString().equals("")) {
                Toast.makeText(this, "Please re-enter your new password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!new_pass.getText().toString().equals(new_pass1.getText().toString())) {
                Toast.makeText(this, "Your new password and confirmation password does not match", Toast.LENGTH_LONG).show();
                return;
            }
            if (old_pass.getText().toString().equals(new_pass.getText().toString())) {
                Toast.makeText(this, "Your new password and old password is same", Toast.LENGTH_SHORT).show();
                return;
            }

            hitAPI();

        }


    }

    public void hitAPI()
    {
        progress.setMessage("Updating password, Please Wait..");
        progress.show();
        progress.setCanceledOnTouchOutside(false);

        JSONObject params = new JSONObject();
        JSONObject header = new JSONObject();

        try {
            params.put("user_token", sessionUser.getAuthentication_token());
            params.put("password", new_pass.getText().toString());
            params.put("password_confirmation", new_pass1.getText().toString());
            params.put("current_password", old_pass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPrequest.callAPI("Post", (getResources().getString(R.string.url) + "sessions/reset_password"), params, header, new HTTPrequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject res = new JSONObject(result);

                    Boolean success = res.getBoolean("success");
                    if (success) {
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


                        sessionUser.setDate_of_birth(Data.getString("dob"));
                        sessionUser.setCity(Data.getString("city"));
                        sessionUser.setOccupation(Data.getString("occupation"));

                        progress.dismiss();

                        Toast.makeText(UpdateProfilePage.this, "Password has been updated successfully", Toast.LENGTH_LONG).show();
                        //  STORING LOCALLY THE DATA OF TEACHER FOR SESSION MANAGEMENT
                    }

                    if (!success) {
                        progress.dismiss();
                        Toast.makeText(UpdateProfilePage.this, "Password has NOT been updated", Toast.LENGTH_LONG).show();
                        Log.i("Failed", "you Failed");
                    }

                } catch (JSONException e) {
                    progress.dismiss();
                    System.out.println("you had" + e);
                }
            }

            @Override
            public void onFaliure(String faliure) {
                Toast.makeText(UpdateProfilePage.this, "Server Error", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                return;
            }
        }, this);

    }

    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent();
        Bundle b= new Bundle();
        b.putSerializable("sessionUser", sessionUser);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
       // super.onBackPressed();
    }
}
