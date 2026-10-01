package com.shareplate.myapplicationexp9;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private TextInputEditText etUsername, etPassword;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);

        Button btnLogin = view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v -> handleLogin());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveCurrentDraft();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etUsername.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);

        populateFieldsFromSession();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateFieldsFromSession();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveCurrentDraft();
    }

    private void saveCurrentDraft() {
        if (etUsername != null && etPassword != null && sessionManager != null) {
            String u = etUsername.getText() != null ? etUsername.getText().toString() : "";
            String p = etPassword.getText() != null ? etPassword.getText().toString() : "";
            sessionManager.saveDraftCredentials(u, p);
        }
    }

    private void populateFieldsFromSession() {
        if (etUsername != null && etPassword != null && sessionManager != null) {
            String savedUser = sessionManager.getSavedUsername();
            String savedPass = sessionManager.getSavedPassword();
            String currentU = etUsername.getText() != null ? etUsername.getText().toString() : "";
            String currentP = etPassword.getText() != null ? etPassword.getText().toString() : "";

            if (!Objects.equals(savedUser, currentU)) {
                etUsername.setText(savedUser);
            }
            if (!Objects.equals(savedPass, currentP)) {
                etPassword.setText(savedPass);
            }
        }
    }

    private void handleLogin() {
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter Username and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.checkUsernameExists(username)) {
            dbHelper.insertUser(username, password);
        }

        sessionManager.createLoginSession(username, password);
        Toast.makeText(getContext(), "Data saved in SharedPreferences XML & SQLite!", Toast.LENGTH_SHORT).show();

        navigateToUserData();
    }

    private void navigateToUserData() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new UserDataFragment())
                .commit();
    }
}