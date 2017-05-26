package com.gc.demolinq;

import com.gc.demolinq.model.Locale;
import com.gc.demolinq.model.User;
import com.gc.linq.LinQ;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LinQ_UpdateTest {

    static ArrayList<User> users = new ArrayList<>();

    static {
        /*
            {id=0, name='Name0', locale={language='ES', country='es'}}
            {id=1, name='Name1', locale={language='ES', country='es'}}
            {id=2, name='Name2', locale={language='ES', country='us'}}
            {id=3, name='Name3', locale={language='EN', country='en'}}
            {id=4, name='Name4', locale={language='EN', country='us'}}
            {id=5, name='Name5', locale={language='ES', country='es'}}
            {id=6, name='Name6', locale={language='ES', country='es'}}
            {id=7, name='Name7', locale={language='ES', country='es'}}
            {id=8, name='Name8', locale={language='ES', country='es'}}
            {id=9, name='Name9', locale={language='ES', country='es'}}
            {id=10, name='Name10', locale={language='ES', country='es'}}
         */
        users.add(new User(new Locale("ES","es")));
        users.add(new User(new Locale("ES","es")));
        users.add(new User(new Locale("ES","us")));
        users.add(new User(new Locale("EN","en")));
        users.add(new User(new Locale("EN","us")));
        users.add(new User(new Locale("ES","es")));
        users.add(new User(new Locale("EN","gb")));
        users.add(new User(new Locale("ES","es")));
        users.add(new User(new Locale("FR","ca")));
        users.add(new User(new Locale("FR","fr")));
        users.add(new User(new Locale("ES","es")));
    }


//    @Test
//    public void simple_sel7ect() throws Exception {
//        List<HashMap<String, Object>> result = new LinQ<User>().FROM(users).SELECT("id","name").WHERE("language.country = us OR language.language = EN AND language.country = {0} OR id > {1}","us",4).exec();
//        assertEquals(2, result.size());
//    }

    public void printResult(String name, String query, List result){
        System.out.println("------------------------------------------------");
        System.out.println(name +" -> "+query);
        for(Object object : result) System.out.println(object);
        System.out.println("------------------------------------------------");
    }


    @Test
    public void update() throws Exception {
        ArrayList<User> copyUsers = new ArrayList<>(users);
        List<User> result = new LinQ<User>().FROM(copyUsers).UPDATE("locale.country=modified").WHERE("id < 5").exec();
        printResult("update", "(locale.country=modified) id< 5", copyUsers);
        assertEquals(result.get(0).locale.country, "modified");
    }

    @Test
    public void update2() throws Exception {
        ArrayList<User> copyUsers = new ArrayList<>(users);
        List<User> result = new LinQ<User>().FROM(copyUsers).UPDATE("locale={0}, name = ITALIAN",new Locale("IT","it")).WHERE("id < 5 OR id > 8").exec();
        printResult("update", "(locale={0}, name=ITALIAN)", copyUsers);
        assertEquals(result.get(0).locale, new Locale("IT","it"));
    }


}