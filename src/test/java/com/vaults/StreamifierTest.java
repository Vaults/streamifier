package com.vaults;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.vaults.Streamifier.streamify;
import static org.junit.Assert.assertEquals;

public class StreamifierTest {

    @Test
    public void streamifyArray() {
        Object[] arr = new Object[]{"A", 1, new Exception()};
        HashSet<Object> arrSet = new HashSet<>();
        arrSet.addAll(Arrays.asList(arr));
        assertEquals(streamify(arr).collect(Collectors.toSet()), arrSet);
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
