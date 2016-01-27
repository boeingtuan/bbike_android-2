package iamhuy.bbike;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.ProcessButton;
import com.dd.processbutton.iml.ActionProcessButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private AutoCompleteTextView mEmailSignUpView;
    private EditText mPasswordSignUpView;
    private EditText mPasswordSignUpCheckView;
    private EditText mUsernameSignUpView;

    private static final String url_signup_mail = "http://bbike.xyz/api/v1/register/mail";
    JSONObject jsonSignup;

    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailSignUpView = (AutoCompleteTextView) findViewById(R.id.email_signup);
        mPasswordSignUpView = (EditText) findViewById(R.id.password_signup);
        mPasswordSignUpCheckView = (EditText) findViewById(R.id.repassword_signup);
        mUsernameSignUpView = (EditText) findViewById(R.id.account_name_signup);

        ActionProcessButton SignUpButton = (ActionProcessButton) findViewById(R.id.signup_button_signup);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpProcess();
            }
        });
    }

    private void SignUpProcess() {
        String email = mEmailSignUpView.getText().toString();
        String password = mPasswordSignUpView.getText().toString();
        String repassword = mPasswordSignUpCheckView.getText().toString();
        String username = mUsernameSignUpView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordSignUpView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordSignUpView;
            cancel = true;
        }

        if (!password.equals(repassword)) {
            mPasswordSignUpView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordSignUpView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailSignUpView.setError(getString(R.string.error_field_required));
            focusView = mEmailSignUpView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailSignUpView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailSignUpView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error
            focusView.requestFocus();
        } else {
            //TODO : kiểm tra đăng thông tin id, password
            new Register(email, password, username).execute();
        }
    }

    class Register extends AsyncTask<String, String, String> {

        private String email;
        private String password;
        private String username;

        Register(String email, String password, String username) {
            this.email = email;
            this.password = password;
            this.username = username;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... agrs) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("username", username));

            // Getting JSON object - Post method
            jsonSignup = jsonParser.makeHttpRequest(url_signup_mail, "POST", params);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (jsonSignup != null && !jsonSignup.optBoolean("error")) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("jsonSignup", jsonSignup.toString());
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
