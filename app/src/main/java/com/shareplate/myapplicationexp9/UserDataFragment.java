package com.shareplate.myapplicationexp9;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserDataFragment extends Fragment {

    private TextView tvUsername, tvPassword, tvUserId, tvSessionStatus;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    public UserDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        tvUsername = view.findViewById(R.id.tv_username);
        tvPassword = view.findViewById(R.id.tv_password);
        tvUserId = view.findViewById(R.id.tv_user_id);
        tvSessionStatus = view.findViewById(R.id.tv_session_status);

        Button btnBack = view.findViewById(R.id.btn_back);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        loadUserData();

        btnBack.setOnClickListener(v -> handleBackToLogin());
        btnLogout.setOnClickListener(v -> handleLogout());
    }

    private void loadUserData() {
        String loggedInUsername = sessionManager.getSavedUsername();
        String loginTime = sessionManager.getLoginTime();

        if (!loggedInUsername.isEmpty()) {
            User user = dbHelper.getUserByUsername(loggedInUsername);
            if (user != null) {
                tvUsername.setText(user.getUsername());
                tvPassword.setText(user.getPassword());
                tvUserId.setText(String.valueOf(user.getId()));
            } else {
                tvUsername.setText(loggedInUsername);
                tvPassword.setText(getString(R.string.not_available));
                tvUserId.setText(getString(R.string.not_applicable));
            }
            tvSessionStatus.setText(getString(R.string.status_logged_in_at, loginTime));
        } else {
            tvUsername.setText(getString(R.string.guest));
            tvPassword.setText(getString(R.string.not_applicable));
            tvUserId.setText(getString(R.string.not_applicable));
            tvSessionStatus.setText(getString(R.string.status_no_active_session));
        }
    }

    private void handleBackToLogin() {
        // Keeps the saved XML data intact and returns to LoginFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    private void handleLogout() {
        // Clears the SharedPreferences XML file data and returns to LoginFragment
        sessionManager.logoutUser();
        Toast.makeText(getContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }
}