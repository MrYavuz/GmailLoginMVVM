package com.example.gmailloginmvvm.viewModel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gmailloginmvvm.R;
import com.example.gmailloginmvvm.model.LoginResult;
import com.example.gmailloginmvvm.model.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";
    private final Application application;
    public GoogleSignInClient googleSignInClient;
    Intent signInIntent;

    public MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        initGoogleSignIn();
    }

    public Intent getSignInIntent(){
        return signInIntent = googleSignInClient.getSignInIntent();
    }

    private void initGoogleSignIn(){

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getApplication().getApplicationContext().getString(R.string.server_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getApplication(), gso);

    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // TODO(developer): send ID Token to server and validate
            String idToken = account.getIdToken();
            String displayName = account.getDisplayName();

            if(idToken != null) {
                Log.i(TAG,"GoogleIdToken: " + idToken);
                UserModel usr = new UserModel(displayName, idToken);
                updateUI(usr);
            }
        } catch (ApiException e) {
            updateUI(null);
            Log.e(TAG, "handleSignInResult:error", e);
        }
    }

    public void signOut() {
        googleSignInClient.signOut().addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 // If you wish you can update UI here
                Toast.makeText(application.getApplicationContext(), "signOut success", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void revokeAccess() {
        googleSignInClient.revokeAccess().addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // If you wish you can update UI here
                        Toast.makeText(getApplication().getApplicationContext(), "revokeAccess success", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateUI(UserModel userModel){

        if(userModel != null){
            loginResult.setValue(new LoginResult(userModel));
        }else{
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

}
