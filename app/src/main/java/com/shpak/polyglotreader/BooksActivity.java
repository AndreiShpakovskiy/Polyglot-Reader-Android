package com.shpak.polyglotreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BooksActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private File curDirectory;
    private final int OPEN_FILE_DIALOG_ID = 1;
    private final int SETTINGS_DIALOG_ID = 2;
    private final int MIN_FONT_SIZE = 18;
    private final int DEFAULT_FONT_SIZE = 20;
    private final int MAX_FONT_SIZE = 40;
    private List directoryFilesList;
    private List bookDirectoryFiles;
    private final String BOOKS_FOLDER_NAME = "Polyglot Reader";
    private final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    private final String BOOKS_FOLDER_ABSOLUTE_PATH = Environment.getExternalStorageDirectory() + "/" + BOOKS_FOLDER_NAME;
    private static LocalisedFields localisedFields = new LocalisedFields(Locale.getDefault().getLanguage());
    private int fromLangIndex = 6, toLangIndex = 0;
    private int fontSize = 22;

    private String savedBookSettings;

    TextView fontSizeView;
    SeekBar fontSizeChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        SharedPreferences readerSettings = getPreferences(MODE_PRIVATE);
        savedBookSettings = readerSettings.getString("readerSettings", "?");

        if (savedBookSettings.equals("?")) {
            fontSize = DEFAULT_FONT_SIZE;
            fromLangIndex = 0;
            toLangIndex = 6;
        }
        else {
            fontSize = Integer.parseInt(getByField(savedBookSettings, "fontSize="));
            fromLangIndex = getLanguageIndex(getByField(savedBookSettings, "from="));
            toLangIndex = getLanguageIndex(getByField(savedBookSettings, "to="));
        }

        ListView availableBooksList = findViewById(R.id.availableBooksList);
        TextView noBooksText = findViewById(R.id.noBooksText);
        noBooksText.setText(LocalisedFields.NO_BOOKS);
        bookDirectoryFiles = getFilesList(new File(BOOKS_FOLDER_ABSOLUTE_PATH));
        if (bookDirectoryFiles.size() != 0) {
            updateList(availableBooksList, bookDirectoryFiles);
            noBooksText.setAlpha(0.0f);
        }
        else {
            noBooksText.setAlpha(1.0f);
        }

        availableBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curDirectory = new File(bookDirectoryFiles.get(position).toString());
                Intent intent = new Intent(BooksActivity.this, Reader.class);
                String bookInfo = "new=false&path=" + curDirectory.getPath() +
                                  "&fontSize=" + fontSize +
                                  "&from=" + LocalisedFields.SUPPORTED_LANGUAGES[fromLangIndex].getLangCode() +
                                  "&to=" + LocalisedFields.SUPPORTED_LANGUAGES[toLangIndex].getLangCode() + "&";
                intent.putExtra("bookInfo", bookInfo);
                startActivity(intent);
            }
        });

        Button yandexButton = findViewById(R.id.yandexButton);
        yandexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://translate.yandex.by/"));
                startActivity(browserIntent);
            }
        });

        yandexButton.setText(LocalisedFields.YANDEX_BUTTON_TEXT);
    }

    private String getByField(String inLine, String field) {
        return inLine.substring(inLine.indexOf(field) + field.length(), inLine.indexOf("&", inLine.indexOf(field)));
    }

    private int getLanguageIndex(String langCode) {
        for (int i = 0; i < LocalisedFields.SUPPORTED_LANGUAGES.length; i++) {
            if (LocalisedFields.SUPPORTED_LANGUAGES[i].getLangCode().equals(langCode)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected Dialog onCreateDialog(final int dialogId) {
        Dialog addFileDialog = null;
        switch (dialogId) {

            case OPEN_FILE_DIALOG_ID:
                curDirectory = new File(ROOT_PATH);
                addFileDialog = new Dialog(BooksActivity.this);
                addFileDialog.setContentView(R.layout.open_file_dialog);
                addFileDialog.setCancelable(true);
                addFileDialog.setCanceledOnTouchOutside(true);

                final Button backButton = addFileDialog.findViewById(R.id.backButton);
                backButton.setEnabled(false);
                String systemLang = Locale.getDefault().getLanguage();
                backButton.setText(LocalisedFields.BACK_BUTTON_TEXT);
                final ListView filesListView = addFileDialog.findViewById(R.id.filesList);
                directoryFilesList = getFilesList(curDirectory);
                updateList(filesListView, directoryFilesList);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!curDirectory.isDirectory()) {
                            curDirectory = curDirectory.getParentFile();
                        }
                        curDirectory = curDirectory.getParentFile();

                        directoryFilesList = getFilesList(curDirectory);
                        updateList(filesListView, directoryFilesList);

                        if (curDirectory.getPath().equals(ROOT_PATH)) {
                            backButton.setEnabled(false);
                        }
                    }
                });

                filesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        curDirectory = new File(directoryFilesList.get(position).toString());
                        if (curDirectory.isDirectory()) {
                            directoryFilesList = getFilesList(curDirectory);
                            updateList(filesListView, directoryFilesList);
                            backButton.setEnabled(true);
                        } else {
                            if (checkExtension(curDirectory.getPath())) {
                                Intent intent = new Intent(BooksActivity.this, Reader.class);
                                String bookInfo = "new=true&path=" + curDirectory.getPath() +
                                                  "&fontSize=" + fontSize +
                                                  "&from=" + LocalisedFields.SUPPORTED_LANGUAGES[fromLangIndex].getLangCode() +
                                                  "&to=" + LocalisedFields.SUPPORTED_LANGUAGES[toLangIndex].getLangCode() + "&";
                                intent.putExtra("bookInfo", bookInfo);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(BooksActivity.this, LocalisedFields.SUPPORTED_EXTENSION_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                return addFileDialog;

            case SETTINGS_DIALOG_ID:
                Dialog settingsDialog = null;
                settingsDialog = new Dialog(BooksActivity.this);
                settingsDialog.setContentView(R.layout.settings_dialog);
                settingsDialog.setCancelable(true);
                settingsDialog.setCanceledOnTouchOutside(false);

                TextView labelFrom = settingsDialog.findViewById(R.id.fromLabel);
                labelFrom.setText(LocalisedFields.LABEL_FROM_TEXT);

                TextView labelTo = settingsDialog.findViewById(R.id.toLabel);
                labelTo.setText(LocalisedFields.LABEL_TO_TEXT);

                ArrayAdapter<String> langsListAdapter;
                langsListAdapter = new ArrayAdapter<>(BooksActivity.this,
                        R.layout.custom_text_spinner_item, LocalisedFields.getLanguagesNames());

                langsListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                final Spinner boxFrom = settingsDialog.findViewById(R.id.boxFrom);
                boxFrom.setAdapter(langsListAdapter);
                boxFrom.setSelection(fromLangIndex);

                final Spinner boxTo = settingsDialog.findViewById(R.id.boxTo);
                boxTo.setAdapter(langsListAdapter);
                boxTo.setSelection(toLangIndex);

                fontSizeView = settingsDialog.findViewById(R.id.fontSizeView);
                fontSizeView.setTextSize(fontSize);
                fontSizeView.setText(LocalisedFields.TEXT_EXAMPLE);

                fontSizeChanger = settingsDialog.findViewById(R.id.fontSizeChanger);
                fontSizeChanger.setOnSeekBarChangeListener(BooksActivity.this);
                fontSizeChanger.setMax(MAX_FONT_SIZE);
                fontSizeChanger.setProgress(fontSize - MIN_FONT_SIZE);

                Button okButton = settingsDialog.findViewById(R.id.okButton);
                final Dialog finalSettingsDialog = settingsDialog;
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fontSize = fontSizeChanger.getProgress() + MIN_FONT_SIZE;
                        fromLangIndex = boxFrom.getSelectedItemPosition();
                        toLangIndex = boxTo.getSelectedItemPosition();

                        SharedPreferences readerSettings = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor settingsEditor = readerSettings.edit();
                        settingsEditor.putString("readerSettings", "fontSize=" + fontSize + "" +
                                                 "&from=" + LocalisedFields.SUPPORTED_LANGUAGES[fromLangIndex].getLangCode() +
                                                 "&to=" + LocalisedFields.SUPPORTED_LANGUAGES[toLangIndex].getLangCode() + "&");
                        settingsEditor.commit();
                        finalSettingsDialog.dismiss();
                    }
                });
                return settingsDialog;
            default:
                return null;
        }
    }

    private List<String> getFilesList(File directoryFile) {
        List<String> filesList = new ArrayList<>();
        File[] directoryFiles = directoryFile.listFiles();

        filesList.clear();
        for (File file : directoryFiles) {
            filesList.add(file.getPath());
        }
        return filesList;
    }

    private void updateList(ListView list, List<String> filesList) {
        List<String> justNamesList = new ArrayList<>();
        for (int i = 0; i < filesList.size(); i++) {
            justNamesList.add(filesList.get(i).substring(filesList.get(i).lastIndexOf("/") + 1));
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, justNamesList);
        list.setAdapter(directoryList);
    }

    private boolean checkExtension(String filePath) {
        final String[] SUPPORTED_EXTENSIONS = {"txt"};
        String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1);
        for (String extension : SUPPORTED_EXTENSIONS) {
            if (fileExtension.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsButton:
                showDialog(SETTINGS_DIALOG_ID);
                return true;
            case R.id.openFileButton:
                showDialog(OPEN_FILE_DIALOG_ID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        fontSizeView.setTextSize(fontSizeChanger.getProgress() + MIN_FONT_SIZE);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        fontSizeView.setTextSize(fontSizeChanger.getProgress() + MIN_FONT_SIZE);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        fontSizeView.setTextSize(fontSizeChanger.getProgress() + MIN_FONT_SIZE);
    }
}
