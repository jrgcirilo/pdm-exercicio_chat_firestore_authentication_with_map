package com.example.exercicio_chat_firestore_authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewUserActivity extends AppCompatActivity {

    private EditText lgnNewUserEditText;
    private EditText pwdNewUserEditText2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_user);
        lgnNewUserEditText =
                findViewById(R.id.lgnNewUserEditText);
        pwdNewUserEditText2 =
                findViewById(R.id.pwdNewUserEditText2);
        mAuth = FirebaseAuth.getInstance();
    }

    public void createNewUser(View view) {
        String login =
                lgnNewUserEditText.getText().toString();
        String password =
                pwdNewUserEditText2.getText().toString();
        Task<AuthResult> task =
                mAuth.createUserWithEmailAndPassword(login,password);



        task.addOnSuccessListener(authResult -> { Toast.makeText(this, getString(R.string.userOK), Toast.LENGTH_SHORT).show();
        });
        task.addOnFailureListener(e ->  Toast.makeText(this, getString(R.string.userNoOK), Toast.LENGTH_SHORT).show());
    }

    public void backNewUser(View view){
        startActivity(new Intent(this,
                MainActivity.class
        ));
    }

}
