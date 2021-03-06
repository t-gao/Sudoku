package me.tangni.sudoku.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by gaojian on 2017/11/2.
 */

public class Cell {
    public static final int FLAG_MASK_FIXED = 0x00000001;
    public static final int FLAG_MASK_HIGHLIGHT = 0x00000002;
    public static final int FLAG_MASK_INVALID = 0x00000004;
    public static final int FLAG_MASK_SELECTED = 0x00000008;

    public static final int FLAG_MASK_MARKER_SAME = 0x00000010;
    public static final int FLAG_MASK_MARKER_CONFLICT = 0x00000020;

    private int row, column;
    private int flags = 0;
    private int value = 0;
    private HashSet<Integer> candidates;

    public Cell() {
    }

    public Cell(int row, int column, int value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public Cell(int row, int column, int value, int flags) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.flags = flags;
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

    public void addCandidate(int value) {
        if (candidates == null) {
            candidates = new HashSet<>(9);
        }

        if (!candidates.contains(value)) {
            candidates.add(value);
        }
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

    public void clearCandidates() {
        if (candidates != null) {
            candidates.clear();
        }
    }

    public void markSame() {
        flags |= FLAG_MASK_MARKER_SAME;
    }

    public void clearMarkerSame() {
        flags &= ~FLAG_MASK_MARKER_SAME;
    }

    public void markConflict() {
        flags |= FLAG_MASK_MARKER_CONFLICT;
    }

    public void clearMarkerConflict() {
        flags &= ~FLAG_MASK_MARKER_CONFLICT;
    }

    public boolean isMarkedSame() {
        return (flags & FLAG_MASK_MARKER_SAME) > 0;
    }

    public boolean isMarkedConflict() {
        return (flags & FLAG_MASK_MARKER_CONFLICT) > 0;
    }

    public String serialize() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("row", row);
            jsonObject.putOpt("column", column);
            jsonObject.putOpt("value", value);
            jsonObject.putOpt("flags", flags);

            if (candidates != null && !candidates.isEmpty()) {
                JSONArray candidatesArray = new JSONArray();
                Iterator<Integer> iterator = candidates.iterator();
                while (iterator.hasNext()) {
                    Integer candidate = iterator.next();
                    if (candidate != null) {
                        candidatesArray.put(candidate.intValue());
                    }
                }

                jsonObject.putOpt("candidates", candidatesArray);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static Cell fromJson(String jsonString) {
        Cell cell = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            int row = jsonObject.optInt("row");
            int column = jsonObject.optInt("column");
            int value = jsonObject.optInt("value");
            int flags = jsonObject.optInt("flags");

            cell = new Cell(row, column, value, flags);

            JSONArray candidatesArray = jsonObject.optJSONArray("candidates");
            int candidateLen = candidatesArray == null ? 0 : candidatesArray.length();
            if (candidateLen > 0) {
                for (int i = 0; i < candidateLen; i++) {
                    int candidate = candidatesArray.optInt(i);
                    cell.addCandidate(candidate);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cell;
    }
}
