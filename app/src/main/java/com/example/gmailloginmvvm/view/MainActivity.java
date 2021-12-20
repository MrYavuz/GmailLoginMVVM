package com.example.gmailloginmvvm.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gmailloginmvvm.R;
import com.example.gmailloginmvvm.databinding.ActivityMainBinding;
import com.example.gmailloginmvvm.model.UserModel;
import com.example.gmailloginmvvm.viewModel.MainViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Context context;
    private MainViewModel mainViewModel;

    private final ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    mainViewModel.handleSignInResult(task);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplication().getApplicationContext();

        final Button loginGmailButton = binding.btnGmail;
        final Button logoutGmailButton = binding.btnGmailExit;

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        loginGmailButton.setOnClickListener(v -> getIdToken());
        logoutGmailButton.setOnClickListener(v -> mainViewModel.signOut());

        mainViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }

            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
            }

        });
    }

    private void getIdToken() {
        // Show an account picker to let the user choose a Google account from the device.
        // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
        // consent screen will be shown here.
        Intent signInIntent = mainViewModel.getSignInIntent();
        mStartForResult.launch(signInIntent);

    }

    private void updateUiWithUser(UserModel user) {
        String welcome = getString(R.string.welcome) + user.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}