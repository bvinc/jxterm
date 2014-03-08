/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jxterm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


/**
 *
 * @author brain
 */
@RunWith(JMock.class)
public class XTermEmulatorTest {
    Mockery context = new JUnit4Mockery(){{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    public XTermEmulatorTest() {
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

    private void doTermWithInput(XTermEmulator xterm, TermModel termModel,
            String input) throws IOException {

        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream pin = new PipedInputStream(pout);
        pout.write(input.getBytes("UTF-8"));
        pout.flush();
        pout.close();
        xterm.runEmulator(pin, null, termModel);
    }

    @Test
    public void testFinishStream() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = new TermModel(80, 25, 100);

        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream pin = new PipedInputStream(pout);

        pout.close();
        xterm.runEmulator(pin, null, termModel);
    }

    @Test
    public void testMoveCursorNoParams() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorTo(1, 1);
            oneOf(termModel).moveCursorTo(3, 5);
            oneOf(termModel).moveCursorTo(7, 1);
            oneOf(termModel).moveCursorTo(7, 1);
        }});

        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream pin = new PipedInputStream(pout);

        pout.write(new byte[]{0x1b, '[', 'H'});
        pout.flush();
        pout.close();
        
        xterm.runEmulator(pin, null, termModel);

        pout = new PipedOutputStream();
        pin = new PipedInputStream(pout);
        pout.write(new String("\u001b[3;5H").getBytes("UTF-8"));
        pout.flush();
        pout.close();
        xterm.runEmulator(pin, null, termModel);

        pout = new PipedOutputStream();
        pin = new PipedInputStream(pout);
        pout.write(new String("\u001b[7;H").getBytes("UTF-8"));
        pout.flush();
        pout.close();
        xterm.runEmulator(pin, null, termModel);

        pout = new PipedOutputStream();
        pin = new PipedInputStream(pout);
        pout.write(new String("\u001b[7H").getBytes("UTF-8"));
        pout.flush();
        pout.close();
        xterm.runEmulator(pin, null, termModel);
    }

    @Test
    public void testEraseLineRightDefault() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).eraseLineRight();
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[K"));
    }

    @Test
    public void testEraseLineRight() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).eraseLineRight();
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[0K"));
    }

    @Test
    public void testEraseLineLeft() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).eraseLineLeft();
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[1K"));
    }
    
    @Test
    public void testEraseLineAll() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).eraseLineAll();
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[2K"));
    }

    @Test
    public void testChangeIconNameAndWindowTitle() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).setIconName("hello");
            oneOf(termModel).setWindowTitle("hello");
        }});

        doTermWithInput(xterm, termModel, new String("\u001b]0;hello\u0007"));
    }

    @Test
    public void testBackSpace() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorBackward(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u0008"));
    }

    @Test
    public void testBell() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).bell();
        }});

        doTermWithInput(xterm, termModel, new String("\u0007"));
    }

    @Test
    public void testMoveCursorUp() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorUp(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5A"));
    }

    @Test
    public void testMoveCursorUpDefault() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorUp(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[A"));
    }

    @Test
    public void testMoveCursorDown() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorDown(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5B"));
    }

    @Test
    public void testMoveCursorDownDefault() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorDown(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[B"));
    }

    @Test
    public void testMoveCursorForward() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorForward(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5C"));
    }

    @Test
    public void testMoveCursorForwardDefault() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorForward(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[C"));
    }

    @Test
    public void testMoveCursorBackward() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorBackward(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5D"));
    }

    @Test
    public void testMoveCursorBackwardDefault() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorBackward(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[D"));
    }

    @Test
    public void testMoveCursorNextLine() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorNextLine(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5E"));
    }

    @Test
    public void testMoveCursorNextLineDefault() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorNextLine(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[E"));
    }

    @Test
    public void testMoveCursorPrevLine() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorPrevLine(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5F"));
    }

    @Test
    public void testMoveCursorPrevLineDefault() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).moveCursorPrevLine(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[F"));
    }

    @Test
    public void testDeleteCharacters() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).deleteCharacters(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5P"));
    }

    @Test
    public void testDeleteCharactersDefault() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).deleteCharacters(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[P"));
    }

    @Test
    public void testSetForegroundColor() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).setForegroundColor(0);
            oneOf(termModel).setForegroundColor(4);
            oneOf(termModel).setForegroundColor(7);
            oneOf(termModel).resetForegroundColor();
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[30m" +
                "\u001b[34m\u001b[37m\u001b[39m"));
    }

    @Test
    public void testSetBackgroundColor() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).setBackgroundColor(0);
            oneOf(termModel).setBackgroundColor(4);
            oneOf(termModel).setBackgroundColor(7);
            oneOf(termModel).resetBackgroundColor();
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[40m" +
                "\u001b[44m\u001b[47m\u001b[49m"));
    }

    @Test
    public void testSetBoldAndForeground() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).setBold(true);
            oneOf(termModel).setForegroundColor(3);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[01;33m"));
    }

    @Test
    public void testSetScrollingRegion() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).setScrollingRegion(2,5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[2;5r"));
    }

    @Test
    public void testScrollUp() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).scrollUp(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[S"));
    }

    @Test
    public void testScrollDown() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).scrollDown(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5T"));
    }

    @Test
    public void testScrollUpDefault() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).scrollUp(1);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[S"));
    }

    @Test
    public void testDeleteLines() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).deleteLines(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5M"));
    }

    @Test
    public void testInsertLines() throws IOException {
        XTermEmulator xterm = new XTermEmulator();
        final TermModel termModel = context.mock(TermModel.class);
        context.checking(new Expectations(){{
            oneOf(termModel).insertLines(5);
        }});

        doTermWithInput(xterm, termModel, new String("\u001b[5L"));
    }
}