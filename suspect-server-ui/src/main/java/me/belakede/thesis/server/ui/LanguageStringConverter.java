package me.belakede.thesis.server.ui;

import javafx.util.StringConverter;

import java.util.Arrays;

public class LanguageStringConverter extends StringConverter<Language> {

    @Override
    public String toString(Language object) {
        return object.getNativeName();
    }

    @Override
    public Language fromString(String string) {
        return Arrays.stream(Language.values()).filter(lang -> string.equals(lang.getNativeName())).findFirst().get();
    }
}
