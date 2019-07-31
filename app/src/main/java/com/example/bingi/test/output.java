package com.example.bingi.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class output extends AppCompatActivity
{

    String url = "http://10.0.2.2/test/Rest.php?";
    String word,job,username;
    ListView posts_with_word_view;
    TextView message_text;
    RequestQueue re_que;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        re_que = Volley.newRequestQueue(getApplicationContext());

        posts_with_word_view = findViewById(R.id.output_posts_with_word_view);
        message_text = findViewById(R.id.meesages_text);

        Bundle data = getIntent().getExtras();
        if(data == null)
        {
            return;
        }
        job = data.getString("job");
        if(job.matches("wordposts"))
        {
            word = data.getString("word");
            find_posts_with_word();
        }
        else if (job.matches("userposts"))
        {
            username = data.getString("username");
            find_posts_with_user();
        }

    }

    void find_posts_with_word()
    {
        JsonArrayRequest j_o_r = new JsonArrayRequest(Request.Method.GET,
                url+"work=wordpost&word="+word,
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
                                String yes_post_text = "Posts that contains word '"+word+"'";
                                message_text.setText(yes_post_text);
                                for(int i=1;i<response.length();i++)
                                {
                                    value = response.getJSONArray(i);
                                    middle_array[i-1][0] = value.getString(0);
                                    middle_array[i-1][1] = value.getString(1);
                                }
                                ListAdapter la = new customAdapter(getApplicationContext(),middle_array);
                                posts_with_word_view.setAdapter(la);
                            }

                            else
                            {
                                String no_post_text = "There are no posts that contains word '"+word+"'";
                                message_text.setText(no_post_text);
                            }


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

    void find_posts_with_user()
    {
        JsonArrayRequest j_o_r = new JsonArrayRequest(Request.Method.GET,
                url+"work=userpost&username="+username,
                null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        JSONArray value;
                        String middle_array[] = new String[response.length()-1];
                        try
                        {
                            if(response.getString(0).equals("success"))
                            {
                                String yes_post_text = "Posts by '"+username+"'";
                                message_text.setText(yes_post_text);
                                for(int i=1;i<response.length();i++)
                                {
                                    middle_array[i-1] = response.getString(i);
                                }
                                ListAdapter la = new ArrayAdapter<String >(getApplicationContext(),android.R.layout.simple_list_item_1,middle_array);
                                posts_with_word_view.setAdapter(la);
                            }

                            else
                            {
                                String no_post_text = "There are no posts by '"+username+"'";
                                message_text.setText(no_post_text);
                            }


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
