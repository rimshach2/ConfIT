package com.fyp.confit.ui.home;

import android.app.Presentation;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fyp.confit.HTTPrequest;
import com.fyp.confit.MainActivity;
import com.fyp.confit.PresentationClass;
import com.fyp.confit.R;
import com.fyp.confit.SignupPage;
import com.fyp.confit.User;
import com.fyp.confit.startPresentationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private int RESULT_SELECT_FILE = 1;
    Button newPresentationBtn;
    LinearLayout uploadFile;
    Uri slides;
    View root;
    Boolean doesSlidesExist=false;
    EditText title;
    RadioGroup audience, genre, env_type;
    Context context;
    User sessionUser;
    String env, audi, genr;
    int envEnum, audienceEnum, genreEnum;
    ProgressDialog progress;
    PresentationClass presentation_obj;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        context= (MainActivity)getActivity();
        progress= new ProgressDialog(context);

        MainActivity a= (MainActivity)getActivity();
        sessionUser=a.sessionUser;
        newPresentationBtn = root.findViewById(R.id.btnStartPresentation);
        uploadFile= root.findViewById(R.id.file_upload);

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("text/plain");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Slides.."), RESULT_SELECT_FILE);
            }
        });
        newPresentationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadAndStartPresentation();
                startPresentation();
            }
        });

        return root;
    }

    private void startPresentation() {
        title=root.findViewById(R.id.title1);
        audience=root.findViewById(R.id.audience_type);
        genre= root.findViewById(R.id.genre);
        env_type= root.findViewById(R.id.environment_type);

        if(title.getText().toString().equals("") || title.getText().toString().equals(" ") || title.getText().toString().contains("\n") || title.getText().toString().charAt(0)==' ')
        {
            Toast.makeText(context, "Please enter a valid presentation title",Toast.LENGTH_LONG).show();
            return;
        }

        int selectedId = audience.getCheckedRadioButtonId();
        RadioButton radioButton= audience.findViewById(selectedId);

        audi= radioButton.getText().toString();

        selectedId = genre.getCheckedRadioButtonId();
        radioButton= genre.findViewById(selectedId);

        genr= radioButton.getText().toString();

        selectedId = env_type.getCheckedRadioButtonId();
        radioButton= env_type.findViewById(selectedId);

        env= radioButton.getText().toString();

        findEnums();

        presentation_obj = new PresentationClass(title.getText().toString(),genr,env,audi,null,null,null,null,null);

        Intent intent = new Intent(context, startPresentationActivity.class);

        Bundle b = new Bundle();
        b.putSerializable("sessionUser", sessionUser);
        b.putSerializable("presentation", presentation_obj);
        if(doesSlidesExist)
            b.putSerializable("slides", slides.toString());
        b.putSerializable("slidesExist", doesSlidesExist);
        intent.putExtras(b);


        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == RESULT_SELECT_FILE && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            slides = data.getData( );
            doesSlidesExist=true;
            Toast.makeText(getActivity(), slides.getPath(),Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "Script Uploaded", Toast.LENGTH_SHORT).show();

        }
    }

    public void uploadAndStartPresentation()
    {
        title=root.findViewById(R.id.title1);
        audience=root.findViewById(R.id.audience_type);
        genre= root.findViewById(R.id.genre);
        env_type= root.findViewById(R.id.environment_type);

        if(title.getText().toString().equals(""))
        {
            Toast.makeText(context, "Please enter a valid presentation title",Toast.LENGTH_LONG).show();
            return;
        }


        int selectedId = audience.getCheckedRadioButtonId();
        RadioButton radioButton= audience.findViewById(selectedId);

        audi= radioButton.getText().toString();

        selectedId = genre.getCheckedRadioButtonId();
        radioButton= genre.findViewById(selectedId);

        genr= radioButton.getText().toString();

        selectedId = env_type.getCheckedRadioButtonId();
        radioButton= env_type.findViewById(selectedId);

        env= radioButton.getText().toString();

        findEnums();

        progress.setMessage("Creating Presentation, Please Wait..");
        progress.show();
        progress.setCanceledOnTouchOutside(false);

        JSONObject params = new JSONObject();
        JSONObject header = new JSONObject();

        try {
            params.put("user_token", sessionUser.getAuthentication_token());
            params.put("title", title.getText().toString());
            params.put("genre", genreEnum);
            params.put("env_type", envEnum);
            params.put("audience_type", audienceEnum);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPrequest.callAPI("Post", (getResources().getString(R.string.url) + "presentations"), params, header, new HTTPrequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject res = new JSONObject(result);

                    Boolean success = res.getBoolean("success");
                    if (success){
                        JSONObject Data1 = res.getJSONObject("data");

                        JSONObject Data = Data1.getJSONObject("presentations");

                        presentation_obj = new PresentationClass(
                                Data.getString("title"),
                                Data.getString("genre"),
                                Data.getString("env"),
                                Data.getString("audience"),
                                Data.getString("loudness_score"),
                                Data.getString("clarity_score"),
                                Data.getString("emotional_app_score"),
                                Data.getString("fluency_score"),
                                Data.getString("total_score")
                                );

                        progress.dismiss();


                        Intent intent = new Intent(context, startPresentationActivity.class);

                        Bundle b = new Bundle();
                        b.putSerializable("sessionUser", sessionUser);
                        b.putSerializable("presentation", presentation_obj);
                        intent.putExtras(b);

                        Toast.makeText(context, "Presentation created successfully", Toast.LENGTH_SHORT).show();


                        startActivity(intent);
                    }

                    if (!success) {
                        progress.dismiss();
                        Toast.makeText(context, "Unsuccessful attempt in creating presentation", Toast.LENGTH_SHORT).show();
                        Log.i("Failed", "you Failed");
                    }

                } catch (JSONException e) {
                    progress.dismiss();
                    System.out.println("you had" + e);
                }
            }

            @Override
            public void onFaliure(String faliure) {
                Toast.makeText(context, "Server Error in creating presentation", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                return;
            }
        },context);




    }

    public void findEnums()
    {
        if(env.equals("Class Room"))
        {
            envEnum=0;
        } else if(env.equals("Auditorium"))
        {
            envEnum=2;
        }

        if(genr.equals("Humorous"))
        {
            genreEnum=0;
        } else if(genr.equals("Serious"))
        {
            genreEnum=1;
        } else if(genr.equals("Emotional"))
        {
            genreEnum=2;
        }

        if(audi.equals("Noisy (Indecent)"))
        {
            audienceEnum=1;
        } else if(audi.equals("Serious (Decent)"))
        {
            audienceEnum=0;
        }else if(audi.equals("Moderate (Bored)"))
        {
            audienceEnum=2;
        }else if(audi.equals("Random (Any Audience)"))
        {
            Random rand= new Random();
            int randomNum = rand.nextInt((3 - 0) );

            if(randomNum<3)
            {
                audienceEnum=randomNum;
                String audi="Serious";
                if(audienceEnum==0){
                    audi="Serious";
                }else if(audienceEnum==1)
                {
                    audi="Noisy";
                }else if(audienceEnum==2)
                {
                    audi="Moderate";
                }
              //  presentation_obj.setAudience(audi);
            }
            else
            {
                audienceEnum=0;
            }
        }
    }
}