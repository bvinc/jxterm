/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jxterm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 *
 * @author brain
 */
public abstract class Emulator {

    static final byte ASCII_BEL = 0x07;
    static final byte ASCII_BS = 0x08;
    static final byte ASCII_CR = 0x0d;
    static final byte ASCII_ENQ = 0x05;
    static final byte ASCII_FF = 0x0c;
    static final byte ASCII_LF = 0x0a;
    static final byte ASCII_SO = 0x0e;
    static final byte ASCII_TAB = 0x09;
    static final byte ASCII_VT = 0x0b;
    static final byte ASCII_SI = 0x0f;
    static final byte ASCII_ESC = 0x1b;

    BlockingQueue<byte[]> outQueue = new LinkedBlockingQueue<byte[]>();
    Thread outThread;
    Reader reader;
    StringBuilder newText = new StringBuilder();
    TermModel termModel;


    public void sendBytes(byte[] data) {
        boolean b = outQueue.add(data);
    }

    public void outThread(OutputStream outStream) {
        try {
            while (true) {
                byte[] buffer = outQueue.take();
                outStream.write(buffer);
                outStream.flush();
            }
        } catch (InterruptedException interruptedException) {
        } catch (IOException iOException) {
        }
    }

    public abstract byte[] getKF1();

    public abstract byte[] getKF2();

    public abstract byte[] getKF3();

    public abstract byte[] getKF4();

    public abstract byte[] getKF5();

    public abstract byte[] getKF6();

    public abstract byte[] getKF7();

    public abstract byte[] getKF8();

    public abstract byte[] getKF9();

    public abstract byte[] getKF10();

    public abstract byte[] getKF11();

    public abstract byte[] getKF12();

    public abstract void sendKeyBackSpace();
    public abstract void sendKeyLeftArrow();
    public abstract void sendKeyUpArrow();
    public abstract void sendKeyRightArrow();
    public abstract void sendKeyDownArrow();

    public abstract void sendKeyHome();
    public abstract void sendKeyEnd();
    public abstract void sendKeyPageUp();
    public abstract void sendKeyPageDown();
    public abstract void sendKeyInsert();
    public abstract void sendKeyDelete();



    // use this queue in case we read a codepoint that we want to handle later
    Queue<Integer> codePointQueue = new LinkedList<Integer>();

    int readCodePoint() throws IOException {
        if(!codePointQueue.isEmpty()) return codePointQueue.remove();

        int i = reader.read();
        if (i == -1) {
            // it's pretty convenient to just throw an exception and abort
            throw new IOException("Connection has been closed.");
        }
        char ch = (char) i;

        int uchar;
        if (Character.isHighSurrogate(ch)) {
            char high = ch;
            int lowi = reader.read();
            if (lowi == -1) {
                throw new IOException("Connection has been closed.");
            }
            char low = (char) lowi;

            if (Character.isSurrogatePair(high, low)) {
                uchar = Character.toCodePoint(high, low);
            } else {
                // this shouldn't happen, encoding error!
                uchar = low;
            }
        } else {
            uchar = ch;
        }

        return uchar;
    }

    abstract boolean handleSingleChar(int codePoint) throws IOException;

    public void runEmulator(InputStream in, final OutputStream out, TermModel termModel) {
        this.termModel = termModel;
        // start the outbound thread
        outThread = new Thread(new Runnable() {

            public void run() {
                outThread(out);
            }
        },
                "Data Sender");
        outThread.start();

        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while(true) {

                int codePoint = readCodePoint();

                if (handleSingleChar(codePoint)) continue;

                termModel.writeCodePoint(codePoint);
            }
        } catch(UnsupportedEncodingException e) {
            // should not happen
        } catch(IOException e) {
        }
    }

}
