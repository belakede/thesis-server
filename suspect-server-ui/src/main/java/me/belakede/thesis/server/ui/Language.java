package me.belakede.thesis.server.ui;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum Language {

    HUNGARIAN(new Locale("hu"), "magyar"), ENGLISH(new Locale("en"), "English");

    private final Locale locale;
    private final String nativeName;

    Language(Locale locale, String nativeName) {
        this.locale = locale;
        this.nativeName = nativeName;
    }

    public static Language valueOf(Locale locale) {
        Optional<Language> language = Arrays.stream(values()).filter(lang -> lang.getLocale().getLanguage().equals(locale.getLanguage())).findFirst();
        if (!language.isPresent()) {
            throw new IllegalArgumentException("Language not found for the specified localge");
        }
        return language.get();
    }

    public Locale getLocale() {
        return locale;
    }

    public String getNativeName() {
        return nativeName;
    }

}
