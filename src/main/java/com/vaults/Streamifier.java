package com.vaults;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Streamifier {

    public static <T>Stream<T> streamify(T[] array){
        return Arrays.stream(array);
    }

    public static <T>Stream<T> streamify(Collection<T> collection){
        return collection.stream();
    }

    public static <T>Stream<T> streamify(Enumeration<T> enumeration){
        return Collections.list(enumeration).stream();
    }

    public static <T>Stream<T> streamify(Iterator<T> iterator){
        Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static Stream<Enum> streamify(Enum e){
        return streamify(e.getDeclaringClass().getEnumConstants()).map(obj -> (Enum) obj);
    }

    public static <T,U>Stream<Map.Entry<T,U>> streamify(Map<T,U> map){
        return map.entrySet().stream();
    }

}
