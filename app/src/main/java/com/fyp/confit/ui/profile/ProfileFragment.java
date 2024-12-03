package com.fyp.confit.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.confit.HistoryAdapter;
import com.fyp.confit.MainActivity;
import com.fyp.confit.PracticeData;
import com.fyp.confit.R;
import com.fyp.confit.UpdateProfilePage;
import com.fyp.confit.User;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    User sessionUser;
    Context context;
    ImageView editButton;
    View root;
    Bundle b=new Bundle();
    Button editButton1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
         root = inflater.inflate(R.layout.fragment_profile, container, false);

        context= (MainActivity) getActivity();
        MainActivity a= (MainActivity) getActivity();
        sessionUser=a.sessionUser;

        editButton = root.findViewById(R.id.editProfile);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UpdateProfilePage.class);
                b.putSerializable("sessionUser", sessionUser);
                i.putExtras(b);
                startActivityForResult(i, 1);
            }
        });
        editButton1= root.findViewById(R.id.button2);
        editButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), UpdateProfilePage.class);
                b.putSerializable("sessionUser", sessionUser);
                i.putExtras(b);
                startActivityForResult(i,1);
            }
        });

        setVariables();

        /*final TextView textView = root.findViewById(R.id.text_notifications);
        ProfileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    public void setVariables()
    {

        EditText name= root.findViewById(R.id.Uname);
        EditText email= root.findViewById(R.id.Uemail);
        EditText phone= root.findViewById(R.id.Uphone);
        EditText dob= root.findViewById(R.id.Udob);
        EditText city= root.findViewById(R.id.Ucity);
        EditText occupation= root.findViewById(R.id.Uoccupation);

        name.setText(sessionUser.getName());
        email.setText(sessionUser.getEmail());
        phone.setText(sessionUser.getPhone());
        if(sessionUser.getDate_of_birth() != null) {
            dob.setText(sessionUser.getDate_of_birth());
        }
        if(sessionUser.getCity() != null) {
            city.setText(sessionUser.getCity());
        }
        if(sessionUser.getOccupation() != null) {
            occupation.setText(sessionUser.getOccupation());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle b= new Bundle();
                b = data.getExtras();
                sessionUser=(User)b.getSerializable("sessionUser");
                MainActivity a= (MainActivity) getActivity();
                a.sessionUser=sessionUser;
                setVariables();
            }
        }
    }

}