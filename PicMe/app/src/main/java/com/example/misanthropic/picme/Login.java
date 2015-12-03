package com.example.misanthropic.picme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.simplelogin.FirebaseSimpleLoginError;
import com.firebase.simplelogin.FirebaseSimpleLoginUser;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;


public class Login extends AppCompatActivity //implements
       /* GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener*/ {

    private EditText user;
    private EditText pass;
    private Button login;

    public SimpleLogin authClient;

    Firebase myFirebaseRef;
    //private static final String TAG = "SignInActivity";
    //private static final int RC_SIGN_IN = 9001;

    /*
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);


        setContentView(R.layout.activity_login);
        /*
        Button nextview = (Button) findViewById(R.id.nextview);

        nextview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AlbumView.class);
                startActivity(i);
            }
        });

            // Views
            //mStatusTextView = (TextView) findViewById(R.id.status);

            // Button listeners
            findViewById(R.id.sign_in_button).setOnClickListener(this);
            //findViewById(R.id.sign_out_button).setOnClickListener(this);
            //findViewById(R.id.disconnect_button).setOnClickListener(this);

            // [START configure_signin]
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // [END configure_signin]

            // [START build_client]
            // Build a GoogleApiClient with access to the Google Sign-In API and the
            // options specified by gso.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity *///, this /* OnConnectionFailedListener */)
                    //.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    //.build();
            // [END build_client]

            // [START customize_button]
            // Customize sign-in button. The sign-in button can be displayed in
            // multiple sizes and color schemes. It can also be contextually
            // rendered based on the requested scopes. For example. a red button may
            // be displayed when Google+ scopes are requested, but a white button
            // may be displayed when only basic profile is requested. Try adding the
            // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
            // difference.
        /*
            SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setScopes(gso.getScopeArray());
            // [END customize_button]*/
        }

        @Override
        public void onStart() {
            super.onStart();

            myFirebaseRef = new Firebase("https://shining-heat-4056.firebaseio.com/");

            user = (EditText) findViewById(R.id.UserField);
            pass = (EditText) findViewById(R.id.PassField);
            login = (Button) findViewById(R.id.Login);
            authClient = new SimpleLogin(myFirebaseRef, getApplicationContext());

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
            /*
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }*/
        }

    /*
        // [START onActivityResult]
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
        // [END onActivityResult]

        // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText("Successful Login");

            //Register user with Firebase
            myFirebaseRef.authWithOAuthToken("google", acct.getIdToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    login(acct.getEmail());
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                }
            });
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
    public void login(String email){
     Intent mainIntent = new Intent(this, MainActivity.class);
     mainIntent.putExtra("USER_EMAIL", email);
     startActivity(mainIntent);
    }*/

    public void GoToActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void login(){
        authClient.loginWithEmail(user.getText().toString(), pass.getText().toString(), new SimpleLoginAuthenticatedHandler() {
            public void authenticated(FirebaseSimpleLoginError error, FirebaseSimpleLoginUser user) {
                if (error != null) {
                    // There was an error logging into this account
                } else {
                    // We are now logged in
                    GoToActivity();
                }
            }
        });
    }



}
