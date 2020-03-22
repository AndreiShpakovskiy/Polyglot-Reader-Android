package com.shpak.polyglotreader;

public class Language {
    private String langName;
    private String langCode;

    public Language(String langName, String langCode) {
        this.langName = langName;
        this.langCode = langCode;
    }

    public String getLangName() {
        return langName;
    }

    public String getLangCode() {
        return langCode;
    }
}
