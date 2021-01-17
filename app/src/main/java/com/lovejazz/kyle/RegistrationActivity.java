package com.lovejazz.kyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Deleting toolbar
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            Log.d("RegistrationActivity", "Toolbar produced null pointer exception");
        }
        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    //Registration button is clicked
    public void onRegistrationButtonClicked(View view) {
        //Remembering all registration fields
        final EditText loginEntry = findViewById(R.id.login_entry);
        final EditText emailEntry = findViewById(R.id.email_entry);
        EditText passwordEntry = findViewById(R.id.password_entry);
        EditText repeatPasswordEntry = findViewById(R.id.repeat_password_entry);
        fstore = FirebaseFirestore.getInstance();
        //Checking if empty fields exist
        if (loginEntry.getText().toString().equals("") || emailEntry.getText().toString().
                equals("") || passwordEntry.getText().toString().equals("") || repeatPasswordEntry
                .getText().toString().equals("")) {
            Snackbar
                    .make(
                            findViewById(R.id.activity_registration),
                            getString(R.string.error_empty_fields),
                            Snackbar.LENGTH_LONG
                    )
                    .setTextColor(getResources().getColor(R.color.full_white))
                    .setBackgroundTint(getResources().getColor(R.color.red))
                    .show();
            //Checking if password equals repeated password
        } else if (!passwordEntry.getText().toString().equals(repeatPasswordEntry.getText().
                toString())) {
            Snackbar
                    .make(
                            findViewById(R.id.activity_registration),
                            getString(R.string.error_passwords_dont_equal),
                            Snackbar.LENGTH_LONG
                    )
                    .setTextColor(getResources().getColor(R.color.full_white))
                    .setBackgroundTint(getResources().getColor(R.color.red))
                    .show();
            //Checking if login length is okay
        } else if (loginEntry.getText().toString().length() > 12 || loginEntry.getText().toString()
                .length() < 5) {
            Snackbar
                    .make(
                            findViewById(R.id.activity_registration),
                            getString(R.string.error_login_length),
                            Snackbar.LENGTH_LONG
                    )
                    .setTextColor(getResources().getColor(R.color.full_white))
                    .setBackgroundTint(getResources().getColor(R.color.red))
                    .show();
            //Checking if password length is okay
        } else if (passwordEntry.getText().toString().length() > 18 || passwordEntry.getText().
                toString().length() < 6) {
            Snackbar
                    .make(
                            findViewById(R.id.activity_registration),
                            getString(R.string.error_password_length),
                            Snackbar.LENGTH_LONG
                    )
                    .setTextColor(getResources().getColor(R.color.full_white))
                    .setBackgroundTint(getResources().getColor(R.color.red))
                    .show();
            //Checking if email length is okay
        } else if (emailEntry.getText().toString().length() > 254 || emailEntry.getText().
                toString().length() < 3) {
            Snackbar
                    .make(
                            findViewById(R.id.activity_registration),
                            getString(R.string.error_password_length),
                            Snackbar.LENGTH_LONG
                    )
                    .setTextColor(getResources().getColor(R.color.full_white))
                    .setBackgroundTint(getResources().getColor(R.color.red))
                    .show();
            //Checking if text in fields are valid
        } else if (!isValid(emailEntry.getText().toString())) {
            Snackbar
                    .make(
                            findViewById(R.id.activity_registration),
                            getString(R.string.error_contains_not_valid_symbols),
                            Snackbar.LENGTH_LONG
                    )
                    .setTextColor(getResources().getColor(R.color.full_white))
                    .setBackgroundTint(getResources().getColor(R.color.red))
                    .show();
        } else {
            mAuth.createUserWithEmailAndPassword(emailEntry.getText().toString(), passwordEntry
                    .getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    DocumentReference documentReference = fstore.collection("users")
                            .document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("nickname", loginEntry.getText().toString());
                    user.put("email", emailEntry.getText().toString());
                    user.put("id", userID);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Registration", "Document is created");
                            Intent intent = new Intent(RegistrationActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar
                                            .make(
                                                    findViewById(R.id.activity_registration),
                                                    getString(R.string.some_problem_occurred),
                                                    Snackbar.LENGTH_LONG
                                            )
                                            .setTextColor(getResources().getColor(R.color.white))
                                            .setBackgroundTint(getResources().getColor(R.color.red))
                                            .show();
                                }
                            });
                }
            });
        }
    }

    //Arrow button is clicked
    public void onArrowButtonClicked(View view) {
        Intent backIntent = new Intent(RegistrationActivity.this, MainRegisterActivity.class);
        startActivity(backIntent);
    }

    //login in existed account text clicked
    public void onLoginTextClicked(View view) {
        Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    //Checking if user`s entry text is valid
    private static boolean isValid(String text) {
        String textRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(textRegex);
        if (text == null)
            return false;
        return pat.matcher(text).matches();
    }

}
