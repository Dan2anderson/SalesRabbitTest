package com.anderson.daniel.salesrabbittest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Daniel on 1/20/2016.
 */
public class FriendsListAdapter extends BaseAdapter {
    private ArrayList<Friend> friendsList;
    private ArrayList<Bitmap> pictureList;
    private LayoutInflater layoutInflater;

    public FriendsListAdapter(Context context,ArrayList<Friend> listOfFriends){
        friendsList = listOfFriends;
        layoutInflater = LayoutInflater.from(context);
        pictureList = new ArrayList<Bitmap>();

        //get pictures and create picture list
        for(int i=0; i<friendsList.size();i++) {
            String urlStr = friendsList.get(i).getImgUrlStr();
            new GetPictureAsync().execute(urlStr,Integer.toString(i));
        }

    }


    @Override
    public int getCount() {
        return friendsList.size();

    }

    @Override
    public Object getItem(int position) {
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder tmpViewHolder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.friend_row,null);
            tmpViewHolder = new ViewHolder();
            tmpViewHolder.name= (TextView)convertView.findViewById(R.id.namelable);
            tmpViewHolder.status = (TextView)convertView.findViewById(R.id.statuslable);
            tmpViewHolder.picture = (ImageView)convertView.findViewById(R.id.pictureview);
            tmpViewHolder.statusPic = (ImageView) convertView.findViewById(R.id.statuspic);
            convertView.setTag(tmpViewHolder);
        }else{
            tmpViewHolder = (ViewHolder)convertView.getTag();
        }

        tmpViewHolder.name.setText(friendsList.get(position).getName());
        tmpViewHolder.status.setText(friendsList.get(position).getStatus());

        if(pictureList.size()>position){
            tmpViewHolder.picture.setImageBitmap(pictureList.get(position));
        }

        //set the status image
        String picFileName="busy";
        if(friendsList.get(position).isAvailable()){
            picFileName="free";
        }
        int drawableID = layoutInflater.getContext().getResources().getIdentifier(picFileName,"drawable",layoutInflater.getContext().getPackageName());
        tmpViewHolder.statusPic.setImageResource(drawableID);


        return convertView;
    }
    static class ViewHolder{
        ImageView picture;
        ImageView statusPic;
        TextView name;
        TextView status;
    }

    private class GetPictureAsync extends AsyncTask<String,Void,Bitmap> {
        int currPosition;
        @Override
        protected Bitmap doInBackground(String... params) {
            currPosition = Integer.parseInt(params[1]);
            URL pictureURL= null;
            try {
                pictureURL = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap pic = null;
            try {
                pic = BitmapFactory.decodeStream(pictureURL.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pic;
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            pictureList.add(bmp);
            notifyDataSetChanged();

        }
    }

}
