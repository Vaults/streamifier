package com.vaults;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.ResultSet;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ExtendedStream extends Stream {

    static <T> Stream<T> of(T[] array){
        return Arrays.stream(array);
    }

    static <T> Stream<T> of(Enumeration<T> enumeration){
        return Collections.list(enumeration).stream();
    }

    static <T> Stream<T> of(Iterable<T> iterable){
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    static <T> Stream<T> of(Iterator<T> iterator){
        return of(() -> iterator);
    }

    static <T extends Enum<T>> Stream<T> of(Class<T> e){
        return of(e.getEnumConstants()).map(obj -> (T) obj);
    }

    static <T,U> Stream<Map.Entry<T,U>> of(Map<T,U> map){
        return map.entrySet().stream();
    }

    static Stream<Node> of(NodeList list){
        return IntStream.range(0, list.getLength()).mapToObj(list::item);
    }

    static <T> Stream<T> of(ResultSet set, Function<ResultSet, T> mapper, Consumer<Exception> exceptionConsumer){
        return of(new ResultSetIterable<T>(set, mapper, exceptionConsumer));
    }


}
