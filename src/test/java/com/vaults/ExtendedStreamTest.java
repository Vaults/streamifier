package com.vaults;

import com.vaults.test.TestEnum;
import com.vaults.test.TestNodeList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.w3c.dom.Node;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vaults.ExtendedStream.of;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(MockitoJUnitRunner.class)
public class ExtendedStreamTest {

    @Mock
    private Consumer<Exception> mockConsumer;
    @Mock
    private Consumer<SQLException> mockSqlConsumer;
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

    private Set<String> initResultSetMock(ResultSet mock) throws SQLException {
        final Set<String> expected = new HashSet<>();
        Mockito.when(mock.getString(anyInt())).thenAnswer(invocationOnMock -> {
            String ans = UUID.randomUUID().toString();
            expected.add(ans);
            return ans;
        });
        return expected;
    }

    @Test
    public void streamifyArrayContainsAllOriginalData() {
        Object[] arr = testList.toArray();
        assertEquals(testSet(), ExtendedStream.of(arr).collect(Collectors.toSet()));
    }

    @Test
    public void streamifyEnumerationContainsAllOriginalData() {
        String test_string = "this is a test";
        Enumeration<Object> enumeration = new StringTokenizer(test_string);
        String expected = " " + test_string;
        String actual = ExtendedStream.of(enumeration).map(o -> (String) o).reduce("", (prev, next) -> prev + " " + next);
        assertEquals(expected, actual);
    }

    @Test
    public void streamifyIterableContainsAllOriginalData() {
        Iterable<Object> testIterable = this.testList;
        assertEquals(testSet(), of(testIterable).collect(Collectors.toSet()));
    }

    @Test
    public void streamifyIteratorContainsAllOriginalData() {
        assertEquals(testSet(), of(testList.iterator()).collect(Collectors.toSet()));
    }

    @Test
    public void streamifyEnumContainsAllOriginalData() {
        Set<TestEnum> expected = toSet(Arrays.asList(TestEnum.A, TestEnum.B, TestEnum.C));
        Set<TestEnum> actual = of(TestEnum.class).collect(Collectors.toSet());
        assertEquals(expected, actual);
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
        assertEquals(expected, actual);
    }

    @Test
    public void streamifyNodeListContainsAllOriginalData() {
        TestNodeList testNodeList = new TestNodeList(1000);
        Set<Node> expected = toSet(testNodeList.getNodeList());
        Set<Node> actual = of(testNodeList).collect(Collectors.toSet());
        assertEquals(expected, actual);
    }

    @Test
    public void streamifyResultSetContainsAllOriginalData() throws SQLException {
        ResultSet mock = Mockito.mock(ResultSet.class);
        Mockito.when(mock.next()).thenAnswer(new Answer<Object>() {
            private int count = 0;

            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                count++;
                return count < 5;
            }
        });
        final Set<String> expected = initResultSetMock(mock);

        assertEquals(expected, of(mock, rs -> rs.getString(1), mockConsumer).collect(Collectors.toSet()));

    }

    @Test
    public void streamifyResultSetThrowsExceptionOnInitialization() throws SQLException {
        ResultSet mock = Mockito.mock(ResultSet.class);
        Mockito.when(mock.next()).thenThrow(SQLException.class);

        Set<String> actual = of(mock, rs -> rs.getString(1), mockConsumer, mockSqlConsumer).collect(Collectors.toSet());
        assertTrue(actual.isEmpty());

        Mockito.verify(mockSqlConsumer, Mockito.times(1)).accept(any());
        Mockito.verify(mockConsumer, Mockito.times(0)).accept(any());
    }

    @Test
    public void streamifyResultSetThrowsExceptionOnNext() throws SQLException {
        ResultSet mock = Mockito.mock(ResultSet.class);
        Answer<Boolean> booleanAnswer = new Answer<Boolean>() {
            private boolean init = false;

            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (!init) {
                    init = true;
                    return true;
                }
                throw new SQLException();
            }
        };
        initResultSetMock(mock);
        Mockito.when(mock.next()).thenAnswer(booleanAnswer);


        Set<String> actual = of(mock, rs -> rs.getString(1), mockConsumer, mockSqlConsumer).collect(Collectors.toSet());
        assertEquals(1, actual.size());

        Mockito.verify(mockSqlConsumer, Mockito.times(1)).accept(any());
        Mockito.verify(mockConsumer, Mockito.times(0)).accept(any());
    }

    @Test
    public void streamifyResultSetMapperThrowsException() throws SQLException {
        ResultSet mock = Mockito.mock(ResultSet.class);
        Mockito.when(mock.next()).thenReturn(true);

        Set<Object> actual = of(mock, rs -> {throw new Exception();}, mockConsumer).collect(Collectors.toSet());

        assertEquals(1, actual.size());
        assertNull(actual.toArray()[0]);

        Mockito.verify(mockSqlConsumer, Mockito.times(0)).accept(any());
        Mockito.verify(mockConsumer, Mockito.times(1)).accept(any());
    }



}
