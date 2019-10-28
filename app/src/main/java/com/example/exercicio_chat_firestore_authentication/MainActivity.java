package com.example.exercicio_chat_firestore_authentication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText lgnEditText;
    private EditText pwdEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lgnEditText = findViewById(R.id.lgnEditText);
        pwdEditText = findViewById(R.id.pwdEditText2);
                mAuth = FirebaseAuth.getInstance();
    }

    public void makeLogin(View view){
        String lgn = lgnEditText.getText().toString();
        String pwd = pwdEditText.getText().toString();
        mAuth.signInWithEmailAndPassword(
                lgn,
                pwd
        ).addOnSuccessListener((success) -> {
            startActivity(
                    new Intent(
                            this,
                            ChatActivity.class
                    )
            );
        })
                .addOnFailureListener((exception) -> {
                    Toast.makeText(this, getString(R.string.userNoLogin), Toast.LENGTH_SHORT).show();
                });



    }

    public void makeRegistration(View view){
        startActivity(new Intent(this,
                NewUserActivity.class
        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
