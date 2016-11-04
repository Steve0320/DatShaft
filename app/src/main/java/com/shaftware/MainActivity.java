package com.shaftware;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;

        import com.shaftware.shaftquack.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    } // end OnCreate

    // Called when the user clicks the Demo Button
    public void toDemo(View view) {
        startActivity(new Intent(MainActivity.this, ConversationActivity.class));
    }

} // end MainActivity
