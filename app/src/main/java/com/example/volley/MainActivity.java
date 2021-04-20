package com.example.volley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<Data> dataArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView=findViewById(R.id.listView);

        String url="https://picsum.photos/v2/list";


        ProgressDialog dialog= new ProgressDialog(this);
        dialog.setMessage("Wait......");
        dialog.setCancelable(true);
        dialog.show();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i= new Intent(MainActivity.this, RetroFitEx.class);
//                startActivity(i);
//            }
//        });


        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
          if (response!=null){
        dialog.dismiss();
          try{
               JSONArray json=new JSONArray(response);
               parseArray(json);

             }catch (JSONException e){
             e.printStackTrace();
}

}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }

    });

        RequestQueue queue= Volley.newRequestQueue(this);

        queue.add(request);

    }

    private void parseArray(JSONArray json) {

        for (int i=0; i<json.length();i++){
            try{
                JSONObject ob=json.getJSONObject(i);
                Data d=new Data();
                d.setName(ob.getString("author"));

                d.setImage(ob.getString("download_url"));

                dataArrayList.add(d);

            }catch(JSONException e){
                e.printStackTrace();


            }
            listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return dataArrayList.size();
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

                    View v=getLayoutInflater().inflate(R.layout.list_item,null);
                    Data data=dataArrayList.get(position);

                    ImageView img=v.findViewById(R.id.imageView);
                    TextView text=v.findViewById(R.id.textView);

                    Glide.with(getApplicationContext()).load(data.getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);

                    text.setText(data.getName());


                    return v;
                }
            });
        }
    }


}
