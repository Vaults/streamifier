package com.vaults;

import com.vaults.test.TestEnum;
import com.vaults.test.TestNodeList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vaults.ExtendedStream.of;

public class ExtendedStreamTest {

    private List<Object> testList;

    @Before
    public void setUp() {
        testList = Arrays.asList("A", 1, new Exception());
    }

    private Set<Object> testSet() {
        return toSet(testList);
    }

    private <T> Set<T> toSet(Collection<T> coll) {
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
        String expected = " " + test_string;
        String actual = ExtendedStream.of(enumeration).map(o -> (String) o).reduce("", (prev, next) -> prev + " " + next);
        Assert.assertEquals(expected, actual);
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
    public void streamifyEnumContainsAllOriginalData() {
        Set<TestEnum> expected = toSet(Arrays.asList(TestEnum.A, TestEnum.B, TestEnum.C));
        Set<TestEnum> actual = of(TestEnum.class).collect(Collectors.toSet());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void streamifyMapContainsAllOriginalData() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>() {{
            put(1, 4);
            put(2, 5);
            put(3, 6);
        }};
        Set<Integer> expected = toSet(Arrays.asList(1, 2, 3, 4, 5, 6));
        Set<Integer> actual = of(map).flatMap(entry -> Stream.of(entry.getKey(), entry.getValue())).collect(Collectors.toSet());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void streamifyNodeListContainsAllOriginalData() {
        TestNodeList testNodeList = new TestNodeList(1000);
        Set<Node> expected = toSet(testNodeList.getNodeList());
        Set<Node> actual = of(testNodeList).collect(Collectors.toSet());
        Assert.assertEquals(expected, actual);
    }
}
