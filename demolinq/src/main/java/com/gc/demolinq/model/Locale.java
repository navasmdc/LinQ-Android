package com.gc.demolinq.model;

/**
 * Created by Navas on 24/5/17.
 */

public class Locale {

    public String language;
    public String country;

    public Locale(String language, String country) {
        this.language = language;
        this.country = country;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Locale){
            Locale locale = (Locale) obj;
            return country.equals(locale.country) && language.equals(locale.language);
        }else return false;
    }

    @Override
    public String toString() {
        return "{" +
                "language='" + language + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
