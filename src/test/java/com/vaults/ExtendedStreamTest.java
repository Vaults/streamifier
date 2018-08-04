package com.vaults;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static com.vaults.ExtendedStream.of;
import static org.junit.Assert.assertEquals;

public class ExtendedStreamTest {

    @Test
    public void streamifyArrayContainsAllOriginalData() {
        Object[] arr = new Object[]{"A", 1, new Exception()};
        HashSet<Object> arrSet = new HashSet<>();
        arrSet.addAll(Arrays.asList(arr));
        Assert.assertEquals(arrSet, ExtendedStream.of(arr).collect(Collectors.toSet()));
    }

    @Test
    public void streamifyEnumerationContainsAllOriginalData() {
        String test_string = "this is a test";
        Enumeration<Object> enumeration = new StringTokenizer(test_string);
        Assert.assertEquals(" " + test_string, ExtendedStream.of(enumeration).map(o -> (String) o).reduce("", (prev, next) -> prev + " " + next));
    }

    @Test
    public void streamifyEnumeration() {

    }

    @Test
    public void streamifyEnum() {

    }
}
