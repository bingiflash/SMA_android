package com.example.bingi.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class Login extends AppCompatActivity {

    Button go_button;
    TextView screen_action;
    EditText username,password,re_password,email,location;
    Switch register_login_switch;
    RequestQueue re_que;

    String url = "http://10.0.2.2/test/Rest.php?";
    String login_result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        re_que = Volley.newRequestQueue(getApplicationContext());

        go_button = findViewById(R.id.go_button);
        screen_action = findViewById(R.id.screen_action);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        re_password = findViewById(R.id.re_enter_password);
        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        register_login_switch = findViewById(R.id.register_login_switch);

        register_login_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    screen_action.setText(getString(R.string.login));
                    re_password.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    location.setVisibility(View.GONE);
                    go_button.setText(getString(R.string.login));
                }
                else
                {
                    screen_action.setText(getString(R.string.register));
                    re_password.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    location.setVisibility(View.VISIBLE);
                    go_button.setText(getString(R.string.register));
                }
            }
        });

        go_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(username.getText().toString().matches(""))
                {
                    username.setError("please enter an username");
                    return;
                }
                if(password.getText().toString().matches(""))
                {
                    password.setError("Please enter a password");
                    return;
                }
                if(register_login_switch.isChecked())
                {
                    login();
                }
                else
                {
                    if(!password.getText().toString().equals(re_password.getText().toString()))
                    {
                        password.setError("Passwords do not match");
                        re_password.setError("Passwords do not match");
                        return ;
                    }
                    if(email.getText().toString().matches(""))
                    {
                        email.setError("Please enter an email");
                        return;
                    }
                    if(location.getText().toString().matches(""))
                    {
                        location.setError("Please enter a location");
                        return;
                    }
                    register();
                }
            }
        });
    }

    void login()
    {
        JsonArrayRequest j_o_r = new JsonArrayRequest(Request.Method.GET,
                url+"work=login&Loginname="+username.getText().toString()+"&Password="+password.getText().toString(),
                null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            if(response.getString(0).matches("success")) {
                                Intent i = new Intent(Login.this, Home.class);
                                i.putExtra("uid",response.getInt(1));
                                i.putExtra("username",response.getString(2));
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_SHORT).show();
                    }
                });
        re_que.add(j_o_r);
    }

    void register()
    {
        JsonArrayRequest j_o_r = new JsonArrayRequest(Request.Method.GET,
                url+"work=register&Loginname="+username.getText().toString()+"&Password="+password.getText().toString()+"&email="+email.getText().toString()+"&location="+location.getText().toString(),
                null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(response.getString(0).matches("success"))
                            {
                                Toast.makeText(getApplicationContext(), "user added",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_SHORT).show();
                    }
                });
        re_que.add(j_o_r);
    }
}
