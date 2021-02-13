package com.geektech.noteapp.ui.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.geektech.noteapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneFragment extends Fragment {

    private EditText editPhone, editVerifCode;
    private Button buttonPhone, buttonGetIn;
    private TextView textViewCountDownTimer, textViewCheckNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editPhone = view.findViewById(R.id.edit_phone);
        textViewCheckNumber = view.findViewById(R.id.text_view_check_number);
        textViewCountDownTimer = view.findViewById(R.id.text_view_count_down_timer);
        editVerifCode = view.findViewById(R.id.edit_verif_code);
        buttonPhone = view.findViewById(R.id.button_next);
        buttonGetIn = view.findViewById(R.id.button_get_in);

        buttonGetIn.setVisibility(View.INVISIBLE);
        editVerifCode.setVisibility(View.INVISIBLE);

        buttonGetIn.setOnClickListener(v -> confirm());
        buttonPhone.setOnClickListener(v -> requestSMS());

        setCallBacks();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish();
                    }
                });
    }

    private void confirm() {
        String code = editVerifCode.getText().toString();
        if (code.length() == 6 && TextUtils.isDigitsOnly(code)) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
            signIn(credential);
        }
    }

    private void setCallBacks() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                editPhone.setVisibility(View.VISIBLE);
                editVerifCode.setVisibility(View.GONE);
            }

       //         TODO: 6th Home Work - window to enter code from requestSMS & count down timer (also on 51 to 57 ln.)
            @SuppressLint("SetTextI18n")
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationID = s;
                editPhone.setVisibility(View.GONE);
                editVerifCode.setVisibility(View.VISIBLE);
                buttonPhone.setVisibility(View.GONE);
                buttonGetIn.setVisibility(View.VISIBLE);
                new CountDownTimer(60000, 1000) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        textViewCountDownTimer.setText("The code will be sent within: " + (millisUntilFinished / 1000) + " s.");
                    }

                    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
                    @Override
                    public void onFinish() {
                        editPhone.setVisibility(View.VISIBLE);
                        editVerifCode.setVisibility(View.GONE);
                        textViewCountDownTimer.setVisibility(View.GONE);
                        textViewCheckNumber.setVisibility(View.VISIBLE);
                        textViewCheckNumber.setText("CHECK PHONE NUMBER");
                        textViewCheckNumber.setTextColor(R.color.red);
                        buttonPhone.setVisibility(View.VISIBLE);
                        buttonGetIn.setVisibility(View.GONE);
                    }
                }.start();
            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    close();
                } else {
                    Toast.makeText(requireContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestSMS() {
        String phone = editPhone.getText().toString().trim();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(callbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
    }
}