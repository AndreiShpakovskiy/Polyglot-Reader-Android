package com.shpak.polyglotreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.Locale;

public class Reader extends AppCompatActivity  {
    private final int QUICK_SETTINGS_DIALOG_ID = 1;
    private final String BOOKS_FOLDER_NAME = "Polyglot Reader";
    private final String BOOKS_FOLDER_ABSOLUTE_PATH = Environment.getExternalStorageDirectory() + "/" + BOOKS_FOLDER_NAME;

    private String[] bookWords;

    private int firstWordNumber = 2;
    private int lastWordNumber = 0;
    private int curWordNumber;

    private int maxLines;

    private String bookInfo;
    private String bookPath;

    private String langFrom = "en", langTo = "en";
    private String readerTheme = "light";

    RelativeLayout readerLayout;
    ImageView nextButton;
    ImageView backButton;
    TextView bookView;
    SeekBar progressChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reader);
        getSupportActionBar().hide();

        if (getIntent().getExtras() != null) {
            bookInfo = getIntent().getExtras().getString("bookInfo");
            bookPath = getByField(bookInfo, "path=");
            langFrom = getByField(bookInfo, "from=");
            langTo = getByField(bookInfo, "to=");

            SharedPreferences firstWord = getPreferences(MODE_PRIVATE);
            firstWordNumber = firstWord.getInt(bookPath.substring(bookPath.lastIndexOf("/")), 0);
            lastWordNumber = firstWordNumber;

            final FileOperations fileOperations = new FileOperations();
            final String bookText = fileOperations.textFromFile(new File(bookPath));
            bookWords = bookText.split(" ");

            bookView = findViewById(R.id.bookView);
            bookView.setTextSize(Integer.parseInt(getByField(bookInfo, "fontSize=")));

            Display deviceDisplay = getWindowManager().getDefaultDisplay();
            Point displaySize = new Point();
            deviceDisplay.getSize(displaySize);

            maxLines = (displaySize.y / bookView.getLineHeight()) - (200 / bookView.getLineHeight()) - 1;

            bookView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    maxLines = (bookView.getMeasuredHeight() / bookView.getLineHeight());
                }
            });

            //maxLines = (displaySize.y / bookView.getLineHeight()) - (200 / bookView.getLineHeight()) - 1;

            nextButton = findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextPage();
                }
            });

            backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousPage();
                }
            });

            nextPage();

            if (getByField(bookInfo, "new=").equals("true")) {
                fileOperations.textToFile(bookText, BOOKS_FOLDER_ABSOLUTE_PATH +
                        bookPath.substring(bookPath.lastIndexOf("/")));
            }

            ImageView quickSettingsButton = findViewById(R.id.quickSettings);
            quickSettingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(QUICK_SETTINGS_DIALOG_ID);
                }
            });
/*
            SharedPreferences readerColorTheme = getPreferences(MODE_PRIVATE);
            readerTheme = readerColorTheme.getString("colorTheme", "?");

            readerLayout = findViewById(R.id.readerLayout);
            if (readerTheme.equals("?")) {
                setLightTheme();
            }
            else {
                setDarkTheme();
            }
            */

        }
    }
/*
    private void setLightTheme() {
        readerTheme = "light";

        readerLayout.setBackgroundColor(Color.parseColor("#F1E9D1"));
        bookView.setTextColor(Color.parseColor("#000000"));
        backButton.setAlpha(0.3f);
        nextButton.setAlpha(0.3f);
    }

    private void setDarkTheme() {
        readerTheme = "dark";

        readerLayout.setBackgroundColor(Color.parseColor("#2C2C2C"));
        //bookView.setTextColor(Color.parseColor("#EAEAEA"));

        String pageText = bookView.getText() + "";

        bookView.setMovementMethod(LinkMovementMethod.getInstance());
        bookView.setText(pageText, TextView.BufferType.SPANNABLE);
        Spannable spans = (Spannable) bookView.getText();

        //spans.setSpan(new ForegroundColorSpan(Color.parseColor("#EAEAEA")), 0, pageText.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //bookView.setText(spans);
        //spans.setSpan(new TermAndConditions(), 0, start + terms.length(), Spannable.SPAN_POINT_MARK);
        spans.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0, pageText.length() - 1, 0);


        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(pageText);
        int start = iterator.first();

        try {
            for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
                String possibleWord = pageText.substring(start, end);
                if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                    ClickableSpan clickSpan = getClickableSpan(possibleWord);
                    spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        catch (Exception e){}



        backButton.setAlpha(0.3f);
        nextButton.setAlpha(0.3f);
    }
*/
    @Override
    protected Dialog onCreateDialog(final int dialogId) {
        switch (dialogId) {
            case QUICK_SETTINGS_DIALOG_ID:
                Dialog quickSettingsDialog = null;
                quickSettingsDialog = new Dialog(Reader.this);
                quickSettingsDialog.setContentView(R.layout.quick_settings_dialog);
                quickSettingsDialog.setCancelable(true);
                quickSettingsDialog.setCanceledOnTouchOutside(true);

                TextView progressLabel = quickSettingsDialog.findViewById(R.id.progressLabel);
                progressLabel.setText(LocalisedFields.PROGRESS_LABEL_TEXT);

                /*
                final Button themeButton = quickSettingsDialog.findViewById(R.id.themeButton);
                themeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (readerTheme.equals("light")) {
                            setDarkTheme();
                            themeButton.setText("dark");
                        }
                        else {
                            setLightTheme();
                            themeButton.setText("light");
                        }
                    }
                });
                */

                Button okButton = quickSettingsDialog.findViewById(R.id.okButton);
                final Dialog finalQuickSettingsDialog = quickSettingsDialog;
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalQuickSettingsDialog.dismiss();
                    }
                });

                progressChanger = quickSettingsDialog.findViewById(R.id.progressChanger);
                progressChanger.setMax(bookWords.length - 100);
                progressChanger.setProgress(lastWordNumber - 1);

                progressChanger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        firstWordNumber = progressChanger.getProgress();
                        lastWordNumber = firstWordNumber;
                        nextPage();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        firstWordNumber = progressChanger.getProgress();
                        lastWordNumber = firstWordNumber;
                        nextPage();
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        firstWordNumber = progressChanger.getProgress();
                        lastWordNumber = firstWordNumber;
                        nextPage();
                    }
                });

                return quickSettingsDialog;

            default:
                return null;
        }
    }

    private void nextPage() {
        final TextView bookView = findViewById(R.id.bookView);

        if (lastWordNumber >= bookWords.length - 1) {

            Toast.makeText(Reader.this, LocalisedFields.LAST_PAGE_TEXT,
                        Toast.LENGTH_LONG).show();
        }
        else {
            firstWordNumber = lastWordNumber;
            curWordNumber = firstWordNumber;
            bookView.post(new Runnable() {
                @Override
                public void run() {
                    String nextPage = "";
                    int lineCount = bookView.getLineCount();
                    while (lineCount < maxLines + 1 && curWordNumber < bookWords.length) {
                        nextPage += bookWords[curWordNumber] + " ";
                        bookView.setText(nextPage);
                        lineCount = bookView.getLineCount();
                        curWordNumber++;
                    }
                    if (curWordNumber != bookWords.length) {
                        nextPage = nextPage.trim();
                        nextPage = nextPage.substring(0, nextPage.lastIndexOf(" "));
                    }
                    lastWordNumber = curWordNumber - 1;

                    bookView.setMovementMethod(LinkMovementMethod.getInstance());
                    bookView.setText(nextPage, TextView.BufferType.SPANNABLE);
                    Spannable spans = (Spannable) bookView.getText();
                    BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
                    iterator.setText(nextPage);
                    int start = iterator.first();

                    try {
                        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
                            String possibleWord = nextPage.substring(start, end);
                            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                                spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                    }
                    catch (Exception e){}

                    SharedPreferences firstWord = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor firstWordEditor = firstWord.edit();
                    firstWordEditor.putInt(bookPath.substring(bookPath.lastIndexOf("/")), firstWordNumber);
                    firstWordEditor.commit();

                    ProgressBar progress = findViewById(R.id.progress);
                    progress.setMax(bookWords.length - 1);
                    progress.setProgress(lastWordNumber);
                }
            });
        }
    }

    private void previousPage() {
        final TextView bookView = findViewById(R.id.bookView);

        if (firstWordNumber > 0) {
            curWordNumber = firstWordNumber - 1;
            lastWordNumber = firstWordNumber;

            bookView.setText(" ");
            bookView.post(new Runnable() {
                @Override
                public void run() {
                    String previousPage = "";
                    int lineCount = bookView.getLineCount();

                    while (lineCount < maxLines + 1 && curWordNumber >= 0) {
                        previousPage = bookWords[curWordNumber] + " " + previousPage;
                        bookView.setText(previousPage);
                        lineCount = bookView.getLineCount();
                        curWordNumber--;
                    }
                    if (curWordNumber >= 0) {
                        previousPage = previousPage.substring(previousPage.indexOf(" ") + 1);
                    }

                    bookView.setMovementMethod(LinkMovementMethod.getInstance());
                    bookView.setText(previousPage, TextView.BufferType.SPANNABLE);
                    Spannable spans = (Spannable) bookView.getText();
                    BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
                    iterator.setText(previousPage);
                    int start = iterator.first();

                    try {
                        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
                            String possibleWord = previousPage.substring(start, end);
                            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                                spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                    }
                    catch (Exception e){}

                    firstWordNumber = curWordNumber + 1;

                    SharedPreferences firstWord = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor firstWordEditor = firstWord.edit();
                    firstWordEditor.putInt(bookPath.substring(bookPath.lastIndexOf("/")), firstWordNumber + 1);
                    firstWordEditor.commit();

                    ProgressBar progress = findViewById(R.id.progress);
                    progress.setMax(bookWords.length - 1);
                    progress.setProgress(lastWordNumber);

                }
            });
        }
        else {
            Toast.makeText(Reader.this, LocalisedFields.FIRST_PAGE_TEXT, Toast.LENGTH_LONG).show();
        }
    }

    private String getByField(String inLine, String field) {
        return inLine.substring(inLine.indexOf(field) + field.length(), inLine.indexOf("&", inLine.indexOf(field)));
    }

    private ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            final String chosenWord;
            { chosenWord = word; }

            @Override
            public void onClick(View widget) {
                final String translatedWord = translate(chosenWord, langFrom, langTo);
                Snackbar.make(Reader.this.findViewById(R.id.readerLayout), chosenWord + " - " + translatedWord, Snackbar.LENGTH_LONG).show();
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
    }

    public String translate(String word, String langFrom, String langTo){
        String resString = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20180411T202148Z.b94c06f7b93925de.498d575cbed4d3f4663a5ceed101d963a61dbb3e&text=" + word + "&lang=" + langFrom + "-" + langTo);
        try {
            HttpResponse response;
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            resString = sb.toString();
            resString = resString.substring(resString.indexOf('[')+1);
            resString = resString.substring(0,resString.indexOf("]"));
            resString = resString.substring(resString.indexOf("\"")+1);
            resString = resString.substring(0,resString.indexOf("\""));
            is.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Reader.this, "Error.", Toast.LENGTH_SHORT).show();
        }
        return resString;
    }

}
