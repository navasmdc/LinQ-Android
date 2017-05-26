package com.gc.linq.queries;

import com.gc.linq.exceptions.BadQuerySyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Navas on 24/5/17.
 */

public class Delete<T> extends Select<T,T> {

    public Delete(List<T> origin) {
        super(origin);
    }

    @Override
    public ArrayList<T> exec() {
        if(result == null) throw new BadQuerySyntaxException("NO WHERE");
        origin.removeAll(result);
        return result;
    }
}
