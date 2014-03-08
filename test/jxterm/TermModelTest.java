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
public class TermModelTest {

    public TermModelTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCursorStart() {
        TermModel termModel = new TermModel(80, 25, 100);
        assertEquals(1, termModel.getCursorX());
        assertEquals(1, termModel.getCursorY());
    }

    @Test
    public void testSizeStart() {
        TermModel termModel = new TermModel(80, 25, 100);
        assertEquals(80, termModel.getWidth());
        assertEquals(25, termModel.getHeight());
    }

    @Test
    public void testWriteText() {
        TermModel termModel = new TermModel(80, 25, 100);
        int curX = termModel.getCursorX();
        int curY = termModel.getCursorY();

        String s = "hi";
        termModel.writeText(s);

        int ncurX = termModel.getCursorX();
        int ncurY = termModel.getCursorY();

        assertEquals(1, ncurY);
        assertEquals(3, ncurX);

        ScreenCell[] line0 = termModel.getLine(0);
        assertEquals((int)'h', line0[0].cp);
        assertEquals((int)'i', line0[1].cp);
    }

    @Test
    public void testConsecutiveWriteText() {
        TermModel termModel = new TermModel(80, 25, 100);
        int curX = termModel.getCursorX();
        int curY = termModel.getCursorY();

        String s = "hi";
        termModel.writeText(s);
        String s2 = "lo";
        termModel.writeText(s2);

        int ncurX = termModel.getCursorX();
        int ncurY = termModel.getCursorY();

        assertEquals(1, ncurY);
        assertEquals(5, ncurX);

        ScreenCell[] line0 = termModel.getLine(0);
        assertEquals((int)'h', line0[0].cp);
        assertEquals((int)'i', line0[1].cp);
        assertEquals((int)'l', line0[2].cp);
        assertEquals((int)'o', line0[3].cp);
    }

    @Test
    public void testWriteAcrossLineEnd() {
        TermModel termModel = new TermModel(4, 25, 100);

        String s = "hellow";
        termModel.writeText(s);

        int ncurX = termModel.getCursorX();
        int ncurY = termModel.getCursorY();

        assertEquals(2, ncurY);
        assertEquals(3, ncurX);

        ScreenCell[] line0 = termModel.getLine(0);
        ScreenCell[] line1 = termModel.getLine(1);
        assertEquals((int)'h', line0[0].cp);
        assertEquals((int)'e', line0[1].cp);
        assertEquals((int)'l', line0[2].cp);
        assertEquals((int)'l', line0[3].cp);
        assertEquals((int)'o', line1[0].cp);
        assertEquals((int)'w', line1[1].cp);
    }

    @Test
    public void testWriteToLineEnd() {
        TermModel termModel = new TermModel(4, 25, 100);

        String s = "hell";
        termModel.writeText(s);

        int ncurX = termModel.getCursorX();
        int ncurY = termModel.getCursorY();

        // the cursor needs to show at position 4
        assertEquals(1, ncurY);
        assertEquals(4, ncurX);
    }

    @Test
    public void testEraseLineRight() {
        TermModel termModel = new TermModel(5, 25, 100);

        String s = "1234";
        termModel.writeText(s);

        termModel.moveCursorTo(1, 2);
        termModel.eraseLineRight();

        ScreenCell[] line0 = termModel.getLine(0);
        assertEquals((int)'1', line0[0].cp);
        assertEquals((int)' ', line0[1].cp);
        assertEquals((int)' ', line0[2].cp);
        assertEquals((int)' ', line0[3].cp);
    }

    @Test
    public void testEraseLineLeft() {
        TermModel termModel = new TermModel(5, 25, 100);

        //X123X
        termModel.moveCursorTo(1, 2);
        termModel.writeText("123");
        termModel.moveCursorTo(1, 3);
        termModel.eraseLineLeft();

        ScreenCell[] line0 = termModel.getLine(0);
        assertEquals((int)' ', line0[1].cp);
        assertEquals((int)' ', line0[2].cp);
        assertEquals((int)'3', line0[3].cp);
    }

    @Test
    public void testEraseLineAll() {
        TermModel termModel = new TermModel(5, 25, 100);

        // "X123X"
        termModel.moveCursorTo(1, 2);
        termModel.writeText("123");
        termModel.moveCursorTo(1, 3);
        termModel.eraseLineAll();

        ScreenCell[] line0 = termModel.getLine(0);
        assertEquals((int)' ', line0[1].cp);
        assertEquals((int)' ', line0[2].cp);
        assertEquals((int)' ', line0[3].cp);
    }

    @Test
    public void testDeleteChars() {
        TermModel termModel = new TermModel(7,25,100);

        termModel.writeText("ABCDEF");
        termModel.moveCursorTo(1, 3);
        termModel.deleteCharacters(2);
        ScreenCell[] line0 = termModel.getLine(0);
        assertEquals((int)'A', line0[0].cp);
        assertEquals((int)'B', line0[1].cp);
        assertEquals((int)'E', line0[2].cp);
        assertEquals((int)'F', line0[3].cp);
        assertEquals((int)' ', line0[4].cp);
        assertEquals((int)' ', line0[5].cp);
        assertEquals((int)' ', line0[6].cp);
    }

    @Test
    public void testDeleteLotsOChars() {
        TermModel termModel = new TermModel(7,25,100);

        termModel.writeText("ABCDEF");
        termModel.moveCursorTo(1, 3);
        termModel.deleteCharacters(999);
        ScreenCell[] line0 = termModel.getLine(0);
        assertEquals((int)'A', line0[0].cp);
        assertEquals((int)'B', line0[1].cp);
        assertEquals((int)' ', line0[2].cp);
        assertEquals((int)' ', line0[3].cp);
        assertEquals((int)' ', line0[4].cp);
        assertEquals((int)' ', line0[5].cp);
        assertEquals((int)' ', line0[6].cp);
    }

    @Test
    public void testNewLineAfterScrollingRegion() {
        TermModel termModel = new TermModel(80, 20, 100);

        termModel.setScrollingRegion(3, 5);
        termModel.moveCursorTo(20, 1);
        assertEquals(20, termModel.getCursorY());
        termModel.writeText("ABCDEF");
        termModel.newLine();
        assertEquals(20, termModel.getCursorY());
        termModel.writeText("GHIJKL");
    }

    @Test
    public void testScrollUp() {
        TermModel termModel = new TermModel(80, 20, 100);

        termModel.setScrollingRegion(3, 5);
        termModel.moveCursorTo(3, 1);
        termModel.writeText("A\nB\nC");
        termModel.scrollUp(2);
        assertEquals('C', termModel.screen[2][0].cp);
        assertEquals(' ', termModel.screen[3][0].cp);
        assertEquals(' ', termModel.screen[4][0].cp);
    }

    @Test
    public void testScrollDown() {
        TermModel termModel = new TermModel(80, 20, 100);

        termModel.setScrollingRegion(3, 5);
        termModel.writeText("X\nY\n");
        termModel.writeText("A\nB\nC");
        termModel.scrollDown(2);
        assertEquals(' ', termModel.screen[2][0].cp);
        assertEquals(' ', termModel.screen[3][0].cp);
        assertEquals('A', termModel.screen[4][0].cp);
    }

    @Test
    public void testScrollDownTop() {
        TermModel termModel = new TermModel(80, 20, 100);

        termModel.setScrollingRegion(1, 19);
        termModel.moveCursorTo(1, 1);
        termModel.writeText("A\nB\nC");
        termModel.scrollDown(2);
        assertEquals(' ', termModel.screen[0][0].cp);
        assertEquals(' ', termModel.screen[1][0].cp);
        assertEquals('A', termModel.screen[2][0].cp);
    }

    @Test
    public void testScrollDownLots() {
        TermModel termModel = new TermModel(80, 20, 100);

        termModel.setScrollingRegion(1, 19);
        termModel.moveCursorTo(1, 1);
        termModel.writeText("A\nB\nC");
        termModel.scrollDown(200);
        assertEquals(' ', termModel.screen[0][0].cp);
        assertEquals(' ', termModel.screen[1][0].cp);
        assertEquals(' ', termModel.screen[2][0].cp);
    }

    @Test
    public void testDeleteLines() {
        TermModel termModel = new TermModel(80, 20, 100);

        termModel.setScrollingRegion(1, 19);
        termModel.writeText("A\nB\nC\nD");
        termModel.moveCursorTo(2, 1);
        termModel.deleteLines(2);

        assertEquals('A', termModel.screen[0][0].cp);
        assertEquals('D', termModel.screen[1][0].cp);
        assertEquals(' ', termModel.screen[2][0].cp);
        assertEquals(' ', termModel.screen[2][0].cp);
    }

}