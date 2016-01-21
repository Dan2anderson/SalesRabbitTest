package com.anderson.daniel.salesrabbittest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FriendsListAdapter friendsListAdapter;
    ListView listView;
    ArrayList<Friend> friends;
    TextView firstName;
    TextView lastName;
    TextView phoneNumber;
    TextView biography;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        listView = (ListView) findViewById(R.id.friendlistview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = (Friend) listView.getItemAtPosition(position);
                listView.setVisibility(View.INVISIBLE);
                findViewById(R.id.detailsView).setVisibility(View.VISIBLE);
                new GetDetails().execute();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        new GetRequest().execute();
    }
    public void backToFriendsList(View view){
        findViewById(R.id.detailsView).setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }


    public void setUpAdapter(){
        listView.setAdapter(new FriendsListAdapter(this,friends));
        friendsListAdapter = (FriendsListAdapter) listView.getAdapter();
    }


    private class GetRequest extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return requestContent("http://private-5bdb3-friendmock.apiary-mock.com/friends");
        }

        @Override
        protected void onPostExecute(String jsonStr) {

            parseJSON(jsonStr);
            setUpAdapter();
        }
    }
    private class GetDetails extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return requestContent("http://private-5bdb3-friendmock.apiary-mock.com/friends/id");
        }

        @Override
        protected void onPostExecute(String jsonStr) {

            TextView firstName = (TextView)findViewById(R.id.first_name);
            TextView lastName = (TextView)findViewById(R.id.last_name);
            TextView phoneNumber= (TextView)findViewById(R.id.phone);
            TextView biography = (TextView)findViewById(R.id.bio);
            DetailsModel details = parseDetailsJSON(jsonStr);
            lastName.setText(details.getLast_name());
            firstName.setText(details.getFirst_name());
            phoneNumber.setText(details.getPhone());
            biography.setText(details.getBio());


        }
    }




    public String requestContent(String url) {
        String result = null;
        try {
            URL tempUrl = new URL(url);
            HttpURLConnection httpConection = (HttpURLConnection) tempUrl.openConnection();
            try{
                InputStream input = new BufferedInputStream(httpConection.getInputStream());
                result = readStream(input);
            }finally{
                httpConection.disconnect();
            }

        }catch(MalformedURLException e){
            Log.e("mainActivity",e.getMessage(),e);
        }catch(IOException e){
            Log.e("mainActivity",e.getMessage(),e);
        }
        return result;
    }
    private String readStream(InputStream stream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try{
            while ((line = reader.readLine()) != null){
                sb.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void parseJSON(String jsonStr){
         friends = new ArrayList<Friend>();

        try {
            JSONArray items = new JSONArray(jsonStr);

            for (int i = 0; i < items.length(); i++) {
                JSONObject friendObject = items.getJSONObject(i);
                Friend friend= new Friend(friendObject.getString("first_name")+" "+ friendObject.getString("last_name"),
                        friendObject.getString("img"),
                        friendObject.getString("status"),
                        friendObject.getBoolean("available"));
                friends.add(friend);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public DetailsModel parseDetailsJSON(String jsonStr){
        try {
            JSONObject json = new JSONObject(jsonStr);
            DetailsModel details = new DetailsModel(json.getString("first_name"),json.getString("last_name"),
                                                    json.getString("phone"),json.getString("bio"));
            return details;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
