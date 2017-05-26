package com.gc.linq.queries;

import com.gc.linq.exceptions.BadQuerySyntaxException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Navas on 24/5/17.
 */

public class Update<T> extends Select<T,T> {

    String updateQuery;
    Object[] updateParms;

    public Update(List<T> origin, String updateQuery) {
        super(origin);
        this.updateQuery = updateQuery;
    }

    public Update(List<T> origin, String updateQuery, Object... updateParams) {
        super(origin);
        this.updateQuery = updateQuery;
        this.updateParms = updateParams;
    }

    @Override
    public ArrayList<T> exec() {
        super.exec();
        proccesUpdateQuery();
        return result;
    }

    protected void proccesUpdateQuery() {
        if(!updateQuery.contains(",")) {
            proccesUpdateSubQuery(updateQuery);
        }else{
            for(String subquery : updateQuery.split(",")) proccesUpdateSubQuery(subquery);
        }
    }

    protected void proccesUpdateSubQuery(String subquery){
        if(!subquery.contains("=")) throw new BadQuerySyntaxException(subquery);
        for(T item : result) {
            boolean valueModified = true;
            Field field = getField(item,subquery.split("=")[0].trim());
            Object obj = splitItem(item,subquery.split("=")[0].trim());
            Object parentObject = getParentObject(item,subquery.split("=")[0].trim());
            String updateValue = subquery.split("=")[1];
            Object param = getParam(updateParms, updateValue.trim());
            try {
            if(obj instanceof Boolean){
                if(param != null && !(param  instanceof Boolean))
                    valueModified = false;
                else if(param != null)
                    field.set(parentObject, param);
                updateValue = updateValue.trim().toUpperCase();
                if(updateValue.equals("TRUE") || updateValue.equals("FALSE"))
                    field.set(parentObject, updateValue.equals("TRUE"));
                else
                    valueModified = false;
            }else if(obj instanceof Integer){
                if(param != null && !(param  instanceof Integer))
                    valueModified = false;
                else if(param != null)
                    field.set(parentObject, param);
                else
                    field.set(parentObject, Integer.parseInt(updateValue));
            }else if(obj instanceof  Double){
                    if(param != null && !(param  instanceof  Double))
                        valueModified = false;
                    else if(param != null)
                        field.set(parentObject, param);
                    else
                        field.set(parentObject,  Double.parseDouble(updateValue));
            }else if(obj instanceof  Float){
                if(param != null && !(param  instanceof  Float))
                    valueModified = false;
                else if(param != null)
                    field.set(parentObject, param);
                else
                    field.set(parentObject,  Float.parseFloat(updateValue));
            }else if(obj instanceof  Long){
                if(param != null && !(param  instanceof  Long))
                    valueModified = false;
                else if(param != null)
                    field.set(parentObject, param);
                else
                    field.set(parentObject,  Long.parseLong(updateValue));
            }else{
               if(param != null)
                    field.set(parentObject, param);
                else
                    field.set(parentObject, updateValue);
            }

            } catch (IllegalAccessException e) {
                valueModified = false;
            }catch (NumberFormatException ex){
                valueModified = false;
            }
            if(!valueModified) throw new BadQuerySyntaxException(subquery);
        }
    }

    protected Object getParentObject(T item, String fieldQuery){
        Object o = item;
        try {
            if(fieldQuery.contains(".")){
                String[] fields = fieldQuery.split("\\.");
                for(int i = 0; i < fields.length; i++){
                    if(i == fields.length -1) return o;
                    o = o.getClass().getField(fields[i]).get(o);
                }
                return null;
            }else return item;
        } catch (NoSuchFieldException e) {
            throw new BadQuerySyntaxException(fieldQuery);
        } catch (IllegalAccessException e) {
            throw new BadQuerySyntaxException(fieldQuery);
        }
    }

    protected Field getField(T item, String fieldQuery){
        Object o = item;
        try {
            if(fieldQuery.contains(".")){
                String[] fields = fieldQuery.split("\\.");
                for(int i = 0; i < fields.length; i++){
                    if(i == fields.length -1) return o.getClass().getField(fields[i]);
                    o = o.getClass().getField(fields[i]).get(o);
                }
                return null;
            }else return item.getClass().getField(fieldQuery);
        } catch (NoSuchFieldException e) {
            throw new BadQuerySyntaxException(fieldQuery);
        } catch (IllegalAccessException e) {
            throw new BadQuerySyntaxException(fieldQuery);
        }
    }






}
