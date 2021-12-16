package com.greenhouse.android.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.greenhouse.android.R;
import com.greenhouse.android.ViewModel.ProfileViewModel;
import com.greenhouse.android.Wrappers.User;
import com.greenhouse.android.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private User currentUser;

    private Button saveButton;

    private TextView username;
    private TextView email;
    private EditText password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //getArguments().getSerializable() GET THE CURRENT USER
        //updateFields(currentUser);

        saveButton = root.findViewById(R.id.profileSaveButton);

        username = root.findViewById(R.id.usernameTextView);
        email = root.findViewById(R.id.emailTextView);
        password = root.findViewById(R.id.editTextTextPassword);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

//
        return root;
    }


//    //Old method
//    public void OnViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
//        saveButton = getView().findViewById(R.id.profileSaveButton);
//
//        username = getView().findViewById(R.id.usernameTextView);
//        email = getView().findViewById(R.id.emailTextView);
//        password = getView().findViewById(R.id.editTextTextPassword);
//
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateUser();
//            }
//        });
//    }

    private void updateFields(User current){
        username.setText(current.getName());
        email.setText(current.getEmail());
        password.setText(current.getPassword());
    }

    private void updateUser(){
        try {
            String newPassword = password.getText().toString();
            User updated = new User(email.getText().toString(),newPassword);
            profileViewModel.updateUser(updated);
        }  catch (Exception e) {
            Toast.makeText(getContext(), "Password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}