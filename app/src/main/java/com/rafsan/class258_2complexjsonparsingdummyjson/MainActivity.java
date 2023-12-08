package com.rafsan.class258_2complexjsonparsingdummyjson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    GridView gridView;
    ArrayList<HashMap< String,String > > arrayList = new ArrayList<>();
    HashMap < String,String > hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView);

        progressBar = findViewById(R.id.progressBar);
//        textView = findViewById(R.id.textView);

        String url = "https://dummyjson.com/carts";

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("serverRes", String.valueOf(response));

                        try {
                            JSONArray jsonArray = response.getJSONArray("carts");

                            for (int x = 0; x<3; x++){
                                JSONObject jsonObject = jsonArray.getJSONObject(x);

                                String id = jsonObject.getString("id");
//                                textView.append(x+" . "+id);

                                JSONArray productsArray = jsonObject.getJSONArray("products");
                                for (int y = 0; y<productsArray.length(); y++){
                                    JSONObject jsonObject1 = productsArray.getJSONObject(y);

                                    String title = jsonObject1.getString("title");
                                    String thumbnail = jsonObject1.getString("thumbnail");

                                    hashMap = new HashMap<>();
                                    hashMap.put("title",title);
                                    hashMap.put("thumbnail",thumbnail);
                                    arrayList.add(hashMap);
                                }
                            }

                            MyAdapter myAdapter = new MyAdapter();
                            gridView.setAdapter(myAdapter);

                        }
                        catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.VISIBLE);

                    }
                }
        );
        queue.add(jsonObjectRequest);

    }
    //============== Adapter====================
    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myView = layoutInflater.inflate(R.layout.items,parent,false);

            TextView textTitle = myView.findViewById(R.id.textTitle);
            ImageView thumbnail = myView.findViewById(R.id.thumbnail);

            HashMap< String,String > hashMap1 = arrayList.get(position);

            String stitle = hashMap1.get("title");
            String sthumbnail = hashMap1.get("thumbnail");

            textTitle.setText(stitle);

            Picasso.get()
                    .load(sthumbnail)
                    .placeholder(R.drawable.img)
                    .into(thumbnail);

            return myView;
        }
    }
}