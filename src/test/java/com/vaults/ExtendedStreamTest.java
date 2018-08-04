package com.vaults;

import com.vaults.test.TestEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vaults.ExtendedStream.of;
import static org.junit.Assert.assertEquals;

public class ExtendedStreamTest {

    private List<Object> testList;

    @Before
    public void setUp(){
        testList = Arrays.asList("A", 1, new Exception());
    }

    private Set<Object> testSet(){
        return toSet(testList);
    }

    private <T> Set<T> toSet(Collection<T> coll){
        HashSet<T> set = new HashSet<>();
        set.addAll(coll);
        return set;
    }

    @Test
    public void streamifyArrayContainsAllOriginalData() {
        Object[] arr = testList.toArray();
        Assert.assertEquals(testSet(), ExtendedStream.of(arr).collect(Collectors.toSet()));
    }

    @Test
    public void streamifyEnumerationContainsAllOriginalData() {
        String test_string = "this is a test";
        Enumeration<Object> enumeration = new StringTokenizer(test_string);
        Assert.assertEquals(" " + test_string, ExtendedStream.of(enumeration).map(o -> (String) o).reduce("", (prev, next) -> prev + " " + next));
    }

    @Test
    public void streamifyIterableContainsAllOriginalData() {
        Assert.assertEquals(testSet(), of((Iterable<Object>) testList).collect(Collectors.toSet()));
    }

    @Test
    public void streamifyIteratorContainsAllOriginalData() {
        Assert.assertEquals(testSet(), of(testList.iterator()).collect(Collectors.toSet()));
    }

    @Test
    public void streamifyEnum() {
        Set<TestEnum> expected = toSet(Arrays.asList(TestEnum.A, TestEnum.B, TestEnum.C));
        Set<TestEnum> actual = of(TestEnum.class).collect(Collectors.toSet());
        Assert.assertEquals(expected, actual);
    }
}
