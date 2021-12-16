package com.greenhouse.android.View.Fragments;

import android.content.Intent;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.greenhouse.android.R;
import com.greenhouse.android.View.LoginActivity;
import com.greenhouse.android.ViewModel.ProfileViewModel;
import com.greenhouse.android.Wrappers.APIResponse.LoggedUser;
import com.greenhouse.android.Wrappers.User;
import com.greenhouse.android.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private User currentUser;

    private Button saveButton;
    private Button logoutButton;

    private TextView username;
    private TextView email;
    private EditText password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profileViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<LoggedUser>() {
            @Override
            public void onChanged(LoggedUser loggedUser) {
                if (loggedUser.getUser() != null) {
                    updateFields(loggedUser.getUser());
                } else {
                    Toast.makeText(getContext(), "Please check your connection!", Toast.LENGTH_SHORT).show();
//                    Navigation.findNavController(getView()).navigate(R.id.navigation_home);
                }
            }
        });

        saveButton = root.findViewById(R.id.profileSaveButton);
        logoutButton = root.findViewById(R.id.profile_button_logout);

        username = root.findViewById(R.id.usernameTextView);
        email = root.findViewById(R.id.emailTextView);
        password = root.findViewById(R.id.editTextTextPassword);

        saveButton.setOnClickListener(v -> updateUser());

        logoutButton.setOnClickListener(v->{
            profileViewModel.logout();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });
        return root;
    }

    private void updateFields(User current){
        username.setText(current.getName());
        email.setText(current.getEmail());
        password.setText("password");
    }

    private void updateUser(){
        try {
            String newPassword = password.getText().toString();
            profileViewModel.updatePassword(newPassword);
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