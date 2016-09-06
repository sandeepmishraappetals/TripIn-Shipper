package tripin.com.tripin_shipper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import tripin.com.tripin_shipper.R;
import tripin.com.tripin_shipper.SharedPreference.SharedPreference;
import tripin.com.tripin_shipper.helper.SQLiteHandler;
import tripin.com.tripin_shipper.helper.SessionManager;

public class DashBoardActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtEmail, tv;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;
    private String text;

    private SharedPreference sharedPreference;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        tv = (TextView) findViewById(R.id.tv_id);
        sharedPreference = new SharedPreference();

        //Retrieve a value from SharedPreference
        text = sharedPreference.getValue(context);
        tv.setText(text);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
       /* User user1 = new User();
        String uid=user.get("uid");

        Log.e("@@ UID", uid);*/

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
   /*  Intent intent = new Intent(DashBoardActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();*/

    }
  }
