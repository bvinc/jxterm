/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jxterm;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author brain
 */
public class ArrayQueueTest {

    public ArrayQueueTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        ArrayQueue<Integer> q = new ArrayQueue<Integer>(-1);
    }

    @Test
    public void testGetCapacity() {
        ArrayQueue<Integer> q = new ArrayQueue<Integer>(69);
        assertEquals(69, q.getCapacity());

        ArrayQueue<Integer> q2 = new ArrayQueue<Integer>(73);
        assertEquals(73, q2.getCapacity());
    }

    @Test
    public void testIsEmpty() {
        ArrayQueue<Integer> q = new ArrayQueue<Integer>(10);
        assertTrue(q.isEmpty());

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOnEmpty() {
        ArrayQueue<Integer> q = new ArrayQueue<Integer>(10);
        q.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOnEmpty() {
        ArrayQueue<Integer> q = new ArrayQueue<Integer>(10);
        q.get(-1);
    }

    @Test
    public void testGetSize() {
        ArrayQueue<Integer> q = new ArrayQueue<Integer>(2);
        assertEquals(0, q.getSize());
        q.add(new Integer(1));
        assertEquals(1, q.getSize());
        q.add(new Integer(2));
        assertEquals(2, q.getSize());
        q.remove();
        assertEquals(1, q.getSize());
        q.remove();
        assertEquals(0, q.getSize());
        q.add(new Integer(3));
        assertEquals(1, q.getSize());
        q.remove();
        assertEquals(0, q.getSize());
        q.add(new Integer(4));
        assertEquals(1, q.getSize());
        q.add(new Integer(5));
        assertEquals(2, q.getSize());
        q.remove();
        assertEquals(1, q.getSize());
        q.remove();
        assertEquals(0, q.getSize());
    }

    @Test
    public void testFullCircle() {
        ArrayQueue<Integer> q = new ArrayQueue<Integer>(2);
        q.add(new Integer(1));
        q.add(new Integer(2));
        assertEquals(new Integer(1), q.remove());
        assertEquals(new Integer(2), q.remove());
        q.add(new Integer(3));
        assertEquals(new Integer(3), q.remove());
        q.add(new Integer(4));
        q.add(new Integer(5));
        assertEquals(new Integer(4), q.remove());
        assertEquals(new Integer(5), q.remove());
        assertTrue(q.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOnFull() {
        ArrayQueue<Integer> q = new ArrayQueue<Integer>(2);
        q.add(new Integer(1));
        q.add(new Integer(2));
        q.add(new Integer(3));
    }
}