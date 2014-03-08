/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jxterm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Represents the model of the screen, backbuffer and cursor
 * @author brain
 */
public class TermModel {

    public long getLinesProduced() {
        return linesProduced;
    }

    // zero-based cursor location
    private int curX;
    private int curY;

    private int width = 80;
    private int height = 24;

    private int scrollYStart;
    private int scrollYEnd;

    private CellAttributes currentAttributes = CellAttributes.DEFAULT;
    private boolean wrapNext = false;

    // This always increments, allowing us to reference specific line numbers,
    // even after scrolling has occurred
    private long linesProduced = 0;

    private Lock mutex = new ReentrantLock();

    private Collection<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private boolean connected = false;

    private String iconName;

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }
    private String windowTitle;

    public Lock getMutex() {
        return mutex;
    }

    public class Position {
        public int y;
        public int x;
    }

    ArrayQueue<ScreenCell[]> backBuffer;
    ScreenCell screen[][];

    public TermModel(int width, int height, int backBufferLines) {
        this.width = width;
        this.height = height;
        scrollYStart = 0;
        scrollYEnd = height - 1;
        screen = new ScreenCell[height][width];
        linesProduced = height;
        backBuffer = new ArrayQueue<ScreenCell[]>(backBufferLines);
        
        for(ScreenCell[] line : screen) {
            for(int c=0; c < width; c++) {
                line[c] = new ScreenCell();
                line[c].attributes = CellAttributes.DEFAULT;
            }
        }
    }

    private void writeCodePointAt(int cp, int r, int c) {
        if(screen[r][c] == null) screen[r][c] = new ScreenCell();
        screen[r][c].cp = cp;
        screen[r][c].attributes = currentAttributes;
    }

    private void moveCell(int r1, int c1, int r2, int c2) {
        screen[r1][c1].cp = screen[r2][c2].cp;
        screen[r1][c1].attributes = screen[r2][c2].attributes;
    }

    public void writeText(String s) {
        for(int i=0; i < s.length();) {
            int cp = s.codePointAt(i);
            writeCodePoint(cp);

            i+= Character.charCount(cp);
        }

        fireChangeEvent(new ChangeEvent(this));
    }

    /**
     * Writes a single unicode code point.  Handles cursor manipulation.
     * @param cp
     */
    public void writeCodePoint(int cp) {
        if(cp == '\n') {
            newLine();
            return;
        }

        if(cp == '\r') {
            carriageReturn();
            return;
        }

        // if the cursor is we need to wrap around, wrap
        if(wrapNext) {
            newLine();
            wrapNext = false;
        }

        if(curY >= height) {
            //TODO move everything up
        }


        if(screen[curY][curX] == null) screen[curY][curX] = new ScreenCell();
        writeCodePointAt(cp, curY, curX);

        if(curX == width -1) {
            wrapNext = true;
        }

        // increase position
        if(curX < width -1) {
            curX++;
        }
        
        fireChangeEvent(new ChangeEvent(this));
    }

    public void newLine() {
        if(curY == scrollYEnd) {
            scrollUp(1);
            curY = scrollYEnd;
            curX = 0;
        } else {
            curY++;
            curX = 0;
            clipCursor();
        }
        wrapNext = false;

        fireChangeEvent(new ChangeEvent(this));
    }

    public void index() {
        newLine();
    }

    public void reverseIndex() {
        if(curY == scrollYStart) {
            scrollDown(1);
            curY = scrollYStart;
            curX = 0;
        } else {
            curY--;
            curX = 0;
            clipCursor();
        }
        wrapNext = false;
    }

    // r1 and r2 are 1-based
    public void setScrollingRegion(int rStart, int rEnd) {
        scrollYStart = rStart - 1;
        scrollYEnd = rEnd - 1;

        if(scrollYStart < 0) scrollYStart = 0;
        if(scrollYEnd >= height) scrollYEnd = height - 1;
    }

    public void scrollUp(int num) {
        scrollUpInRange(scrollYStart, scrollYEnd, num);
    }

    public void scrollDown(int num) {
        scrollDownInRange(scrollYStart, scrollYEnd, num);
    }

    /**
     * Scroll up between startY and endY inclusively
     * @param startY 0-based y
     * @param endY 0-based y
     * @param num number of lines to scroll
     */
    private void scrollUpInRange(int startY, int endY, int num) {
        if(num <= 0) return;

        System.arraycopy(screen, startY + num, screen, startY,
                Math.max(0, endY - startY - num + 1));
        for(int r = Math.max(startY, endY - num + 1); r <= endY; r++) {
            screen[r] = new ScreenCell[width];
            for(int c = 0; c < width; c++) {
                writeCodePointAt(' ', r, c);
            }
        }
    }

    /**
     * Scroll down between startY and endY inclusively
     * @param startY 0-based y
     * @param endY 0-based y
     * @param num number of lines to scroll
     */
    private void scrollDownInRange(int startY, int endY, int num) {
        if(num <= 0) return;

        for(int i=0; i < endY - startY + 1 &&
                endY - i - num >= startY; i++ ) {
            screen[endY - i] = screen[endY - i - num];
        }
        for(int r = startY; r <= Math.min(startY + num - 1, endY); r++) {
            screen[r] = new ScreenCell[width];
            for(int c = 0; c < width; c++) {
                writeCodePointAt(' ', r, c);
            }
        }
    }

    public void insertLines(int num) {
        scrollDownInRange(curY, height - 1, num);
    }

    public void deleteLines(int num) {
        scrollUpInRange(curY, height - 1, num);
    }

    public void setForegroundColor(int color) {
        currentAttributes = currentAttributes.foreground(color);
    }
    
    public void resetForegroundColor() {
        currentAttributes = currentAttributes.foregroundDefault();
    }

    public void setBackgroundColor(int color) {
        currentAttributes = currentAttributes.background(color);
    }

    public void resetBackgroundColor() {
        currentAttributes = currentAttributes.backgroundDefault();
    }

    public void setBold(boolean b) {
        currentAttributes = currentAttributes.bold(b);
    }

    public void setUnderline(boolean b) {
        currentAttributes = currentAttributes.underline(b);
    }

    public void setBlink(boolean b) {
        currentAttributes = currentAttributes.blink(b);
    }

    public void setInverse(boolean b) {
        currentAttributes = currentAttributes.inverse(b);
    }

    public void setHidden(boolean b) {
        currentAttributes = currentAttributes.hidden(b);
    }

    public void resetAttributes() {
        currentAttributes = CellAttributes.DEFAULT;
    }

    /**
     * Move cursor to position.  The cursor will move as far
     * as it can go in each direction, without going off of
     * the screen.
     * @param r 1-based row number
     * @param c 1-based column number
     */
    public void moveCursorTo(int r, int c) {
        curY = r-1;
        curX = c-1;
        clipCursor();
        wrapNext = false;

        fireChangeEvent(new ChangeEvent(this));

    }

    public void moveCursorUp(int num) {
        curY -= num;
        clipCursor();
        wrapNext = false;

        fireChangeEvent(new ChangeEvent(this));
    }

    public void moveCursorDown(int num) {
        curY += num;
        clipCursor();
        wrapNext = false;

        fireChangeEvent(new ChangeEvent(this));
    }

    public void moveCursorForward(int num) {
        curX += num;
        clipCursor();
        wrapNext = false;

        fireChangeEvent(new ChangeEvent(this));
    }

    public void moveCursorBackward(int num) {
        curX -= num;
        clipCursor();
        wrapNext = false;

        fireChangeEvent(new ChangeEvent(this));
    }

    public void moveCursorNextLine(int num) {
        curX = 0;
        curY += num;
        clipCursor();
        wrapNext = false;

        fireChangeEvent(new ChangeEvent(this));
    }

    public void moveCursorPrevLine(int num) {
        curX = 0;
        curY -= num;
        clipCursor();
        wrapNext = false;

        fireChangeEvent(new ChangeEvent(this));
    }

    public void deleteCharacters(int num) {
        if(num <= 0) return;
        ScreenCell[] line = screen[curY];
        
        if(width - curX - num > 0) {
            System.arraycopy(line, curX + num, line, curX, width - curX - num);
        }

        // null out the new spaces in the line
        int startNull = Math.max(width-num, curX);
        for (int c = startNull; c < width; c++) {
            // since we just did arraycopy, we have to null out
            // what was copied
            screen[curY][c] = null;
            writeCodePointAt(' ', curY, c);
        }

        fireChangeEvent(new ChangeEvent(this));
    }

    /**
     * Gets the code point located at (y, x)
     * @param y 1-based y coordinate
     * @param x 1-based x coordinate
     * @return codepoint at (y,x)
     */
    public int getCodePointAt(int y, int x) {
        return screen[y-1][x-1].cp;
    }

    public int getCursorY() {
        return curY+1;
    }

    public int getCursorX() {
        return curX+1;
    }

    private void clipCursor() {
        if(curY < 0) curY = 0;
        if(curY > height - 1) curY = height-1;
        if(curX < 0) curX = 0;
        if(curX > width -1) curX = width -1;
    }

    /**
     * Returns the line of text refered to by lineNumber
     * @param lineNumber 0-based number of the line produced by this terminal
     * @return array of ScreenChar's that represents this line
     */
    public ScreenCell[] getLine(long lineNumber) {
        if (lineNumber < 0 || lineNumber >= linesProduced) {
            throw new IndexOutOfBoundsException(lineNumber +
                    " line is out of bounds out of " + linesProduced);
        }

        if(lineNumber < linesProduced - height) {
            // line is in backbuffer
            Vector<Integer> v = new Vector<Integer>();

            long lineInBackBuffer = (linesProduced - height) - lineNumber - 1;
            if(lineInBackBuffer > Integer.MAX_VALUE) throw new Error("Line backbuffer overflow");
            return backBuffer.get((int) lineInBackBuffer);
        } else {
            // line is on screen
            long screenYIndex = lineNumber - (linesProduced - height);
            // this shouldn't be a problem
            if(screenYIndex > Integer.MAX_VALUE) throw new Error("Line screen overflow");
            return screen[(int) screenYIndex];
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void addChangeListener(ChangeListener changeListener) {
        listeners.add(changeListener);
    }
    
    private void fireChangeEvent(ChangeEvent changeEvent) {
        for(ChangeListener cl : listeners) {
            cl.stateChanged(changeEvent);
        }
    }

    public void setConnected(boolean c) {
        this.connected = c;
        fireChangeEvent(new ChangeEvent(this));
    }

    public void bell() {
        //TODO bell
    }

    /* This may not be very useful
    public void backSpace() {
        if(curX > 0) {
            moveCursorBackward(1);
            writeCodePoint(' ');
            moveCursorBackward(1);
        }
        fireChangeEvent(new ChangeEvent(this));
    }
     */

    public void carriageReturn() {
        curX = 0;
        fireChangeEvent(new ChangeEvent(this));
    }

    public void returnTerminalStatus() {
        //TODO returnTerminalStatus
    }

    public void eraseLineRight() {
        for(int c=curX; c < width; c++) {
            writeCodePointAt(' ', curY, c);
        }
        fireChangeEvent(new ChangeEvent(this));
    }

    public void eraseLineLeft() {
        for(int c=0; c <= curX; c++) {
            writeCodePointAt(' ', curY, c);
        }
        fireChangeEvent(new ChangeEvent(this));
    }

    public void eraseLineAll() {
        for(int c=0; c < width; c++) {
            writeCodePointAt(' ', curY, c);
        }
        fireChangeEvent(new ChangeEvent(this));
    }

    /**
     * Erase where the cursor is, and everything below it on the screen
     */
    public void eraseBelow() {
        for(int r=curY+1; r < height; r++) {
            for(int c=0; c < width; c++) {
                writeCodePointAt(' ', r, c);
            }
        }

        int r=curY;
        for(int c=curX; c < width; c++) {
            writeCodePointAt(' ', r, c);
        }

        fireChangeEvent(new ChangeEvent(this));
    }

    /**
     * Erase where the cursor is, and everything above it on the screen
     */
    public void eraseAbove() {
        for(int r=0; r < curY; r++) {
            for(int c=0; c < width; c++) {
                writeCodePointAt(' ', r, c);
            }
        }

        int r=curY;
        for(int c=0; c <= curX; c++) {
            writeCodePointAt(' ', r, c);
        }

        fireChangeEvent(new ChangeEvent(this));
    }

    /**
     * Erase everything on the screen
     */
    public void eraseAll() {
        for(int r=0; r < height; r++) {
            for(int c=0; c < width; c++) {
                writeCodePointAt(' ', r, c);
            }
        }

        fireChangeEvent(new ChangeEvent(this));
    }
}
