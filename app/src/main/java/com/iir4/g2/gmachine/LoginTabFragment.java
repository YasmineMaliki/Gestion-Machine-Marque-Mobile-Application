package com.iir4.g2.gmachine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iir4.g2.gmachine.models.User;
import com.iir4.g2.gmachine.utils.SessionManagment;

import java.util.HashMap;
import java.util.Map;

public class LoginTabFragment extends Fragment {
    EditText username,password;
    Button login;
    private static final String urlLoginUser = "http://10.0.2.2:8090/login";
    SessionManagment sessionManagment;
    float v=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root =(ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
        sessionManagment = new SessionManagment(getContext());
        checkLogin();
        username = root.findViewById(R.id.usernamee);
        password = root.findViewById(R.id.passwordd);
        login = root.findViewById(R.id.connecter);

        username.setTranslationX(800);
        password.setTranslationX(800);
        login.setTranslationX(800);

        username.setAlpha(v);
        password.setAlpha(v);
        login.setAlpha(v);

        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

       /* login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity currentActivity = (Activity) getContext();
                Intent intent = new Intent(currentActivity,NavActivity.class);
                startActivity(intent);
            }
        });*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                login(usernameText, passwordText);

            }
        });

        return root;

    }
    private void checkLogin() {
        if (sessionManagment.getLogin()) {
            Activity currentActivity = (Activity) getContext();
            Intent i = new Intent(currentActivity, NavActivity.class);
            currentActivity.startActivity(i);
            getActivity().finish();
        }
    }

    private void login(String usernameText, String passwordText) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.start();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLoginUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        User user = new User(usernameText, passwordText);
                        sessionManagment.setLogin(true);
                        sessionManagment.setUsername(usernameText);
                        Activity currentActivity = (Activity) getContext();
                        Intent i = new Intent(currentActivity, NavActivity.class);
                        currentActivity.startActivity(i);
                        getActivity().finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Mot de pass ou username incorrect",
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", usernameText);
                params.put("password", passwordText);
                return params;
            }
        };
        {

        }
        requestQueue.add(stringRequest);
    }
}
