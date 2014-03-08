/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jxterm;

import com.sun.org.apache.xml.internal.serialize.Encodings;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author brain
 */
public class JXTermPanel extends JPanel implements MouseListener,
        MouseMotionListener, KeyListener, ChangeListener {

    public void frameHasResized() {
        Rectangle r = getBounds();
        Dimension d = getSize();
        System.out.println("Resize");
    }

    static final Color BLACK = new Color(0x00, 0x00, 0x00);
    static final Color RED = new Color(0xbb, 0x00, 0x00);
    static final Color GREEN = new Color(0x00, 0xbb, 0x00);
    static final Color BLUE = new Color(0x00, 0x00, 0xbb);
    static final Color CYAN = new Color(0x00, 0xbb, 0xbb);
    static final Color PURPLE = new Color(0xbb, 0x00, 0xbb);
    static final Color BROWN = new Color(0xbb, 0xbb, 0x00);
    static final Color LIGHT_GRAY = new Color(0xbb, 0xbb, 0xbb);
    static final Color DARK_GRAY = new Color(0x55, 0x55, 0x55);
    static final Color LIGHT_RED = new Color(0xff, 0x55, 0x55);
    static final Color LIGHT_GREEN = new Color(0x55, 0xff, 0x55);
    static final Color LIGHT_BLUE = new Color(0x55, 0x55, 0xff);
    static final Color LIGHT_CYAN = new Color(0x55, 0xff, 0xff);
    static final Color LIGHT_PURPLE = new Color(0xff, 0x55, 0xff);
    static final Color YELLOW = new Color(0xff, 0xff, 0x55);
    static final Color WHITE = new Color(0xff, 0xff, 0xff);

    static final Color CURSOR_BG_COLOR = LIGHT_GREEN;
    static final Color CURSOR_FG_COLOR = BLACK;

    static final Color DEFAULT_FOREGROUND = new Color(0xbb, 0xbb, 0xbb);
    static final Color DEFAULT_BACKGROUND = new Color(0x00, 0x00, 0x00);

    static final Color[] COLORS = {
        new Color(0x00, 0x00, 0x00), //BLACK
        new Color(0xbb, 0x00, 0x00), //RED
        new Color(0x00, 0xbb, 0x00), //GREEN
        new Color(0xbb, 0xbb, 0x00), //YELLOW
        new Color(0x00, 0x00, 0xbb), //BLUE
        new Color(0xbb, 0x00, 0xbb), //PURPLE
        new Color(0x00, 0xbb, 0xbb), //CYAN
        new Color(0xbb, 0xbb, 0xbb), //LIGHT_GRAY
        new Color(0x55, 0x55, 0x55), //DARK_GRAY
        new Color(0xff, 0x55, 0x55), //LIGHT_RED
        new Color(0x55, 0xff, 0x55), //LIGHT_GREEN
        new Color(0xff, 0xff, 0x55), //LIGHT_YELLOW
        new Color(0x55, 0x55, 0xff), //LIGHT_BLUE
        new Color(0xff, 0x55, 0xff), //LIGHT_PURPLE
        new Color(0x55, 0xff, 0xff), //LIGHT_CYAN
        new Color(0xff, 0xff, 0xff), //WHITE
    };

    public void keyTyped(KeyEvent e) {
        try {
            char c = e.getKeyChar();
            byte[] charBytes = new String(new char[]{c}).getBytes("UTF-8");
            // backspace, send 127 for xterm
            switch (c) {
                case KeyEvent.VK_DELETE:
                    emu.sendKeyDelete();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    emu.sendKeyBackSpace();
                    break;
                default:
                    emu.sendBytes(charBytes);
            }
        } catch (UnsupportedEncodingException ex) {
            // won't happen
        }
    }

    private OutputStream output;
    private Emulator emu;
    private TermModel term;
    private int fontWidth;
    private int fontHeight;
    private int fontDescent;

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT: emu.sendKeyLeftArrow(); break;
            case KeyEvent.VK_UP: emu.sendKeyUpArrow(); break;
            case KeyEvent.VK_RIGHT: emu.sendKeyRightArrow(); break;
            case KeyEvent.VK_DOWN: emu.sendKeyDownArrow(); break;
            case KeyEvent.VK_HOME: emu.sendKeyHome(); break;
            case KeyEvent.VK_END: emu.sendKeyEnd(); break;
            case KeyEvent.VK_PAGE_UP: emu.sendKeyPageUp(); break;
            case KeyEvent.VK_PAGE_DOWN: emu.sendKeyPageDown(); break;
            case KeyEvent.VK_INSERT: emu.sendKeyInsert(); break;
            default: System.out.println("keypress event " + e.getKeyCode());
        }
        
    }

    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    public void mousePressed(MouseEvent e) {
        // TODO: should start to highlight
    }

    public void mouseReleased(MouseEvent e) {
        // TODO: should end the hightlight
    }

    public void mouseDragged(MouseEvent e) {
        // TODO: should continue the highlight
    }

    public void mouseMoved(MouseEvent e) {
        // do nothing
    }


    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }


    public void stateChanged(ChangeEvent changeEvent) {
        repaint();
    }

    public JXTermPanel() {
        init();
    }

    public JXTermPanel(OutputStream output, Emulator emu, TermModel term) {
        this.output = output;
        this.emu = emu;
        this.term = term;
        init();
    }

    public void setTermModel(TermModel termModel) {
        this.term = termModel;
        if(this.term != null) {
            term.addChangeListener(this);
        }
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public void setEmulator(Emulator emu) {
        this.emu = emu;
    }

    private void init() {

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(BLACK);
        setForeground(LIGHT_GRAY);
        //setFont(Font.);
        //setFont(new Font("Courier New", Font.PLAIN, 12));
        //setFont(new Font("Monospaced", Font.PLAIN, 14));
        setFont(new Font("Courier New", Font.PLAIN, 14));
        setFocusable(true);

        setFocusTraversalKeysEnabled(false);
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    public int getMaxColumns() {
        return getWidth()/fontWidth;
    }

    public int getMaxRows() {
        return getHeight()/fontHeight;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        FontMetrics fm = getFontMetrics(font);
        fontWidth = fm.charWidth('A');
        fontHeight = fm.getAscent() + fm.getDescent();
        fontDescent = fm.getDescent();

        int a = fm.getAscent();
        int d = fm.getDescent();
        int l = fm.getLeading();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(term.getWidth() * fontWidth,
                term.getHeight() * fontHeight);
    }

    private void drawRun(Graphics g, String run, int startX, int y, CellAttributes attrs) {
        Color bgColor;
        Color fgColor;
        if(attrs.isBackgroundSet()) {
            int bg = attrs.getBackground();
            if(bg < 8 && attrs.isBold()) bg += 8;
            //FIXME extend colors to 256 later
            if(bg > 16) bg = 0;
            bgColor = COLORS[bg];
        } else {
            bgColor = DEFAULT_BACKGROUND;
        }
        //TODO draw background

        if(attrs.isForegroundSet()) {
            int fg = attrs.getForeground();
            if(fg < 8 && attrs.isBold()) fg += 8;
            //FIXME extend colors to 256 later
            if(fg > 16) fg = 0;
            fgColor = COLORS[fg];
        } else {
            fgColor = DEFAULT_FOREGROUND;
        }

        //flip if inverse
        if(attrs.isInverse()) {
            Color tmpColor = bgColor;
            bgColor = fgColor;
            fgColor = tmpColor;
        }

        g.setColor(bgColor);
        g.fillRect(startX*fontWidth,
        y * fontHeight,
        fontWidth * run.length(), fontHeight);

        g.setColor(fgColor);
        g.drawString(run, startX*fontWidth, fontHeight+y*fontHeight - fontDescent);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(term == null) return;

        g.setColor(LIGHT_GRAY);
        //TODO locking on termModel

        for(int y=0; y < term.getHeight(); y++ ) {
            int[] codePoints = new int[term.getWidth()];
            StringBuilder runSB = new StringBuilder(term.getWidth());
            ScreenCell[] cells = term.getLine(term.getLinesProduced() - term.getHeight() + y);
            int drawStartX = 0;
            CellAttributes drawAttr = cells[0].attributes;
            for(int x=0; x < term.getWidth(); x++) {
                if(cells[x].attributes.equals(drawAttr)) {
                    runSB.appendCodePoint(cells[x].cp);
                } else {
                    drawRun(g, runSB.toString(), drawStartX, y, drawAttr);
                    // start a new run
                    drawAttr = cells[x].attributes;
                    drawStartX = x;
                    runSB.setLength(0);
                    runSB.appendCodePoint(cells[x].cp);
                }
            }
            drawRun(g, runSB.toString(), drawStartX, y, drawAttr);
        }

        // Draw cursor
        int curRow = term.getCursorY();
        int curCol = term.getCursorX();
        StringBuilder curCharSB = new StringBuilder();
        curCharSB.appendCodePoint(term.getCodePointAt(curRow, curCol));
        g.setColor(CURSOR_BG_COLOR);
        g.fillRect((curCol-1) * fontWidth,
                (curRow-1) * fontHeight,
                fontWidth, fontHeight);
        g.setColor(CURSOR_FG_COLOR);
        g.drawString(curCharSB.toString(), (curCol-1)*fontWidth,
                fontHeight+(curRow-1)*fontHeight - fontDescent);
    }

}
