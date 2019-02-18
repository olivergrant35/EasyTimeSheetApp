package com.olivergrant.oliver.easytimesheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_AdminLogin extends AppCompatActivity {

    /*
    Could do admin login differently, the admins could have their own dedicated QR code or numeric code
    which they can scan or enter, the application will realise it is an admin log in and not a normal
    sign in. This will then automatically open the admin page. This might be better as it is quicker
    and means an admin will not have to remember a username and password that they may not use often.
    This would also already be built in functionality, therefore be easy to implement and save time.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__admin_login);

        final Button buttonLogin = findViewById(R.id.buttonAdminLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Need to create admin file and verify when someone tries to log in.
            }
        });
    }
}
