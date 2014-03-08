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
import java.io.UnsupportedEncodingException;
import java.util.Queue;

/**
 *
 * @author brain
 */
public class XTermEmulator extends Emulator {

    /*
     * infocmp xterm
#       Reconstructed via infocmp from file: /lib/terminfo/x/xterm
xterm|X11 terminal emulator,
        am, bce, km, mc5i, mir, msgr, npc, xenl,
        colors#8, cols#80, it#8, lines#24, pairs#64,
        acsc=``aaffggiijjkkllmmnnooppqqrrssttuuvvwwxxyyzz{{||}}~~,

        bel=^G, blink=\E[5m, bold=\E[1m, cbt=\E[Z, civis=\E[?25l,
        clear=\E[H\E[2J, cnorm=\E[?12l\E[?25h, cr=^M,
        csr=\E[%i%p1%d;%p2%dr, cub=\E[%p1%dD, cub1=^H,
        cud=\E[%p1%dB, cud1=^J, cuf=\E[%p1%dC, cuf1=\E[C,
        cup=\E[%i%p1%d;%p2%dH, cuu=\E[%p1%dA, cuu1=\E[A,
        cvvis=\E[?12;25h, dch=\E[%p1%dP, dch1=\E[P, dl=\E[%p1%dM,
        dl1=\E[M, ech=\E[%p1%dX, ed=\E[J, el=\E[K, el1=\E[1K,
        flash=\E[?5h$<100/>\E[?5l, home=\E[H, hpa=\E[%i%p1%dG,
        ht=^I, hts=\EH, ich=\E[%p1%d@, il=\E[%p1%dL, il1=\E[L,
        ind=^J, indn=\E[%p1%dS, invis=\E[8m,
        is2=\E[!p\E[?3;4l\E[4l\E>, kDC=\E[3;2~, kEND=\E[1;2F,
        kHOM=\E[1;2H, kIC=\E[2;2~, kLFT=\E[1;2D, kNXT=\E[6;2~,
        kPRV=\E[5;2~, kRIT=\E[1;2C, kb2=\EOE, kbs=\177, kcbt=\E[Z,
        kcub1=\EOD, kcud1=\EOB, kcuf1=\EOC, kcuu1=\EOA,
        kdch1=\E[3~, kend=\EOF, kent=\EOM, kf1=\EOP, kf10=\E[21~,
        kf11=\E[23~, kf12=\E[24~, kf13=\EO2P, kf14=\EO2Q,
        kf15=\EO2R, kf16=\EO2S, kf17=\E[15;2~, kf18=\E[17;2~,
        kf19=\E[18;2~, kf2=\EOQ, kf20=\E[19;2~, kf21=\E[20;2~,
        kf22=\E[21;2~, kf23=\E[23;2~, kf24=\E[24;2~, kf25=\EO5P,
        kf26=\EO5Q, kf27=\EO5R, kf28=\EO5S, kf29=\E[15;5~,
        kf3=\EOR, kf30=\E[17;5~, kf31=\E[18;5~, kf32=\E[19;5~,
        kf33=\E[20;5~, kf34=\E[21;5~, kf35=\E[23;5~,
        kf36=\E[24;5~, kf37=\EO6P, kf38=\EO6Q, kf39=\EO6R,
        kf4=\EOS, kf40=\EO6S, kf41=\E[15;6~, kf42=\E[17;6~,
        kf43=\E[18;6~, kf44=\E[19;6~, kf45=\E[20;6~,
        kf46=\E[21;6~, kf47=\E[23;6~, kf48=\E[24;6~, kf49=\EO3P,
        kf5=\E[15~, kf50=\EO3Q, kf51=\EO3R, kf52=\EO3S,
        kf53=\E[15;3~, kf54=\E[17;3~, kf55=\E[18;3~,
        kf56=\E[19;3~, kf57=\E[20;3~, kf58=\E[21;3~,
        kf59=\E[23;3~, kf6=\E[17~, kf60=\E[24;3~, kf61=\EO4P,
        kf62=\EO4Q, kf63=\EO4R, kf7=\E[18~, kf8=\E[19~, kf9=\E[20~,
        khome=\EOH, kich1=\E[2~, kmous=\E[M, knp=\E[6~, kpp=\E[5~,
        mc0=\E[i, mc4=\E[4i, mc5=\E[5i, meml=\El, memu=\Em,
        op=\E[39;49m, rc=\E8, rev=\E[7m, ri=\EM, rin=\E[%p1%dT,
        rmacs=\E(B, rmam=\E[?7l, rmcup=\E[?1049l, rmir=\E[4l,
        rmkx=\E[?1l\E>, rmso=\E[27m, rmul=\E[24m, rs1=\Ec,
        rs2=\E[!p\E[?3;4l\E[4l\E>, sc=\E7, setab=\E[4%p1%dm,
        setaf=\E[3%p1%dm,
        setb=\E[4%?%p1%{1}%=%t4%e%p1%{3}%=%t6%e%p1%{4}%=%t1%e%p1%{6}%=%t3%e%p1%d%;m,
        setf=\E[3%?%p1%{1}%=%t4%e%p1%{3}%=%t6%e%p1%{4}%=%t1%e%p1%{6}%=%t3%e%p1%d%;m,
        sgr=\E[0%?%p6%t;1%;%?%p2%t;4%;%?%p1%p3%|%t;7%;%?%p4%t;5%;%?%p7%t;8%;m%?%p9%t\E(0%e\E(B%;,
        sgr0=\E[m\E(B, smacs=\E(0, smam=\E[?7h, smcup=\E[?1049h,
        smir=\E[4h, smkx=\E[?1h\E=, smso=\E[7m, smul=\E[4m,
        tbc=\E[3g, u6=\E[%i%d;%dR, u7=\E[6n, u8=\E[?1;2c, u9=\E[c,
        vpa=\E[%i%p1%dd,
     */

    /* Inputs */
    private static final byte[] KF1 = new byte[]{ 0x1b, 'O', 'P' };
    private static final byte[] KF2 = new byte[]{ 0x1b, 'O', 'Q' };
    private static final byte[] KF3 = new byte[]{ 0x1b, 'O', 'R' };
    private static final byte[] KF4 = new byte[]{ 0x1b, 'O', 'S' };
    private static final byte[] KF5 = new byte[]{ 0x1b, '[', '1', '5', '~' };
    private static final byte[] KF6 = new byte[]{ 0x1b, '[', '1', '7', '~' };
    private static final byte[] KF7 = new byte[]{ 0x1b, '[', '1', '8', '~' };
    private static final byte[] KF8 = new byte[]{ 0x1b, '[', '1', '9', '~' };
    private static final byte[] KF9 = new byte[]{ 0x1b, '[', '2', '0', '~' };
    private static final byte[] KF10 = new byte[]{ 0x1b, '[', '2', '1', '~' };
    private static final byte[] KF11 = new byte[]{ 0x1b, '[', '2', '3', '~' };
    private static final byte[] KF12 = new byte[]{ 0x1b, '[', '2', '4', '~' };
    private static final byte[] KBS = new byte[]{ 0x7f };
    private static final byte[] KEY_LEFT  = new byte[]{ 0x1b, 'O', 'D' };
    private static final byte[] KEY_UP    = new byte[]{ 0x1b, 'O', 'A' };
    private static final byte[] KEY_RIGHT = new byte[]{ 0x1b, 'O', 'C' };
    private static final byte[] KEY_DOWN  = new byte[]{ 0x1b, 'O', 'B' };
    private static final byte[] KEY_HOME  = new byte[]{ 0x1b, 'O', 'H' };
    private static final byte[] KEY_END  = new byte[]{ 0x1b, 'O', 'F' };
    private static final byte[] KEY_PAGE_UP  = new byte[]{ 0x1b, '[', '5', '~' };
    private static final byte[] KEY_PAGE_DOWN  = new byte[]{ 0x1b, '[', '6', '~' };
    private static final byte[] KEY_INSERT  = new byte[]{ 0x1b, '[', '2', '~' };
    private static final byte[] KEY_DELETE  = new byte[]{ 0x1b, '[', '3', '~' };

    public byte[] getKF1() {
        return KF1;
    }

    public byte[] getKF2() {
        return KF2;
    }

    public byte[] getKF3() {
        return KF3;
    }

    public byte[] getKF4() {
        return KF4;
    }

    public byte[] getKF5() {
        return KF5;
    }

    public byte[] getKF6() {
        return KF6;
    }

    public byte[] getKF7() {
        return KF7;
    }

    public byte[] getKF8() {
        return KF8;
    }

    public byte[] getKF9() {
        return KF9;
    }

    public byte[] getKF10() {
        return KF10;
    }

    public byte[] getKF11() {
        return KF11;
    }

    public byte[] getKF12() {
        return KF12;
    }

    public void sendKeyBackSpace() {
        sendBytes(KBS);
    }

    public void sendKeyLeftArrow() {
        sendBytes(KEY_LEFT);
    }

    public void sendKeyUpArrow() {
        sendBytes(KEY_UP);
    }

    public void sendKeyRightArrow() {
        sendBytes(KEY_RIGHT);
    }

    public void sendKeyDownArrow() {
        sendBytes(KEY_DOWN);
    }

    public void sendKeyHome(){
        sendBytes(KEY_HOME);
    }
    public void sendKeyEnd(){
        sendBytes(KEY_END);
    }
    public void sendKeyPageUp(){
        sendBytes(KEY_PAGE_UP);
    }
    public void sendKeyPageDown(){
        sendBytes(KEY_PAGE_DOWN);
    }
    public void sendKeyInsert(){
        sendBytes(KEY_INSERT);
    }
    public void sendKeyDelete(){
        sendBytes(KEY_DELETE);
    }


    private void gotEsc() throws IOException {
        int codePoint = readCodePoint();
        switch(codePoint) {
            case 'D': termModel.index(); break;
            case 'E': System.out.println("ESC " + (char)codePoint);break;
            case 'H': System.out.println("ESC " + (char)codePoint);break;
            case 'M': termModel.reverseIndex(); break;
            case 'N': System.out.println("ESC " + (char)codePoint);break;
            case 'O': System.out.println("ESC " + (char)codePoint);break;
            case 'P': System.out.println("ESC " + (char)codePoint);break;
            case 'V': System.out.println("ESC " + (char)codePoint);break;
            case 'W': System.out.println("ESC " + (char)codePoint);break;
            case 'X': System.out.println("ESC " + (char)codePoint);break;
            case 'Z': System.out.println("ESC " + (char)codePoint);break;
            case '[': gotCSI(); break;
            case '\\': System.out.println("ESC " + (char)codePoint);break;
            case ']': gotOSC(); break;
            case '^': System.out.println("ESC " + (char)codePoint);break;
            case '_': System.out.println("ESC " + (char)codePoint);break;

            case ' ': gotEscSpace(); break;
            case '#': gotEscHash(); break;
            case '%': gotEscPercent(); break;
            case '(': desG0CharSet(readCodePoint()); break;
            case ')': desG1CharSet(readCodePoint()); break;
            case '*': desG2CharSet(readCodePoint()); break;
            case '+': desG3CharSet(readCodePoint()); break;

            case '7': break;
            case '8': break;
            case '=': break;
            case '>': break;
            case 'F': break;
            case 'c': break;
            case 'l': break;
            case 'm': break;
            case 'n': break;
            case 'o': break;
            case '|': break;
            case '}': break;
            case '~': break;

        }
    }

    boolean handleSingleChar(int codePoint) throws IOException {
        if(codePoint >= 0x80 && codePoint <= 0x9f) {
            switch(codePoint) {
                case 0x84: break;
                case 0x85: break;
                case 0x88: break;
                case 0x8d: break;
                case 0x8e: break;
                case 0x8f: break;
                case 0x90: break;
                case 0x96: break;
                case 0x97: break;
                case 0x98: break;
                case 0x9a: break;
                case 0x9b: gotCSI(); break;
                case 0x9c: break;
                case 0x9d: gotOSC(); break;
                case 0x9e: break;
                case 0x9f: break;
            }
            return true;
        }

        if(codePoint <= 0x1f) {
            switch(codePoint) {
                case ASCII_BEL: termModel.bell(); break;
                case ASCII_BS: termModel.moveCursorBackward(1); break;
                case ASCII_CR: termModel.carriageReturn(); break;
                case ASCII_ENQ: break;
                case ASCII_FF: break;
                case ASCII_LF: termModel.newLine(); break;
                case ASCII_SO: break;
                case ASCII_TAB: break;
                case ASCII_VT: break;
                case ASCII_SI: break;
                case ASCII_ESC: gotEsc(); break;
            }
            return true;
        }
        return false;
    }

    private void gotCSI() throws IOException {
        int cp;
        StringBuilder paramSB = new StringBuilder();
        StringBuilder interSB = new StringBuilder();

        cp = readCodePoint();
        while (cp >= 0x30 && cp <= 0x3f) {
            paramSB.appendCodePoint(cp);
            cp = readCodePoint();
        }

        while (cp >= 0x20 && cp <= 0x2f) {
            interSB.appendCodePoint(cp);
            cp = readCodePoint();
        }

        String param = paramSB.toString();

        // check final byte
        if(cp >= 0x40 && cp <= 0x7f) {
            switch(cp) {
                //TODO add all the CSI cases
                case 'A': moveCursorUp(param); break;
                case 'B': moveCursorDown(param); break;
                case 'C': moveCursorForward(param); break;
                case 'D': moveCursorBackward(param); break;
                case 'E': moveCursorNextLine(param); break;
                case 'F': moveCursorPrevLine(param); break;
                case 'H': moveCursor(param); break;
                case 'J': eraseInDisplay(param); break;
                case 'K': eraseInLine(param); break;
                case 'L': insertLines(param);  break;
                case 'M': deleteLines(param); break;
                case 'P': deleteCharacters(param); break;
                case 'S': scrollUp(param); break;
                case 'T': scrollDown(param); break;
                case 'm': characterAttributes(param); break;
                case 'r': setScrollingRegion(param); break;
                default: unknownCSI(paramSB.toString(), interSB.toString(), cp);
            }
        }
        //TODO figure out if we need to "un-get" a character
    }

    private void gotOSC() throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        do {
            cp = readCodePoint();
            if(cp == ASCII_BEL || cp == ASCII_ESC) break;
            sb.appendCodePoint(cp);
        }while(true);
        // put back the esc if thats what it was
        // because thats what xterm seems to do
        if(cp == 0x1b) codePointQueue.add(cp);

        String paramString = sb.toString();
        String[] params = paramString.split(";",2);
        if(params.length != 2) return;
        int p1 = getNumericParameter(params[0], 0);
        String p2 = params[1];
        switch(p1) {
            case 0:
                termModel.setIconName(p2);
                termModel.setWindowTitle(p2);
                break;
            case 1: termModel.setIconName(p2); break;
            case 2: termModel.setWindowTitle(p2); break;
            //TODO 4-18
        }
        

    }

    private void gotEscSpace() throws IOException {
        int codePoint = readCodePoint();
        switch(codePoint) {
            case 'F': break;
            case 'G': break;
            case 'L': break;
            case 'M': break;
            case 'N': break;
        }
    }

    private void gotEscHash() throws IOException {
        int codePoint = readCodePoint();
        switch(codePoint) {
            case '3': break;
            case '4': break;
            case '5': break;
            case '6': break;
            case '8': break;
        }
    }

    private void gotEscPercent() throws IOException {
        int codePoint = readCodePoint();
        switch(codePoint) {
            case '@': break;
            case 'G': break;
        }
    }

    private int getNumericParameter(String params, int def) {
        try {
            return Integer.parseInt(params);
        } catch(NumberFormatException e) {
            return def;
        }
    }

    private int[] getNumericParameters(String paramString, int def) {
        String[] paramStrings = paramString.split(";");
        int [] paramInts = new int[paramStrings.length];
        for(int i=0; i < paramInts.length; i++) {
            paramInts[i] = getNumericParameter(paramStrings[i], def);
        }
        return paramInts;
    }

    private void unknownCSI(String param, String interm, int fin) {
        System.out.println("Got CSI " + (char) fin + " param(" + param + ") inter(" + interm + ")");
    }

    private void moveCursor(String params) {
        String[] pa = params.split(";", 2);
        int row = 1;
        if (pa.length >= 1) {
            row = getNumericParameter(pa[0], 1);
        }

        int column = 1;
        if (pa.length >= 2) {
            column = getNumericParameter(pa[1], 1);
        }

        termModel.moveCursorTo(row, column);
    }

    private void moveCursorUp(String params) {
        int p1 = getNumericParameter(params, 1);
        termModel.moveCursorUp(p1);
    }

    private void moveCursorDown(String params) {
        int p1 = getNumericParameter(params, 1);
        termModel.moveCursorDown(p1);
    }

    private void moveCursorForward(String params) {
        int p1 = getNumericParameter(params, 1);
        termModel.moveCursorForward(p1);
    }

    private void moveCursorBackward(String params) {
        int p1 = getNumericParameter(params, 1);
        termModel.moveCursorBackward(p1);
    }

    private void moveCursorNextLine(String params) {
        int p1 = getNumericParameter(params, 1);
        termModel.moveCursorNextLine(p1);
    }

    private void moveCursorPrevLine(String params) {
        int p1 = getNumericParameter(params, 1);
        termModel.moveCursorPrevLine(p1);
    }

    private void eraseInLine(String params) {
        String[] pa = params.split(";", 2);
        int p1 = 0;
        if (pa.length >= 1) {
            p1 = getNumericParameter(pa[0], 0);
        }
        switch(p1) {
            case 0: termModel.eraseLineRight(); break;
            case 1: termModel.eraseLineLeft(); break;
            case 2: termModel.eraseLineAll(); break;
        }
    }

    private void eraseInDisplay(String params) {
        String[] pa = params.split(";", 2);
        int p1 = 0;
        if (pa.length >= 1) {
            p1 = getNumericParameter(pa[0], 0);
        }
        switch(p1) {
            case 0: termModel.eraseBelow(); break;
            case 1: termModel.eraseAbove(); break;
            case 2: termModel.eraseAll(); break;
            //TODO J3 Erase Saved Lines
            default: System.out.println("CSI J (" + params + ")");
        }
    }

    private void deleteCharacters(String params) {
        int p1 = getNumericParameter(params, 1);
        termModel.deleteCharacters(p1);
    }

    private void setScrollingRegion(String paramString) {
        //FIXME defaults
        int[] params = getNumericParameters(paramString, 0);
        if(params.length >= 2) {
            termModel.setScrollingRegion(params[0], params[1]);
        }
    }
    private void characterAttributes(String paramString) {
        int[] params = getNumericParameters(paramString, 0);
        for(int p : params) {
            switch(p) {
                case 0: termModel.resetAttributes(); break;
                case 1: termModel.setBold(true); break;
                case 4: termModel.setUnderline(true); break;
                case 5: termModel.setBlink(true); break;
                case 7: termModel.setInverse(true); break;
                case 8: termModel.setHidden(true); break;
                case 22: termModel.setBold(false); break; //TODO faint?
                case 24: termModel.setUnderline(false); break;
                case 25: termModel.setBlink(false); break;
                case 27: termModel.setInverse(false); break;
                case 28: termModel.setHidden(false); break;
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                    termModel.setForegroundColor(p-30); break;
                case 39: termModel.resetForegroundColor(); break;
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                    termModel.setBackgroundColor(p-40); break;
                case 49: termModel.resetBackgroundColor(); break;
                default:
                    System.out.println("Character attribute: " + p);
            }
        }
    }

    private void desG0CharSet(int csCodePoint) {
        //TODO G0 charset
    }
    private void desG1CharSet(int csCodePoint) {
        //TODO G1 charset
    }
    private void desG2CharSet(int csCodePoint) {
        //TODO G2 charset
    }
    private void desG3CharSet(int csCodePoint) {
        //TODO G3 charset
    }

    private void scrollUp(String param) {
        int p1 = getNumericParameter(param, 1);
        termModel.scrollUp(p1);
    }

    private void scrollDown(String param) {
        int p1 = getNumericParameter(param, 1);
        termModel.scrollDown(p1);
    }

    private void insertLines(String param) {
        int p1 = getNumericParameter(param, 1);
        termModel.insertLines(p1);
    }

    private void deleteLines(String param) {
        int p1 = getNumericParameter(param, 1);
        termModel.deleteLines(p1);
    }

}
