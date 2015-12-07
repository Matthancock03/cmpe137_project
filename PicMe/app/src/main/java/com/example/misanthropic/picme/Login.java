package com.example.misanthropic.picme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.simplelogin.FirebaseSimpleLoginError;
import com.firebase.simplelogin.FirebaseSimpleLoginUser;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;


public class Login extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    public SimpleLogin authClient;
    private EditText user;
    private EditText pass;
    private Button login;
    private Button createuser;
    Firebase myFirebaseRef;
    String name;
    String email;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private boolean mGoogleIntentInProgress;

    public class Acc {
        private String email;

        public Acc() {}

        public Acc(String email){
            this.email = email;
        }

        public String getemail(){
            return email;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://PicMe.firebaseio.com");

        setContentView(R.layout.activity_login);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile().requestId().requestIdToken(getString(R.string.server_client_ID))
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]


    }

    @Override
    public void onStart() {
        super.onStart();

        myFirebaseRef = new Firebase("https://PicMe.firebaseio.com");

        user = (EditText) findViewById(R.id.UserField);
        pass = (EditText) findViewById(R.id.PassField);
        login = (Button) findViewById(R.id.Login);
        createuser = (Button) findViewById(R.id.createuser);

        authClient = new SimpleLogin(myFirebaseRef, getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                email = user.getText().toString();
                //Toast.makeText(getApplicationContext(), ""+email, Toast.LENGTH_SHORT).show();
            }
        });

        createuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createuser();
                //Firebase newaccref = myFirebaseRef.child("Users");
                //Acc newacc = new Acc(user.getText().toString().replace(".", ","));
                //newaccref.push().setValue(newacc);
            }
        });
        /*
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            Log.d("Google Result", result.getSignInAccount().getIdToken());
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

        }
        */
    }

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
            final GoogleSignInAccount acct = result.getSignInAccount();
            name = acct.getDisplayName();
            email = acct.getEmail();
            Log.d("Login Status: ", "Successful Login");
            Log.d("Email: ", result.getSignInAccount().getIdToken());
            try {
                getGoogleOAuthTokenAndLogin();
            }catch(Exception e){
                Log.d("Exception on Get Token", e.toString());
            }
        } else {
            Log.d("Debug: ", result.getStatus().toString());
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

    public void login(String id) {
        Log.d("USER_EMAIL", id);
        Bundle bundle = new Bundle();
        bundle.putString("USER_EMAIL", email);
        bundle.putString("NAME", name);
        Intent mainIntent = new Intent(this, MainActivity.class);

        mainIntent.putExtras(bundle);
        startActivity(mainIntent);
    }


    public void onConnected(final Bundle bundle) {
        /* Connected with Google API, use this to authenticate with Firebase */
        Log.d("ON Connected", "Connected");
        getGoogleOAuthTokenAndLogin();
    }

    // Async call to get google token for Firebase login.
    private void getGoogleOAuthTokenAndLogin() {
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scopes = "oauth2:profile email";
                    token = GoogleAuthUtil.getToken(getApplicationContext(), email, scopes);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, 1);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }

                return token;
            }

            // Executed on completion of Async getGoogleOAuthTokenAndLogin
            @Override
            protected void onPostExecute(String token) {
                if (token != null) {
                    myFirebaseRef.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            email = email.replaceAll("\\." , ",");
                            Log.d("Firebase: ", "Authenticated. Changing activity");
                            myFirebaseRef.child("users").child(authData.getUid()).child("email").setValue(email);
                            myFirebaseRef.child("users").child(authData.getUid()).child("permissions").setValue(email);

                            login(authData.getUid());
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Log.d("Firebase Error", firebaseError.toString());
                        }
                    });
                } else if (errorMessage != null) {
                    Log.d("Error: ", "onPostExecute error");

                }
            }
        };
        task.execute();
    }

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

    public void createuser(){
        authClient.createUser(user.getText().toString(), pass.getText().toString(), new SimpleLoginAuthenticatedHandler() {
            public void authenticated(FirebaseSimpleLoginError error, FirebaseSimpleLoginUser user) {
                if (error != null) {
                    Toast.makeText(Login.this, "Username is taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "New Account created", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }
}