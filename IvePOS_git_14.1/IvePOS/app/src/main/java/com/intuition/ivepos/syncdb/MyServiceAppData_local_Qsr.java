package com.intuition.ivepos.syncdb;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.intuition.ivepos.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static com.intuition.ivepos.SplashScreenActivity.getDefaultSharedPreferencesMultiProcess;

public class MyServiceAppData_local_Qsr extends Service {

    RequestQueue queue;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static com.intuition.ivepos.syncdb.MyServiceAppData_local_Qsr.OnProgressUpdateListener progressListener;
    public static void setOnProgressChangedListener(com.intuition.ivepos.syncdb.MyServiceAppData_local_Qsr.OnProgressUpdateListener _listener) {
        progressListener = _listener;
    }
    public SQLiteDatabase db = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = "appdata";
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("initializing")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(1, notification);



        Runnable r = new Runnable() {
            public void run() {
                dump();
            }
        };

        Thread t = new Thread(r);
        t.start();
        return Service.START_STICKY;
    }



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public interface OnProgressUpdateListener {

        void onProgressUpdate(int progress);
    }


    private void dump() {

        int myDays = 60;

//        final Calendar c = Calendar.getInstance();
//        c.add(Calendar.DATE, (myDays * -1));  // number of days to subtract
//        int newDate =     (c.get(Calendar.YEAR) * 10000) +
//                ((c.get(Calendar.MONTH) + 1) * 100) +
//                (c.get(Calendar.DAY_OF_MONTH));
//        String newDate1 = String.valueOf(newDate)+"0000";
//        System.out.println("date is "+newDate1);

        SharedPreferences sharedpreferences=getDefaultSharedPreferencesMultiProcess(getApplicationContext());
        final String company= sharedpreferences.getString("companyname", null);
        final String store= sharedpreferences.getString("storename", null);
        final String device= sharedpreferences.getString("devicename", null);

        JSONObject params = new JSONObject();

        try {
            params.put("device",device);
            params.put("store",store);
            params.put("company",company);
            // params.put("date1",newDate1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  if(queue==null){
        queue = Volley.newRequestQueue(MyServiceAppData_local_Qsr.this);
        // }

        JsonObjectRequest sr = new JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                "https://theandroidpos.com/testapi/dump5.php",params,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject responseString) {

                        Log.e("MyServiceAppData_local_Qsr",getString(R.string.Button10));
                        String response= "";
                        JSONObject jsonObject=null;
                        try {
                            jsonObject=responseString;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            response = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(response.equalsIgnoreCase("success")){

                            Log.e("file name","file_"+company+"_"+store+"_"+device+"_mydbappdata.sql");
                            new DownloadOperation().execute("file_"+company+"_"+store+"_"+device+"_mydbappdata.sql");
                            //downloadFile("file_"+company+"_"+store+"_"+device+"_mydbappdata.sql");
                            // downloadFile("file_"+company+"_"+store+"_"+device+"_mydbappdata.sql",getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));

                        }else{
                            Toast.makeText(MyServiceAppData_local_Qsr.this, "download failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup confirm", "Error: " + error.getMessage());
                    }
                })  {

        };
/*    sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);

    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void downloadFile(String filename) {
//        String DownloadUrl = "https://theandroidpos.com:8085/dumps/"+filename;
//        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
//        request1.setDescription("Database File");   //appears the same in Notification bar while downloading
//        request1.setTitle(filename);
//        request1.setVisibleInDownloadsUi(false);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            request1.allowScanningByMediaScanner();
//            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
//        }
//        request1.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, filename);
//
//        DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        Objects.requireNonNull(manager1).enqueue(request1);
//
//        boolean flag = true;
//        boolean downloading =true;
//
//        DownloadManager.Query query = null;
//        query = new DownloadManager.Query();
//        Cursor c = null;
//        if(query!=null) {
//            query.setFilterByStatus(DownloadManager.STATUS_FAILED|DownloadManager.STATUS_PAUSED|DownloadManager.STATUS_SUCCESSFUL|DownloadManager.STATUS_RUNNING|DownloadManager.STATUS_PENDING);
//        } else {
//
//        }
//
//        while (downloading) {
//            c = manager1.query(query);
//            if(c.moveToFirst()) {
//                Log.i ("FLAG","Downloading");
//                int status =c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
//
//                if (status==DownloadManager.STATUS_SUCCESSFUL) {
//                    Log.i ("FLAG","done");
//                    downloading = false;
//                    flag=true;
//                    try {
//                        insertFromFile(filename);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(MyServiceAppData_local_Qsr.this,"initializing",Toast.LENGTH_LONG).show();
//                    break;
//                }
//                if (status==DownloadManager.STATUS_FAILED) {
//                    Log.i ("FLAG","Fail");
//                    downloading = false;
//                    flag=false;
//                    Toast.makeText(MyServiceAppData_local_Qsr.this,"failed",Toast.LENGTH_LONG).show();
//                    break;
//                }
//            }
//        }
//
//
//
//    }


    private  void downloadFile(String filename, File outputFile) {
        String DownloadUrl = "https://theandroidpos.com/dumps/"+filename;
        try {
            URL u = new URL(DownloadUrl);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();

            Log.e("FLAG app data","done");
            // Toast.makeText(MyServiceAppData_local_Qsr.this,"initializing",Toast.LENGTH_LONG).show();
            try {
                insertFromFile(filename);
            } catch (IOException e) {

                Log.e("Exception",e.toString());
                e.printStackTrace();
            }



        } catch(FileNotFoundException e) {
            Log.e("Exception",e.toString());
            e.printStackTrace();
            return; // swallow a 404
        } catch (IOException e) {
            Log.e("Exception",e.toString());
            e.printStackTrace();
            return; // swallow a 404
        }
    }



    private String download(String filename,File outputFile,DownloadOperation downloadOperation){
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://theandroidpos.com/dumps/"+filename);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(outputFile);

            byte data[] = new byte[fileLength];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {

                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known

                    //
                    downloadOperation.doProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            Log.e("exception appdata",e.toString());
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }

        try {
            insertFromFile(filename);
        } catch (IOException e) {

            Log.e("Exception",e.toString());
            e.printStackTrace();
        }

        return null;
    }

//    public int insertFromFile(String filename) throws IOException {
//
//        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
//        // Reseting Counter
//        int result = 0;
//
//        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+filename;
//        Log.e("path",path);
//        File file = new File(path);
//        FileInputStream insertsStream = new FileInputStream(file);
//        // InputStream insertsStream = context.getResources().openRawResource(resourceId);
//        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
//
//        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
//        while (insertReader.ready()) {
//            String insertStmt = insertReader.readLine();
//            try {
//                if(insertStmt!=null){
//                    db.execSQL(insertStmt);
//                    Log.e("insertStmt",insertStmt);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e("stack trace",e.toString());
//            }
//            result++;
//        }
//        insertReader.close();
//        stopSelf();
//        publishResults();
//
//        // returning number of inserted rows
//        return result;
//    }

    public int insertFromFile(String filename) throws IOException {

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE, null);
        // Reseting Counter
        int result = 0;

        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+filename;
        Log.e("path",path);
        File file = new File(path);
        FileInputStream insertsStream = new FileInputStream(file);
        // InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            try {
                if(insertStmt!=null){
                    db.execSQL(insertStmt);
                    Log.e("insertStmt",insertStmt);

                    if(insertStmt.contains("INSERT INTO `items`")){

                        String[] arrOfStr = insertStmt.split("VALUES", 0);

                        // for (String a : arrOfStr) {
                        String[] arrOfStr1 = arrOfStr[1].split(",\\(", 0);
                        for (String a1 : arrOfStr1) {
                            String[] arrOfStr2 = a1.split(",", 0);
                            System.out.println("image "+arrOfStr2[6]);
                            String encodedImage=arrOfStr2[6].substring(1) ;
                            System.out.println("image "+encodedImage);
                            encodedImage= removeLastChar(encodedImage);
                            System.out.println("image "+encodedImage);
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                            if(decodedByte==null){

                                ContentValues contentValues5 = new ContentValues();
                                contentValues5.put("image", "");
                                String where = "_id = '" + arrOfStr2[0].replaceAll("\\(","") + "'";
                                db.update("Items", contentValues5, where, new String[]{});

                            }else{
//                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                                byte[]  img = bos.toByteArray();

                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[]  img = bos.toByteArray();

                                ContentValues contentValues5 = new ContentValues();
                                contentValues5.put("image", img);
                                String where = "_id = '" + arrOfStr2[0].replaceAll("\\(","") + "'";
                                db.update("Items", contentValues5, where, new String[]{});
                            }

                        }

                    }else {
                        if(insertStmt.contains("INSERT INTO `logo`")){

                            String[] arrOfStr = insertStmt.split("VALUES", 0);

                            // for (String a : arrOfStr) {
                            String[] arrOfStr1 = arrOfStr[1].split(",\\(", 0);
                            for (String a1 : arrOfStr1) {
                                String[] arrOfStr2 = a1.split(",", 0);
                                System.out.println("companylogo "+arrOfStr2[1]);
                                String encodedImage=arrOfStr2[1].substring(1) ;
                                System.out.println("companylogo "+encodedImage);
                                encodedImage= removeLastChar(encodedImage);
                                System.out.println("companylogo "+encodedImage);
                                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


                                if(decodedByte==null){

                                    ContentValues contentValues5 = new ContentValues();
                                    contentValues5.put("companylogo", "");
                                    String where = "_id = '" + arrOfStr2[0].replaceAll("\\(","") + "'";
                                    db.update("Logo", contentValues5, where, new String[]{});

                                }else{
//                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
//                                byte[]  img = bos.toByteArray();

                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    decodedByte.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                    byte[]  img = bos.toByteArray();

                                    ContentValues contentValues5 = new ContentValues();
                                    contentValues5.put("companylogo", img);
                                    String where = "_id = '" + arrOfStr2[0].replaceAll("\\(","") + "'";
                                    db.update("Logo", contentValues5, where, new String[]{});
                                }

                            }

                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("stack trace",e.toString());
            }
            result++;
        }
        insertReader.close();
        stopSelf();
        publishResults();

        // returning number of inserted rows
        return result;
    }

    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0)
                ? null
                : (s.substring(0, s.length() - 1));
    }

    private void publishResults() {
        SharedPreferences sharedpreferences = getSharedPreferences("downloadpref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("download", "complete");
        editor.commit();
        Intent intent = new Intent("com.intuition.ivepos.GlobalPreferenceActivity_Qsr.receiverapp");
        sendBroadcast(intent);


    }


    private final class DownloadOperation extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            // downloadFile(params[0],getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
            download(params[0],new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+params[0]),DownloadOperation.this);
            return "Executed";
        }

        public void doProgress(int value){
            publishProgress(value);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressListener.onProgressUpdate(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

}
