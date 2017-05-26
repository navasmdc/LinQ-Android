package com.gc.linq.queries;

import com.gc.linq.exceptions.BadQuerySyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Navas on 23/5/17.
 */

public class Select<T,K> {

    ArrayList<T> result;
    List<K> origin;

    String[] fields;

    //region CONSTRUCTORS

    public Select() { }

    public Select(List<K> origin) {
        this.origin = origin;
    }

    public Select(List<K> origin, String... fields) {
        this.origin = origin;
        this.fields = fields;
    }

    //endregion

    //region WHERE

    Object[] params;

    public Select<T,K> WHERE(String query){
        result = new ArrayList<>();
        processQuery(query);
        return this;
    }

    public Select<T,K> WHERE(String query, Object... params){
        result = new ArrayList<>();
        this.params = params;
        processQuery(query);
        return this;
    }

    protected void processQuery(String query){
        ArrayList<Condition> conditions = generateConditions(query);
        for(K item : origin){
            if(checkConditions(item,conditions)) addToResult(item);
        }

    }

    protected ArrayList<Condition> generateConditions(String query){
        ArrayList<Condition> conditions = new ArrayList<>();
        if(query.contains("OR")){
            for(String s : query.split("OR")){
                if(s.contains("AND")){
                    conditions.add(new Condition(s.split("AND")));
                }else{
                    conditions.add(new Condition(s));
                }
            }
        }else{
            if(query.contains("AND")){
                conditions.add(new Condition(query.split("AND")));
            }else{
                conditions.add(new Condition(query));
            }
        }
        return conditions;
    }

    protected boolean checkConditions(K item, ArrayList<Condition> conditions){
        for(Condition condition : conditions){
            if(checkCondition(item, condition)) return true;
        }
        return false;
    }

    protected boolean checkCondition(K item, Condition condition){
        for(SubQuery subQuery : condition.subQueries){
            if(!checkSubQuery(item, subQuery)) return false;
        }
        return true;
    }

    protected boolean checkSubQuery(K item, SubQuery subQuery){
        Object o = splitItem(item,subQuery);
        if(o == null) return  false;
        try {
            Object param = getParam(params,subQuery.comparison);
            switch (subQuery.comparator) {
                case ">=":
                    return Integer.parseInt(o.toString()) >= getInt(subQuery);
                case "<=":
                    return Integer.parseInt(o.toString()) <= getInt(subQuery);
                case "<":
                    return Integer.parseInt(o.toString()) < getInt(subQuery);
                case ">":
                    return Integer.parseInt(o.toString()) > getInt(subQuery);
                case "CONTAINS":
                    if(param != null) return o.toString().contains(param.toString());
                    else return o.toString().contains(subQuery.comparison);
                case "=":
                    if(param != null) return o.equals(param);
                    else if(subQuery.comparison.matches("^\\d+$") && o instanceof Integer){
                        return Integer.parseInt(o.toString()) == Integer.parseInt(subQuery.comparison);
                    }else return o.equals(subQuery.comparison);
                case "!=":
                    if(param != null) return !o.equals(param);
                    else if(subQuery.comparison.matches("^\\d+$") && o instanceof Integer){
                        return Integer.parseInt(o.toString()) != Integer.parseInt(subQuery.comparison);
                    }else return !o.equals(subQuery.comparison);
            }
        }catch (NumberFormatException ex){
            throw new BadQuerySyntaxException(subQuery.subQuery);
        }
        return false;
    }

    protected Object splitItem(K itemClass, SubQuery subQuery){
        try{
            return splitItem(itemClass,subQuery.item);
        }catch (BadQuerySyntaxException ex){
            throw new BadQuerySyntaxException(subQuery.subQuery);
        }
    }

    protected Object splitItem(K itemClass, String field){
        Object o = itemClass;
        try {
            if(field.equals("this")) return itemClass;
            else if(field.contains(".")){
                for(String _field : field.split("\\.")){
                    o = o.getClass().getField(_field).get(o);
                }
                return o;
            }else return o.getClass().getField(field).get(o);
        } catch (NoSuchFieldException e) {
            throw new BadQuerySyntaxException(field);
        } catch (IllegalAccessException e) {
            throw new BadQuerySyntaxException(field);
        }
    }

    protected int getInt(SubQuery subQuery){
        Object o = getParam(params,subQuery.comparison);
        if(o != null && o instanceof Integer) return (int) o;
        return Integer.parseInt(subQuery.comparison);
    }

    protected Object getParam(Object[] params, String subQuery){
        if(params == null) return null;
        if(subQuery.matches("\\{\\d}")){
            int position = Integer.parseInt(subQuery.replace("{","").replace("}",""));
            if(position >= params.length) throw new BadQuerySyntaxException("Index out of bounds "+subQuery);
            return params[position];
        }else{
            return null;
        }
    }

    protected void addToResult(K item){
        if(fields == null){
            result.add((T) item);
        }
        else if (fields.length == 1){
            result.add((T) splitItem(item, fields[0]));
        }else{
            HashMap<String, Object> resultMap = new HashMap<>();
            for(String field : fields)
                resultMap.put(field, splitItem(item,field));
            result.add((T) resultMap);
        }
    }

    //endregion

    //region QUERY CONDITION
    protected class Condition{
        ArrayList<SubQuery> subQueries = new ArrayList<>();

        public Condition(String subquery){
            add(subquery);
        }

        public Condition(String[] subqueries) {
            for(String s : subqueries) add(s);
        }

        public void add(String subquery){
            subQueries.add(new SubQuery(subquery));
        }
    }

    protected static class SubQuery{

        String subQuery;
        String item;
        String comparator;
        String comparison;

        static String[] COMPARATORS = {"!=",">=","<=","=","<",">","CONTAINS"};

        public SubQuery(String subquery) {
            this.subQuery = subquery;
            for(String comparator : COMPARATORS){
                if(subquery.contains(comparator)){
                    if(subquery.split(comparator).length != 2)
                        throw new BadQuerySyntaxException(subquery);
                    item = subquery.split(comparator)[0].trim();
                    comparison = subquery.split(comparator)[1].trim();
                    this.comparator = comparator;
                    return;
                }
            }
            if(comparator == null) throw new BadQuerySyntaxException(subquery);
        }
    }
    //endregion

    public ArrayList<T> exec(){
        if(result == null){
            result = new ArrayList<>();
            for(K item : origin) addToResult(item);
        }
        return result;
    }
}
