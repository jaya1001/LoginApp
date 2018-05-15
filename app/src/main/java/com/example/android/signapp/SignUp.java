package com.example.android.signapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

/**
 * Created by User on 3/8/2018.
 */

public class SignUp extends Fragment {

        private static final String TAG = "";
        private EditText mUsername;
        private EditText mEmail;
        private FirebaseAuth mAuth;
        private View view;
        private EditText mConfirmEmail;

        public SignUp(){

        }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sign_up, container, false);
        /* setContentView(R.layout.activity_main); */
        mAuth = getInstance();

        mUsername = (EditText)view.findViewById(R.id.username);
        mEmail = (EditText)view.findViewById(R.id.email);
        mConfirmEmail = (EditText)view.findViewById(R.id.confirm_email);
        Button b1 = (Button)view.findViewById(R.id.button1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(mConfirmEmail.getText().toString().compareTo(mEmail.getText().toString()) == 0)
                {
                final Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mUsername.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "SignUp successful", Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = task.getResult().getUser();
                                    if (user != null) {
                                        Toast.makeText(getActivity(), "user is not null", Toast.LENGTH_SHORT).show();
                                        sendVerificationEmail();
                                    } else {
                                        Toast.makeText(getActivity(), "user is null", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                else {

                                    Toast.makeText(getActivity(), "SignUp Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
                else
                {
                    Toast.makeText(getActivity(), "confirm the correct password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void sendVerificationEmail() {
        // Disable button
        view.findViewById(R.id.button1).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user;
        user = getInstance().getCurrentUser();
        assert user != null;

        Toast.makeText(getActivity(),
                "send email " + user.getEmail(),
                Toast.LENGTH_SHORT).show();

        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        view.findViewById(R.id.email).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getActivity(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
