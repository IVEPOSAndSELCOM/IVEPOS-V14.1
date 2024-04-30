package com.intuition.ivepos;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.intuition.ivepos.sync.StubProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Customer_Feedback extends AppCompatActivity {

    float mul1 = 0, mul2 = 0, mul3 = 0, mul4 = 0;
    Uri contentUri,resultUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_feedback);

        Bundle extras = getIntent().getExtras();
        final String name = extras.getString("name");
        final String phone = extras.getString("phone");

        TextView cust_name = (TextView) findViewById(R.id.cust_name);
        cust_name.setText(name);

        ImageView back_pressed = (ImageView) findViewById(R.id.back_pressed);
        back_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final RatingBar ambience_rating = (RatingBar) findViewById(R.id.ratingBar_Ambience);
        ambience_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromuser) {

            }
        });

        final RatingBar prod_qual_rating = (RatingBar) findViewById(R.id.ratingBar_prod_qual);
        prod_qual_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromuser) {

            }
        });

        final RatingBar service_rating = (RatingBar) findViewById(R.id.ratingBar_service);
        service_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromuser) {

            }
        });

        final RatingBar all_exp_rating = (RatingBar) findViewById(R.id.ratingBar_all_exp);
        all_exp_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromuser) {

            }
        });

        final EditText comments = (EditText) findViewById(R.id.comment);


        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy MM dd");
                final String currentDateandTime1 = sdf2.format(new Date());

                SimpleDateFormat normal = new SimpleDateFormat("dd MMM yyyy");
                final String normal1 = normal.format(new Date());

                Date dt = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");
                final String time1 = sdf1.format(dt);

                Date dtt = new Date();
                SimpleDateFormat sdf1t = new SimpleDateFormat("yyyyMMddkk:mm:ss");
                final String time24 = sdf1t.format(dtt);

                float am_ra = ambience_rating.getRating();
                float pq_ra = prod_qual_rating.getRating();
                float ser_ra = service_rating.getRating();
                float ove_exp_ra = all_exp_rating.getRating();

                if (am_ra > 0 || pq_ra > 0 || ser_ra > 0 || ove_exp_ra > 0){

                    if (am_ra > 0){
                        mul1 = ambience_rating.getRating()*5;
                    }
                    if (pq_ra > 0){
                        mul2 = prod_qual_rating.getRating()*5;
                    }
                    if ( ser_ra > 0){
                        mul3 = service_rating.getRating()*5;
                    }
                    if (ove_exp_ra > 0){
                        mul4 = all_exp_rating.getRating()*5;
                    }

                    float addmul = mul1+mul2+mul3+mul4;


                    ContentValues contentValues = new ContentValues();
                    contentValues.put("cust_name", name);
                    contentValues.put("cust_phoneno", phone);
                    contentValues.put("date", currentDateandTime1);
                    contentValues.put("time", time1);
                    contentValues.put("ambience_rating", am_ra);
                    contentValues.put("pro_qual_rating", pq_ra);
                    contentValues.put("service_rating", ser_ra);
                    contentValues.put("overall_exp_rating", ove_exp_ra);
                    contentValues.put("comments", comments.getText().toString());
                    contentValues.put("percentage", String.valueOf(addmul));


                    contentUri = Uri.withAppendedPath(StubProvider.CONTENT_URI, "Cust_feedback");
                    resultUri = getContentResolver().insert(contentUri, contentValues);
                    getContentResolver().notifyChange(resultUri, null);

                    //      db.insert("Cust_feedback", null, contentValues);

                    Toast.makeText(Customer_Feedback.this, "Feedback saved", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(Customer_Feedback.this, "cannot save", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
