package com.parse.starter;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.graphics.Bitmap;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final LinearLayout linearLayout=(LinearLayout) findViewById(R.id.linearLayout);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        Intent intent4 = getIntent();
        String activeUsername = intent4.getStringExtra("username");
        setTitle(activeUsername+ "'s Feed");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereNotEqualTo("username", activeUsername);
        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size() > 0){
                        for(ParseObject object : objects){
                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if(e==null && data !=null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                        imageView.setImageBitmap(bitmap);

                                        linearLayout.addView(imageView);
                                    }
                                    else{
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });


    }
}
