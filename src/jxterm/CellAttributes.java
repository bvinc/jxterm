/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jxterm;

/**
 *
 * @author brain
 */
public final class CellAttributes {

    public static final CellAttributes DEFAULT = new CellAttributes();

    private int foreground;
    private int background;
    private boolean foregroundSet;
    private boolean backgroundSet;
    private boolean bold;
    private boolean underline;
    private boolean blink;
    private boolean inverse;
    private boolean hidden;
/*
    public CellAttributes(int foreground,
    int background,
    boolean foregroundSet,
    boolean backgroundSet,
    boolean bold,
    boolean underline,
    boolean blink,
    boolean inverse,
    boolean hidden) {
        this.foreground = foreground;
        this.background = background;
        this.foregroundSet = foregroundSet;
        this.backgroundSet = backgroundSet;
        this.bold = bold;
        this.underline = underline;
        this.blink = blink;
        this.inverse = inverse;
        this.hidden = hidden;
    }
*/
    public CellAttributes() {
        // leave at defaults (false and 0)
    }

    public CellAttributes(CellAttributes that) {
        this.foreground = that.foreground;
        this.background = that.background;
        this.foregroundSet = that.foregroundSet;
        this.backgroundSet = that.backgroundSet;
        this.bold = that.bold;
        this.underline = that.underline;
        this.blink = that.blink;
        this.inverse = that.inverse;
        this.hidden = that.hidden;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof CellAttributes) ) return false;

        CellAttributes c = (CellAttributes) obj;

        return (this.foreground == c.foreground)
                && (this.background == c.background)
                && (this.foregroundSet == c.foregroundSet)
                && (this.backgroundSet == c.backgroundSet)
                && (this.bold == c.bold)
                && (this.underline == c.underline)
                && (this.blink == c.blink)
                && (this.inverse == c.inverse)
                && (this.hidden == c.hidden);
    }

    public CellAttributes reset() {
        return DEFAULT;
    }

    public int getBackground() {
        return background;
    }

    public CellAttributes background(int c) {
        CellAttributes ret = new CellAttributes(this);
        ret.background = c;
        ret.backgroundSet = true;
        return ret;
    }

    public CellAttributes backgroundDefault() {
        CellAttributes ret = new CellAttributes(this);
        ret.background = 0;
        ret.backgroundSet = false;
        return ret;
    }

    public boolean isBlink() {
        return blink;
    }

    public CellAttributes blink(boolean b) {
        CellAttributes ret = new CellAttributes(this);
        ret.blink = b;
        return ret;
    }

    public boolean isBold() {
        return bold;
    }

    public CellAttributes bold(boolean b) {
        CellAttributes ret = new CellAttributes(this);
        ret.bold = b;
        return ret;
    }

    public int getForeground() {
        return foreground;
    }

    public boolean isHidden() {
        return hidden;
    }

    public CellAttributes hidden(boolean b) {
        CellAttributes ret = new CellAttributes(this);
        ret.hidden = b;
        return ret;
    }

    public boolean isInverse() {
        return inverse;
    }

    public CellAttributes inverse(boolean b) {
        CellAttributes ret = new CellAttributes(this);
        ret.inverse = b;
        return ret;
    }

    public boolean isUnderline() {
        return underline;
    }

    public CellAttributes underline(boolean b) {
        CellAttributes ret = new CellAttributes(this);
        ret.underline = b;
        return ret;
    }

    public boolean isBackgroundSet() {
        return backgroundSet;
    }

    public boolean isForegroundSet() {
        return foregroundSet;
    }

    public CellAttributes foreground(int c) {
        CellAttributes ret = new CellAttributes(this);
        ret.foreground = c;
        ret.foregroundSet = true;
        return ret;
    }

    public CellAttributes foregroundDefault() {
        CellAttributes ret = new CellAttributes(this);
        ret.foreground = 0;
        ret.foregroundSet = false;
        return ret;
    }

}
