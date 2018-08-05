package com.vaults;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class ResultSetIterable<T> implements Iterable<T> {

    private ResultSet set;
    private CheckedFunction<ResultSet, T> mapper;
    private Consumer<Exception> exceptionHandler;
    private Consumer<SQLException> sqlExceptionHandler;

    ResultSetIterable(ResultSet set, CheckedFunction<ResultSet, T> mapper, Consumer<Exception> exceptionHandler, Consumer<SQLException> sqlExceptionHandler){
        this.set = set;
        this.mapper = mapper;
        this.exceptionHandler = exceptionHandler;
        this.sqlExceptionHandler = sqlExceptionHandler;
    }

    @Override
    public Iterator<T> iterator() {
        try {
            return new Iterator<T>() {

                boolean hasNext = set.next();

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public T next() {
                    T value = null;

                    try {
                        value = mapper.apply(set);
                        hasNext = set.next();
                    } catch (SQLException e){
                        hasNext = false;
                        sqlExceptionHandler.accept(e);
                    } catch (Exception e) {
                        hasNext = false;
                        exceptionHandler.accept(e);
                    }

                    return value;
                }
            };
        } catch (SQLException e) {
            sqlExceptionHandler.accept(e);
            return Collections.emptyIterator();
        }
    }
}
