package com.vaults;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.vaults.ExtendedStream.of;
import static org.junit.Assert.assertEquals;

public class ExtendedStream {

    @Test
    public void streamifyArray() {
        Object[] arr = new Object[]{"A", 1, new Exception()};
        HashSet<Object> arrSet = new HashSet<>();
        arrSet.addAll(Arrays.asList(arr));
        assertEquals(of(arr).collect(Collectors.toSet()), arrSet);
    }

    @Test
    public void streamifyCollection() {

    }

    @Test
    public void streamifyEnumeration() {

    }

    @Test
    public void streamifyEnum() {

    }
}
