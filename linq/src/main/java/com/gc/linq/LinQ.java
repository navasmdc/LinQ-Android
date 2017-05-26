package com.gc.linq;

import com.gc.linq.queries.Delete;
import com.gc.linq.queries.Select;
import com.gc.linq.queries.Update;

import java.util.HashMap;
import java.util.List;

public class LinQ<T>{

    List<T> fromList;

    public LinQ<T> FROM(List<T> list){
        fromList = list;
        return this;
    }

    public Select<T, T> SELECT() {
        return new Select<>(fromList);
    }

    public Select<Object, T> SELECT(String field) {
        return new Select<>(fromList, field);
    }

    public Select<HashMap<String, Object>, T> SELECT(String... fields) {
        return new Select<>(fromList,fields);
    }

    public Select<T, T> DELETE() {
        return new Delete<>(fromList);
    }

    public Update<T> UPDATE(String query){
        return new Update<>(fromList,query);
    }

    public Update<T> UPDATE(String query, Object... params){
        return new Update<>(fromList,query, params);
    }

}
