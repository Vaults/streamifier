package com.vaults;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class ResultSetIterable<T> implements Iterable<T> {

    private ResultSet set;
    private Function<ResultSet, T> mapper;
    private Consumer<Exception> exceptionHandler;

    ResultSetIterable(ResultSet set, Function<ResultSet, T> mapper, Consumer<Exception> exceptionHandler){
        this.set = set;
        this.mapper = mapper;
        this.exceptionHandler = exceptionHandler;
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
                    T value = mapper.apply(set);
                    try {
                        hasNext = set.next();
                    } catch (SQLException e) {
                        exceptionHandler.accept(e);
                    }
                    return value;
                }
            };
        } catch (SQLException e) {
            exceptionHandler.accept(e);
            return null;
        }
    }
}
