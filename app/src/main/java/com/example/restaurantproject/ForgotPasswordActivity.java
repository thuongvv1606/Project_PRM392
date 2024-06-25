package com.example.restaurantproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantproject.repository.AccountRepository;
import com.example.restaurantproject.ultils.mailer.SendEmail;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button sendButton;
    private TextView tvLogin;

    private AccountRepository accountRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.edt_email);
        sendButton = findViewById(R.id.btn_send);
        tvLogin = findViewById(R.id.tv_remember_account_login);

        accountRepository = new AccountRepository(this);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString().trim();


                if (enteredEmail.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(enteredEmail)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean emailExists = accountRepository.checkEmailExists(enteredEmail);
                if (!emailExists) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email does not exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Đặt lại mật khẩu và lấy mật khẩu mới
                String newPassword = accountRepository.resetPassword(enteredEmail);
                if (newPassword == null) {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                    return;
                }

                String subject = "Your New Password for Restaurant Management System";
                String body = "Dear user,\n\n"
                        + "We have received a request to reset your password. Your new password is:\n\n"
                        + newPassword + "\n\n"
                        + "For security reasons, we recommend changing your password after logging in.\n\n"
                        + "Best regards,\n"
                        + "Restaurant Management System";

                SendEmail.sendEmail(enteredEmail, subject, body);
                Toast.makeText(ForgotPasswordActivity.this, "The new password has been sent to the email: " + enteredEmail + " successfully", Toast.LENGTH_SHORT).show();

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isValidEmail(String emailAddress) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

}