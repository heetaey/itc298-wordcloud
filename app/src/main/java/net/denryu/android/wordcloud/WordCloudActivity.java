package net.denryu.android.wordcloud;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WordCloudActivity extends AppCompatActivity implements
        OnClickListener {

    private static final int OPEN_DOCUMENT_REQUEST = 1;
    private EditText txtInput;
    private Button clearInputButton;
    private Button openFileButton;
    private Button generateImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordcloud_activity);

        txtInput = (EditText) findViewById(R.id.txtInput);
        generateImage = (Button) findViewById(R.id.generateImage);
        generateImage.setOnClickListener(this);
        openFileButton = (Button) findViewById(R.id.openFileButton);
        openFileButton.setOnClickListener(this);
        clearInputButton = (Button) findViewById(R.id.clearInputButton);
        clearInputButton.setOnClickListener(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Log.d("WordCloudActivity", "intentData: " + intent.getClipData().getItemAt(0).toString());
            if ("text/plain".equals(type) && (intent.getClipData().getItemAt(0) != null)) {
                Uri uri = intent.getClipData().getItemAt(0).getUri();
                try {
                    String content = readFileContent(uri);
                    txtInput.setText(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if ("text/plain".equals(type)) {
                handleSendText(intent);
                Log.d("WordCloudActivity", "UriIntent: " + intent.toString());
            }
        }

        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (intent.getData() != null) {
                Uri uri = intent.getData();
                try {
                    String content = readFileContent(uri);
                    txtInput.setText(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            txtInput.setText(sharedText);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_word_input, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_about:
                new AlertDialog.Builder(this).setTitle("About")
                        .setMessage("This will have our ABOUT messages")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
            case R.id.item_history:
                Intent i = new Intent(WordCloudActivity.this, WordCloudHistoryActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.generateImage:
                Intent i = new Intent(WordCloudActivity.this, WordCloudOutputActivity.class);
                i.putExtra("txtInput", txtInput.getText().toString());
                startActivity(i);
                break;
            case R.id.openFileButton:
                openFile();
//              openPDFDOC();
                break;
            case R.id.clearInputButton:
                new AlertDialog.Builder(this).
                        setMessage("Are you sure you want to clear all text input?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                txtInput.setText("");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
        }
    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
    }

//    private void openPDFDOC() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/pdf");
//        startActivityForResult(intent, LOAD_IMAGE_RESULTS);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == OPEN_DOCUMENT_REQUEST && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();

                try {
                    String content = readFileContent(uri);
                    txtInput.setText(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String readFileContent(Uri uri) throws IOException {
        InputStream inStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentline;
        while ((currentline = reader.readLine()) != null) {
            stringBuilder.append(currentline + "\n");
        }
        inStream.close();
        return stringBuilder.toString();
    }
}
