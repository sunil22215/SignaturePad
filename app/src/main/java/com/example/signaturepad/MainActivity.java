package com.example.signaturepad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private ListView listView;
    private Bitmap bitmap;
    private Button clear;
    private Button save;
    private ImageView imageView;
    SignatureView signatureView;
    private TextView textView;
    private int path;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        // setDataWithView();
        list = new ArrayList<String>();
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");
        list.add("Sunday");
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
       listView.setAdapter(adapter);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clearCanvas();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = signatureView.getSignatureBitmap();

               // SpannableStringBuilder builder = new SpannableStringBuilder();

               // imageView.setImageBitmap(bitmap);
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {
                    Toast.makeText(getApplicationContext(), "Could not get the text", Toast.LENGTH_LONG).show();

                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); ++i) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }
                    textView.setText(sb.toString());
                    searchView.onActionViewExpanded();
                    searchView.setQuery(sb.toString(),true);
                    searchView.clearFocus();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }


  /*  public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
*/
    /*  private String saveImage(Bitmap bitmap) {
          ByteArrayOutputStream bytes = new ByteArrayOutputStream();
          bitmap.compress(Bitmap.CompressFormat.JPEG,90,bytes);
          File wallpaperDirectory = new File(Environment.getExternalStorageDirectory()+IMAGE_DIRECTORY);
          if (!wallpaperDirectory.exists()){
              wallpaperDirectory.mkdir();
              Log.d("hhh",wallpaperDirectory.toString());

          }
          File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis()+".jpg");
          try {
              f.createNewFile();
              FileOutputStream fo = new FileOutputStream(f);
              fo.write(bytes.toByteArray());
              MediaScannerConnection.scanFile(MainActivity.this,new String[]{f.getPath()},
                      new String[]{"image/jpeg"},null);
              fo.close();
              f.getAbsolutePath();
              textView.setHint(f.getAbsolutePath());
              return f.getAbsolutePath();
          }catch (IOException e){
              e.printStackTrace();
          }
          return "";
      }
  */
    private void initView() {
        signatureView = findViewById(R.id.signature_view);
        save = findViewById(R.id.btnSave);
        clear = findViewById(R.id.btnClear);
        textView = findViewById(R.id.tvPath);
        imageView = findViewById(R.id.ivPath);
        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.listView);


    }


}
