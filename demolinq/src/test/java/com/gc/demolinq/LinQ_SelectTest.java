package com.gc.demolinq;

import com.gc.demolinq.model.Locale;
import com.gc.demolinq.model.User;
import com.gc.linq.LinQ;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LinQ_SelectTest {

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
    public void only_field_select() throws Exception {
        List<Object> result = new LinQ<User>().FROM(users).SELECT("name").exec();
        printResult("only_field_select", "name",result);
        assertEquals(users.size(), result.size());
    }

    @Test
    public void simple_select() throws Exception {
        List<User> result = new LinQ<User>().FROM(users).SELECT().WHERE("id < 5").exec();
        printResult("simple_select", "id < 5", result);
        assertEquals(5, result.size());
    }

    @Test
    public void field_select() throws Exception {
        List<Object> result = new LinQ<User>().FROM(users).SELECT("name").WHERE("id = 5").exec();
        printResult("field_select", "id = 5", result);
        assertEquals("Name5", result.get(0));
    }

    @Test
    public void multy_field_select() throws Exception {
        List<HashMap<String, Object>> result = new LinQ<User>().FROM(users).SELECT("name","locale").WHERE("id = 4").exec();
        printResult("multy_field_select", "(name,locale) id = 4", result);
        assertEquals(new Locale("EN","us"), result.get(0).get("locale"));
    }

    @Test
    public void and_select() throws Exception {
        List<User> result = new LinQ<User>().FROM(users).SELECT().WHERE("id > 1 AND id < 4").exec();
        printResult("and_select", "id > 1 AND id < 4", result);
        assertEquals(2, result.size());
    }

    @Test
    public void or_select() throws Exception {
        List<User> result = new LinQ<User>().FROM(users).SELECT().WHERE("id > 1 AND id < 4 OR id > 6 AND id < 9").exec();
        printResult("or_select", "id > 1 AND id < 4 OR id > 6 AND id < 9", result);
        assertEquals(4, result.size());
    }

    @Test
    public void contains_select() throws Exception {
        List<User> result = new LinQ<User>().FROM(users).SELECT().WHERE("name CONTAINS 1").exec();
        printResult("contains_select", "name CONTAINS 1", result);
        assertEquals(2, result.size());
    }

    @Test
    public void params_select() throws Exception {
        List<User> result = new LinQ<User>().FROM(users).SELECT().WHERE("id < {0}",7).exec();
        printResult("params_select", "id < {0}", result);
        assertEquals(7, result.size());
    }

    @Test
    public void internal_object_select() throws Exception {
        List<User> result = new LinQ<User>().FROM(users).SELECT().WHERE("locale.country = {0}","es").exec();
        printResult("internal_object_select", "locale.country = {0}", result);
        assertEquals(7, result.size());
    }

}