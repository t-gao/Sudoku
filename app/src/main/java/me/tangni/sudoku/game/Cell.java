package me.tangni.sudoku.game;

import java.util.HashSet;

/**
 * Created by gaojian on 2017/11/2.
 */

public class Cell {
    public static final int FLAG_MASK_FIXED = 0x00000001;
    public static final int FLAG_MASK_HIGHLIGHT = 0x00000002;
    public static final int FLAG_MASK_INVALID = 0x00000004;
    public static final int FLAG_MASK_SELECTED = 0x00000008;

    private int row, column;
    private int flags = 0;
    private int value = 0;
    private HashSet<Integer> candidates;

    public Cell(int row, int column, int value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void clearFlags() {
        flags = 0x0;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addFlag(int flag) {
        flags |= flag;
    }

    public void clearFlag(int flag) {
        flags &= ~flag;
    }

    public boolean isNormal() {
        return flags == 0;
    }

    public void setFixed() {
        flags |= FLAG_MASK_FIXED;
    }

    public boolean isFixed() {
        return (flags & FLAG_MASK_FIXED) > 0;
    }

    public void setInvalid() {
        flags |= FLAG_MASK_INVALID;
    }

    public boolean isInvalid() {
        return (flags & FLAG_MASK_INVALID) > 0;
    }

    public void clearInvalid() {
        flags &= ~FLAG_MASK_INVALID;
    }

    public void setHighlighted() {
        flags |= FLAG_MASK_HIGHLIGHT;
    }

    public boolean isHighlighted() {
        return (flags & FLAG_MASK_HIGHLIGHT) > 0;
    }

    public void clearHighlight() {
        flags &= ~FLAG_MASK_HIGHLIGHT;
    }

    public boolean isSelected() {
        return (flags & FLAG_MASK_SELECTED) > 0;
    }

    public void setSelected() {
        flags |= FLAG_MASK_SELECTED;
    }

    public void clearSelected() {
        flags &= ~FLAG_MASK_SELECTED;
    }

    public java.util.HashSet<Integer> getCandidates() {
        return candidates;
    }

    public void toggleCandidate(int value) {
        if (candidates == null) {
            candidates = new HashSet<>(9);
        }

        if (candidates.contains(value)) {
            candidates.remove(value);
        } else {
            candidates.add(value);
        }
    }

}
