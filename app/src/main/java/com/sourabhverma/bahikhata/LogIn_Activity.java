package com.sourabhverma.bahikhata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LogIn_Activity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReferenceToUser;

    //Google
    private GoogleSignInClient googleSignInClient;

    //Constants
    private int CONST = 1;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_);

        //Initialization
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceToUser = firebaseDatabase.getReference().child("User");

        //======================================================================================

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

    }

    public void setSignInButton(View view) {

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, CONST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONST) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                handleSignInResult(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {

        AuthCredential authCredential = null;
        try {
            authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert authCredential != null;
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    firebaseUser = firebaseAuth.getCurrentUser();
                    try {
                        assert firebaseUser != null;
                        id = firebaseUser.getUid();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String essai = "essai";

                    updateUI(essai);

                    databaseReferenceToUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            if (account != null) {

                                final String personname = account.getDisplayName();
                                final String personemail = account.getEmail();
                                String personphotostring = null;
                                try {
                                    Uri personphoto = account.getPhotoUrl();
                                    assert personphoto != null;
                                    personphotostring = personphoto.toString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                final String finalPersonphotostring = personphotostring;

                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("name",personname);
                                hashMap.put("email",personemail);
                                hashMap.put("imageUrl", finalPersonphotostring);

                                databaseReferenceToUser.child(id).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "onSuccess: 1");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LogIn_Activity.this,"Registering Data In Our Database is Failed",Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "onSuccess: 2");
                                    }
                                });

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Log.e("TAG", "onCancelled: 1");

                        }
                    });

                } else {

                    Toast.makeText(LogIn_Activity.this, "We Can Not Reach To Our DataBase", Toast.LENGTH_SHORT).show();
                    String failed = "failed";
                    updateUI(failed);

                }

            }

        });

    }

    private void updateUI(String s) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null && !s.equals("failed")) {

            Intent intent = new Intent(LogIn_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(LogIn_Activity.this, "We Can Not Reach To Our DataBase", Toast.LENGTH_SHORT).show();
        }

    }
}