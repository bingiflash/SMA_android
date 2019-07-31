package com.example.bingi.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class Home extends AppCompatActivity {

    Button post_button;
    EditText post_content;
    ListView list;
    RequestQueue re_que;
    public int uid;


    String username;
    String url = "http://10.0.2.2/test/Rest.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        re_que = Volley.newRequestQueue(getApplicationContext());

        post_button = findViewById(R.id.post_button);
        post_content = findViewById(R.id.post_content);
        list = findViewById(R.id.messages_list_view);

        Bundle data = getIntent().getExtras();
        if(data == null)
        {
            return;
        }
        uid = data.getInt("uid");
        username = data.getString("username");

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(post_content.getText().toString().matches(""))
                {
                    post_content.setError("Please enter some text");
                    return;
                }
                post();
                post_content.setText("");
            }
        });
        messages();
    }

    void post()
    {
        JsonArrayRequest j_o_r = new JsonArrayRequest(Request.Method.GET,
                url+"work=post&uid="+uid+"&post="+post_content.getText().toString(),
                null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            if(response.getString(0).matches("success"))
                            {
                                Toast.makeText(getApplicationContext(), "Post added Successfully",Toast.LENGTH_LONG).show();
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

    void messages()
    {
        JsonArrayRequest j_o_r = new JsonArrayRequest(Request.Method.GET,
                url+"work=messages&uid="+uid,
                null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        JSONArray value;
                        String middle_array[][] = new String[response.length()-1][2];
                        try
                        {
                            if(response.getString(0).equals("success"))
                            {
                                for(int i=1;i<response.length();i++)
                                {
                                    value = response.getJSONArray(i);
                                    middle_array[i-1][0] = value.getString(0);
                                    middle_array[i-1][1] = value.getString(1);
                                }
                                ListAdapter la = new customAdapter(getApplicationContext(),middle_array);
                                list.setAdapter(la);
                            }
                            else
                                Toast.makeText(getApplicationContext(),"There was an error",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e)
                        {
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
