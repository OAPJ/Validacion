package com.example.alumno.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private TextView info;
    private Button singUpbtn,signOutbtn, singInbtn;
    private EditText correo, contra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        info= findViewById(R.id.info);
        singInbtn= findViewById(R.id.signIn);
        signOutbtn=findViewById(R.id.signOut);
        singUpbtn=findViewById(R.id.signUp);
        correo= findViewById(R.id.email);
        contra=findViewById(R.id.pass);
        singUpbtn.setOnClickListener(this);
        singInbtn.setOnClickListener(this);
        signOutbtn.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    public void signUp(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fallo en la creacion"+ e.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fallo en inicio de sesion "+e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getCurrenUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            info.setText(email+" "+ name);
    }
}
    private void updateUI(FirebaseUser firebaseUser){
        if(firebaseUser==null){
            signOutbtn.setVisibility(View.INVISIBLE);
            singInbtn.setVisibility(View.VISIBLE);
            singUpbtn.setVisibility(View.VISIBLE);
            info.setText(R.string.info);
        }else {
            signOutbtn.setVisibility(View.VISIBLE);
            singInbtn.setVisibility(View.INVISIBLE);
            singUpbtn.setVisibility(View.INVISIBLE);
            getCurrenUser();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.signIn){
            signIn(correo.getText().toString(),contra.getText().toString());
        }else if (view.getId()==R.id.signUp){
            signUp(correo.getText().toString(),contra.getText().toString());
        }else{
            FirebaseAuth.getInstance().signOut();
            updateUI(null);
        }
    }
}
