package com.intuition.ivepos;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class MainActivity_Assets extends Activity {
    private final static String TAG = "MainActivity";
    SqlLiteDbHelper dbHelper= null;

    Button button;
    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_assets);

        //app permission
        if (ContextCompat.checkSelfPermission(MainActivity_Assets.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity_Assets.this,
                    permissions(),
                    9);
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity_Assets.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity_Assets.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        9);
            } else {
                ActivityCompat.requestPermissions(MainActivity_Assets.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        9);
            }*/
        }else {
//                    copyDataBase();

        }

        button = (Button) findViewById(R.id.one);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyAsset("mydb_Appdata");
            }
        });
//        dbHelper = new SqlLiteDbHelper(this, getFilesDir().getAbsolutePath());
//        try {
//            dbHelper.prepareDatabase();
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage());
//        }
//        showData();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//    private void showData() {
//        List<Contact> list = dbHelper.getEmployees();
//        StringBuffer data = new StringBuffer();
//        for (int i =0; i< list.size(); i++) {
//            Contact emp = list.get(i);
//            data.append(emp.getName())
//                    .append(",").append(emp.getAge()).append("<br/>");
//        }
//        TextView textView = (TextView)findViewById(R.id.bodytext);
//        textView.setText(Html.fromHtml(data.toString()));
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 9: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity_Assets.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //do nothing
                    }

                } else {
                    Toast.makeText(MainActivity_Assets.this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private void copyAsset(String filename) {
//        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/IVEPOS_backup";
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/IVEPOS_backup";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            File outFile = new File(dirPath, filename);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            Toast.makeText(this, "saved", Toast.LENGTH_LONG).show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    try {
//                        File sd = Environment.getExternalStorageDirectory();
                        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        File data = Environment.getDataDirectory();

                        if (sd.canWrite()) {
                            String currentDBPath = "//data//" + "com.intuition.ivepos"
                                    + "//databases//" + "mydb_Appdata";
                            String backupDBPath = "/IVEPOS_backup/" + "mydb_Appdata";




                            File file = new File("/data/data/com.intuition.ivepos/databases/mydb_Appdata");
                            if(file.exists()){
//                                                    Toast.makeText(getActivity(), "exists", Toast.LENGTH_LONG)
//                                                            .show();
                            }
//                            if (DATA_DIRECTORY_DATABASE.exists()){
////                                                    Toast.makeText(getActivity(), "existsssss", Toast.LENGTH_LONG)
////                                                            .show();
//                            }else {
////                                                    Toast.makeText(getActivity(), "not exists", Toast.LENGTH_LONG)
////                                                            .show();
//                            }

                            File backupDB = new File(data, currentDBPath);
                            File currentDB = new File(sd, backupDBPath);

                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();

                        }
                    } catch (Exception e) {

                        Toast.makeText(MainActivity_Assets.this, e.toString(), Toast.LENGTH_LONG)
                                .show();

                    }

                }
            }, 5000);

        }catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
        } finally {
            if (in != null) {
                try {
                    in.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}
