package com.gc.demolinq.model;

/**
 * Created by Navas on 24/5/17.
 */

public class User {

    static int cont = 0;

    public int id;
    public String name;
    public Locale locale;

    public User(Locale locale) {
        this.locale = locale;
        id = cont;
        name = "Name"+id;
        cont++;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", locale=" + locale.toString() +
                '}';
    }
}
