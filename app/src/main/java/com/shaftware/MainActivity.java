package com.shaftware;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.preference.PreferenceManager;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Toast;

        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.shaftware.shaftquack.R;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "MainActivity";
    private final String ANONYMOUS = "anonymous";

    //Google and Firebase Resourses
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    private String mUsername;
    private String mPhotoURL;

    //Check if a user is currently logged in. If not,
    //transfer control to login screen.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsername = ANONYMOUS;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            Log.d(TAG, "Got user" + mUsername);
            mUsername = mFirebaseUser.getDisplayName();
            mPhotoURL = mFirebaseUser.getPhotoUrl().toString();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    } // end onCreate

    //Action listener for logout button. Log the user out of the system
    //and return to the sign in page (user may perform no further actions
    //until signed in again.
    public void handleLogout(View v) {
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mUsername = ANONYMOUS;
        startActivity(new Intent(this, SignInActivity.class));
        return;
    }

    //Handle a failed connection to Google servers
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show();
    }

    // Called when the user clicks the Demo Button
    public void toDemo(View view) {
        startActivity(new Intent(MainActivity.this, ConversationActivity.class));
    }

} // end MainActivity
