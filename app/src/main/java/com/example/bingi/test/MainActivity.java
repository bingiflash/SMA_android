package com.example.bingi.test;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity
{
    Button register_login, find_posts,finduser_posts_button;
    ListView most_like_posts;
    EditText find_posts_word,finduser_posts_word;
    RequestQueue re_que;

    String url = "http://10.0.2.2/test/Rest.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        re_que = Volley.newRequestQueue(getApplicationContext());

        register_login = findViewById(R.id.button_login_register);
        find_posts = findViewById(R.id.findposts_button);
        most_like_posts = findViewById(R.id.mostlikepost_list_view);
        find_posts_word = findViewById(R.id.findposts_word);
        finduser_posts_word = findViewById(R.id.finduserposts_word);
        finduser_posts_button = findViewById(R.id.finduserposts_button);

        find_posts_with_most_likes();

        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
            }
        });

        find_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(find_posts_word.getText().toString().matches(""))
                {
                    find_posts_word.setError("Please enter a word");
                    return;
                }
                Intent i = new Intent(MainActivity.this, output.class);
                i.putExtra("job","wordposts");
                i.putExtra("word",find_posts_word.getText().toString());
                startActivity(i);
                find_posts_word.setText("");
            }
        });

        finduser_posts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finduser_posts_word.getText().toString().matches(""))
                {
                    finduser_posts_word.setError("Please enter a username");
                    return;
                }
                Intent i = new Intent(MainActivity.this, output.class);
                i.putExtra("job","userposts");
                i.putExtra("username",finduser_posts_word.getText().toString());
                startActivity(i);
                finduser_posts_word.setText("");
            }
        });
    }

    void find_posts_with_most_likes()
    {
        JsonArrayRequest j_o_r = new JsonArrayRequest(
                Request.Method.GET,
                url+"work=mostlikes",
                null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        JSONArray value;
                        String middle_array[][] = new String[response.length()][2];
                        try
                        {
                            for(int i=0;i<response.length();i++)
                            {
                                value = response.getJSONArray(i);
                                middle_array[i][0] = value.getString(0);
                                middle_array[i][1] = value.getString(1);
                            }
                            ListAdapter la = new customAdapter(getApplicationContext(),middle_array);
                            most_like_posts.setAdapter(la);
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
