package com.example.signaturepad;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.kyanogen.signatureview.SignatureView;

public class MainActivity extends AppCompatActivity {

    private Button buttonClear;
    private Button buttonSearch;

    private EditText searchView;

    private SignatureView signatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setUpViewListener();
    }

    private void initView() {
        signatureView = findViewById(R.id.signature_view);
        buttonClear = findViewById(R.id.buttonClear);
        buttonSearch = findViewById(R.id.buttonSearch);
        searchView = findViewById(R.id.searchView);
    }

    private void setUpViewListener() {
        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                searchView.setText("");
                signatureView.clearCanvas();
            }
        });

        buttonSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                updateWritingOnSearchView();
            }
        });
    }

    private void updateWritingOnSearchView() {
        Bitmap bitmap = signatureView.getSignatureBitmap();
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
            searchView.setText(sb.toString());
            signatureView.clearCanvas();
        }
    }
}
