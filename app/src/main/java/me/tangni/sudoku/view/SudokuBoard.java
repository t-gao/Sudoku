package me.tangni.sudoku.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

import me.tangni.sudoku.R;
import me.tangni.sudoku.game.SudokuGame;
import me.tangni.sudoku.util.DrawableUtil;
import me.tangni.sudoku.util.TLog;
import me.tangni.sudoku.game.Cell;

/**
 * @author gaojian
 */
public class SudokuBoard extends View implements ISudokuBoardView {

    private static final String TAG = "SudokuBoard";

    private static final int DEFAULT_BOARD_SIZE = 100;

    private SudokuGame game;

    private int boarderWidth;
    private int inBoarderWidth;
    private int lineWidth;
    private float cellWidth;
    private float cellHeight;

    private int boarderColor;
    private int lineColor;
    private int fixedNumColor;
    private int numColor;
    private int invalidNumColor;
    private int highlightNumColor;
    private int sameCellBgColor;
    private int fixedCellBgColor;
    private int normalCellBgColor;
    private int highlightRowAndColumnCoverColor;
    private int selectedCellCoverColor;

    private Paint boarderPaint;                   // 边框
    private Paint inBoarderPaint;                 // 线
    private Paint linePaint;                      // 细线
    private Paint normalCellBgPaint;                      //
    private Paint fixedCellBgPaint;                      //
    private Paint sameCellBgPaint;                      // marker same 的cell 背景
    private Paint highlightRowAndColumnCoverPaint;     // 手指按下时的行列 Cover
    private Paint selectedCellCoverPaint;              // 选中的Cell Cover
    private Paint pauseBitmapPaint;              // 暂停图标
    private TextPaint fixedNumberPaint;           // 题目固有数字
    private TextPaint cellNumberPaint;            // 数字
    private TextPaint invalidNumPaint;            // 不可用数字
    private TextPaint selectedNumPaint;           // 选中数字
    private TextPaint cellCandidatesPaint;        // 候选数字

    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int bottom;
    private int right;
    private float left1, left2, top1, top2, vLeft1, vLeft2, vLeft4, vLeft5, vLeft7, vLeft8, hTop1, hTop2, hTop4, hTop5, hTop7, hTop8;
    private float[] cellLefts, cellTops;

    private float candidateTextSize;
    private int numberLeft, numberTop, candidateLeft, candidateTop;
    private int contentWidth, contentHeight;

    private Bitmap pauseBm;

    private Cell[][] cells;

    private int selectedRow = -1, selectedColumn = -1;
    private int pausedBmX, pausedBmY, pausedBmWidth, pausedBmHeight;

    public SudokuBoard(Context context) {
        super(context);
        init(null, 0);
    }

    public SudokuBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SudokuBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SudokuBoard, defStyle, 0);

//        mExampleString = a.getString(
//                R.styleable.SudokuBoard_exampleString);
//        mExampleColor = a.getColor(
//                R.styleable.SudokuBoard_exampleColor,
//                mExampleColor);
//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        mExampleDimension = a.getDimension(
//                R.styleable.SudokuBoard_exampleDimension,
//                mExampleDimension);
//
//        if (a.hasValue(R.styleable.SudokuBoard_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(
//                    R.styleable.SudokuBoard_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }

        a.recycle();

        float density = getResources().getDisplayMetrics().density;
        boarderWidth = (int) (2 * density + 0.5f);
        inBoarderWidth = (int) (2 * density + 0.5f);
        lineWidth = (int) (1 * density + 0.5f);

        boarderColor = Color.BLACK;
        lineColor = Color.parseColor("#989898");
        fixedNumColor = Color.BLACK;
        numColor = getResources().getColor(R.color.cell_num_color);//Color.parseColor("#2D52BA");//Color.BLACK;//Color.WHITE;
        invalidNumColor = Color.RED;
        highlightNumColor = numColor;//Color.GREEN;
        selectedCellCoverColor = Color.parseColor("#60459b6f");
        highlightRowAndColumnCoverColor = Color.parseColor("#60a4eaea");
        fixedCellBgColor = Color.WHITE;//getResources().getColor(R.color.fixed_cell_bg);//Color.parseColor("#5e6063");
        normalCellBgColor = Color.WHITE;//Color.parseColor("#898a8c");
        sameCellBgColor = getResources().getColor(R.color.same_cell_bg);//Color.parseColor("#898a8c");

//        pauseBm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_start_big);
        pauseBm = DrawableUtil.getBitmapFromVectorDrawable(getContext(), R.drawable.ic_start_svg);

        boarderPaint = new Paint();
        boarderPaint.setColor(boarderColor);
        boarderPaint.setStyle(Paint.Style.FILL);
        boarderPaint.setStrokeWidth(boarderWidth);

        inBoarderPaint = new Paint();
        inBoarderPaint.setColor(boarderColor);
        inBoarderPaint.setStyle(Paint.Style.FILL);
        inBoarderPaint.setStrokeWidth(inBoarderWidth);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(lineWidth);

        normalCellBgPaint = new Paint();
        normalCellBgPaint.setColor(normalCellBgColor);
        normalCellBgPaint.setStyle(Paint.Style.FILL);

        fixedCellBgPaint = new Paint();
        fixedCellBgPaint.setColor(fixedCellBgColor);
        fixedCellBgPaint.setStyle(Paint.Style.FILL);

        sameCellBgPaint = new Paint();
        sameCellBgPaint.setColor(sameCellBgColor);
        sameCellBgPaint.setStyle(Paint.Style.FILL);

        highlightRowAndColumnCoverPaint = new Paint();
        highlightRowAndColumnCoverPaint.setColor(highlightRowAndColumnCoverColor);
        highlightRowAndColumnCoverPaint.setStyle(Paint.Style.FILL);

        selectedCellCoverPaint = new Paint();
        selectedCellCoverPaint.setColor(selectedCellCoverColor);
        selectedCellCoverPaint.setStyle(Paint.Style.FILL);

        selectedCellCoverPaint = new Paint();
        selectedCellCoverPaint.setColor(selectedCellCoverColor);
        selectedCellCoverPaint.setStyle(Paint.Style.FILL);

        pauseBitmapPaint = new Paint();

        cellNumberPaint = new TextPaint();
        cellNumberPaint.setAntiAlias(true);
        cellNumberPaint.setColor(numColor);

        fixedNumberPaint = new TextPaint();
        fixedNumberPaint.setAntiAlias(true);
        fixedNumberPaint.setColor(fixedNumColor);

        invalidNumPaint = new TextPaint();
        invalidNumPaint.setAntiAlias(true);
        invalidNumPaint.setColor(invalidNumColor);

        selectedNumPaint = new TextPaint();
        selectedNumPaint.setAntiAlias(true);
        selectedNumPaint.setColor(highlightNumColor);

        cellCandidatesPaint = new TextPaint();
        cellCandidatesPaint.setAntiAlias(true);
        cellCandidatesPaint.setColor(numColor);

        cellLefts = new float[9];
        cellTops = new float[9];
        cells = new Cell[9][9];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        TLog.d(TAG, "widthMode=" + getMeasureSpecModeString(widthMode));
        TLog.d(TAG, "widthSize=" + widthSize);
        TLog.d(TAG, "heightMode=" + getMeasureSpecModeString(heightMode));
        TLog.d(TAG, "heightSize=" + heightSize);

        int width = -1, height = -1;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = DEFAULT_BOARD_SIZE;
            if (widthMode == MeasureSpec.AT_MOST && width > widthSize) {
                width = widthSize;
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = DEFAULT_BOARD_SIZE;
            if (heightMode == MeasureSpec.AT_MOST && height > heightSize) {
                height = heightSize;
            }
        }

        if (widthMode != MeasureSpec.EXACTLY) {
            width = height;
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = width;
        }

        if (widthMode == MeasureSpec.AT_MOST && width > widthSize) {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST && height > heightSize) {
            height = heightSize;
        }

        setMeasuredDimension(width, height);

        contentWidth = width - paddingLeft - paddingRight;
        contentHeight = height - paddingTop - paddingBottom;

        right = width - paddingRight;
        bottom = height - paddingBottom;
        cellWidth = (contentWidth - boarderWidth * 2 - inBoarderWidth * 2 - lineWidth * 6) / 9.0f;
        cellHeight = (contentHeight - boarderWidth * 2 - inBoarderWidth * 2 - lineWidth * 6) / 9.0f;

        float cellTextSize = cellHeight * 0.75f;
        candidateTextSize = cellHeight / 3.3f;

        cellNumberPaint.setTextSize(cellTextSize);
        fixedNumberPaint.setTextSize(cellTextSize);
        invalidNumPaint.setTextSize(cellTextSize);
        selectedNumPaint.setTextSize(cellTextSize);
        cellCandidatesPaint.setTextSize(candidateTextSize);

        candidateLeft = (int) (cellCandidatesPaint.measureText("9") / 2);
        candidateTop = (int) candidateTextSize;

        numberLeft = (int) ((cellWidth - cellNumberPaint.measureText("9")) / 2);
        numberTop = (int) ((cellHeight - cellNumberPaint.getTextSize()) / 2);

        left1 = paddingLeft + boarderWidth + lineWidth * 2 + cellWidth * 3;
        left2 = left1 + inBoarderWidth + lineWidth * 2 + cellWidth * 3;

        vLeft1 = paddingLeft + boarderWidth + cellWidth;
        vLeft2 = vLeft1 + lineWidth + cellWidth;
        vLeft4 = left1 + inBoarderWidth + cellWidth;
        vLeft5 = vLeft4 + lineWidth + cellWidth;
        vLeft7 = left2 + inBoarderWidth + cellWidth;
        vLeft8 = vLeft7 + lineWidth + cellWidth;

        top1 = paddingTop + boarderWidth + lineWidth * 2 + cellHeight * 3;
        top2 = top1 + inBoarderWidth + lineWidth * 2 + cellHeight * 3;

        hTop1 = paddingTop + boarderWidth + cellHeight;
        hTop2 = hTop1 + lineWidth + cellHeight;
        hTop4 = top1 + inBoarderWidth + cellHeight;
        hTop5 = hTop4 + lineWidth + cellHeight;
        hTop7 = top2 + inBoarderWidth + cellHeight;
        hTop8 = hTop7 + lineWidth + cellHeight;

        cellLefts[0] = paddingLeft + boarderWidth;
        cellLefts[1] = vLeft1 + lineWidth;
        cellLefts[2] = vLeft2 + lineWidth;
        cellLefts[3] = left1 + inBoarderWidth;
        cellLefts[4] = vLeft4 + lineWidth;
        cellLefts[5] = vLeft5 + lineWidth;
        cellLefts[6] = left2 + inBoarderWidth;
        cellLefts[7] = vLeft7 + lineWidth;
        cellLefts[8] = vLeft8 + lineWidth;

        cellTops[0] = paddingTop + boarderWidth;
        cellTops[1] = hTop1 + lineWidth;
        cellTops[2] = hTop2 + lineWidth;
        cellTops[3] = top1 + inBoarderWidth;
        cellTops[4] = hTop4 + lineWidth;
        cellTops[5] = hTop5 + lineWidth;
        cellTops[6] = top2 + inBoarderWidth;
        cellTops[7] = hTop7 + lineWidth;
        cellTops[8] = hTop8 + lineWidth;
    }

    private String getMeasureSpecModeString(int mode) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return "UNSPECIFIED";
            case MeasureSpec.AT_MOST:
                return "AT_MOST";
            case MeasureSpec.EXACTLY:
                return "EXACTLY";
        }
        return "null";
    }

    private void setCellValue(int row, int column, int value) {
        if (row < 0 || row > 8 || column < 0 || column > 8 || value < 0 || value > 9) {
            return;
        }

        Cell cell = cells[row][column];
//        HashSet<Integer> candidates = cell.getCandidates();
//        boolean valueMode = candidates == null || candidates.isEmpty();

        boolean invalid = false;
        if (!cell.isFixed() && cell.isSelected()) {
            if (game.isPencilMode()) {
                if (value == 0) {
                    cell.clearCandidates();
                    cell.setValue(0);
                } else {
                    if (cell.getValue() != 0) {
                        cell.addCandidate(cell.getValue());
                    }
                    cell.toggleCandidate(value);
                    cell.setValue(0);
                }
            } else {
                cell.clearCandidates();
                cell.setValue(value);
            }


            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (isConflict(cell, i, j)) {
                        cells[i][j].markConflict();
                    } else {
                        cells[i][j].clearMarkerConflict();
                    }

                    if (value != 0 && cells[i][j].getValue() == value) {
                        cells[i][j].markSame();
                    } else {
                        cells[i][j].clearMarkerSame();
                    }

                    if ((i != row || j != column) && !isInvalid(i, j, cells[i][j].getValue())) {
                        cells[i][j].clearInvalid();
                    }
                }
            }

            if (!game.isPencilMode()) {
                if (isInvalid(row, column, value)) {
                    invalid = true;
                    cell.setInvalid();
                } else {
                    cell.clearInvalid();
                }
            }

//            for (int i = 0; i < 9; i++) {
//                for (int j = 0; j < 9; j++) {
//                    cells[i][j].clearHighlight();
//                }
//            }
        }

//        if (invalid) {
            invalidate();
//        } else {
//
//        }

        checkComplete(row, column);
    }

    private boolean isConflict(Cell cell, int i, int j) {
        Cell c = cells[i][j];
        int cellI = cell.getRow(), cellJ = cell.getColumn();
        if (cell.getValue() != 0 && (i != cellI || j != cellJ) && c.getValue() == cell.getValue()) {
            return cellI == i || cellJ == j || isSameBox(cellI, cellJ, i, j);
        }
        return false;
    }

    private void checkComplete(int row, int column) {

        boolean solved = game.isSolved(cells);
        if (solved) {
            game.onGameSolved();
        }
        boolean rowSolved = isRowSolved(row, column);
        boolean columnSolved = isColumnSolved(row, column);
        boolean boxSolved = isBoxSolved(row, column);

        // TODO: 2017/11/4 visual feedback

    }

    private boolean isRowSolved(int i, int j) {
        return false;// TODO: 2017/11/4
    }

    private boolean isColumnSolved(int i, int j) {
        return false;// TODO: 2017/11/4
    }

    private boolean isBoxSolved(int i, int j) {
        return false;// TODO: 2017/11/4
    }

    private boolean isInvalid(int row, int column, int value) {
        return !game.isSafe(cells, row, column, value);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (game != null && (game.isStarted() || game.isPaused())) {
            int action = event.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    float x = event.getX();
                    float y = event.getY();
                    handleTouchDown(x, y);
                    return true;
                case MotionEvent.ACTION_UP:
                    x = event.getX();
                    y = event.getY();
                    handleTouchUp(x, y);
                    return true;
            }
        }

        return super.onTouchEvent(event);
    }

    private void handleTouchDown(float x, float y) {
        if (game.isStarted()) {
            Cell cell = getTouchedCell(x, y);
            if (cell == null) {
                return;
            }

            int row = cell.getRow(), column = cell.getColumn();
            int touchedValue = cell.getValue();

            TLog.d(TAG, "handleTouchDown, row: " + row + ", column: " + column);

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    cells[i][j].clearSelected();
                    cells[i][j].clearHighlight();
                    cells[i][j].clearMarkerSame();
//                cells[i][j].clearMarkerConflict();
                    if (/*touchedValue != 0 && */(i == row || j == column || isSameBox(row, column, i, j))) {
                        cells[i][j].setHighlighted();
                    }

                    if (touchedValue != 0 && cells[i][j].getValue() == touchedValue) {
                        cells[i][j].markSame();
                    }
                }
            }

            cell.setSelected();
            selectedRow = row;
            selectedColumn = column;

            invalidate((int) (cellWidth * column), paddingTop, (int) (cellWidth * (column + 1)), bottom);
            invalidate(paddingLeft, (int) (cellHeight * row), right, (int) (cellHeight * (row + 1)));
        } else if (game.isPaused()) {
            if (x >= pausedBmX && x <= (pausedBmX + pausedBmWidth) && y >= pausedBmY && y <= (pausedBmY + pausedBmHeight)) {
                game.resumeGame();
            }
        }
    }

    private boolean isSameBox(int row, int column, int i, int j) {
        return i / 3 == row / 3 && j / 3 == column / 3;
    }

    private void handleTouchUp(float x, float y) {
//        for (int i = 0; i < 9; i++) {
//            for (int j = 0; j < 9; j++) {
//                cells[i][j].clearHighlight();
//            }
//        }
//
//        invalidate();
    }

    private Cell getTouchedCell(float x, float y) {
        int cellHeight = contentHeight / 9;
        int cellWidth = contentWidth / 9;

        float diffY = y - paddingTop;
        int row = (int) (diffY / cellHeight) + (diffY % cellHeight > 0 ? 1 : 0);
        TLog.d(TAG, "getTouchedCell, row: " + row);
        if (y < ((row + 1) * lineWidth + row * cellHeight)) {
            row -= 1;
        }

        if (row < 0) {
            row = 0;
        } else if (row > 8) {
            row = 8;
        }

        float diffX = x - paddingLeft;
        int column = (int) (diffX / cellWidth) + (diffX % cellWidth > 0 ? 1 : 0);
        TLog.d(TAG, "getTouchedCell, column: " + column);
        if (x < ((column + 1) * lineWidth + column * cellWidth)) {
            column -= 1;
        }

        if (column < 0) {
            column = 0;
        } else if (column > 8) {
            column = 8;
        }
        TLog.d(TAG, "getTouchedCell, row: " + row + ", column: " + column);

        return (row >= 0 && row < 9 && column >= 0 && column < 9) ? cells[row][column] : null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);

        if (game.isStarted() || game.isFinished()) {
            drawCells(canvas);
        } else if (game.isPaused()) {
            drawPaused(canvas);
        }
    }

    private void drawPaused(Canvas canvas) {
        int centerX = contentWidth / 2 + paddingLeft;
        int centerY = contentHeight / 2 + paddingTop;
        pausedBmWidth = pauseBm.getWidth();
        pausedBmHeight = pauseBm.getHeight();
        pausedBmX = centerX - pausedBmWidth / 2;
        pausedBmY = centerY - pausedBmHeight / 2;

        canvas.drawBitmap(pauseBm, pausedBmX, pausedBmY, pauseBitmapPaint);
    }

    private void drawCell(Canvas canvas, int row, int column, Cell cell) {
        float cellLeft = cellLefts[column];
        float cellTop = cellTops[row];
        float x = cellLeft + numberLeft;
        float numberAscent = cellNumberPaint.ascent();
        float y = cellTop + numberTop - numberAscent;

        // draw bg
        if (cell.isMarkedSame()) {
            canvas.drawRect(cellLeft, cellTop, cellLeft + cellWidth, cellTop + cellHeight, sameCellBgPaint);
        } else if (cell.isFixed()) {
            canvas.drawRect(cellLeft, cellTop, cellLeft + cellWidth, cellTop + cellHeight, fixedCellBgPaint);
        } else {
            canvas.drawRect(cellLeft, cellTop, cellLeft + cellWidth, cellTop + cellHeight, normalCellBgPaint);
        }

        HashSet<Integer> candidates = cell.getCandidates();
        if (candidates != null && !candidates.isEmpty()) {
            drawCellCandidates(canvas, cell, row, column, candidates);
        } else {
            int value = cell.getValue();
            if (value > 0) {
                // draw text
                if (cell.isInvalid() || cell.isMarkedConflict()) {
                    canvas.drawText(String.valueOf(value), x, y, invalidNumPaint);
                } else if (cell.isSelected() /* || cell.isMarkedSame()*/) { // marker same 的 不再用不同字体色，改用背景色标示
                    canvas.drawText(String.valueOf(value), x, y, selectedNumPaint);
                } else if (cell.isFixed()) {
                    canvas.drawText(String.valueOf(value), x, y, fixedNumberPaint);
                } else {
                    canvas.drawText(String.valueOf(value), x, y, cellNumberPaint);
                }
            } else {
                canvas.drawText("", x, y, cellNumberPaint);
            }
        }

        // draw cover
        if (cell.isSelected()) {
            canvas.drawRect(cellLeft, cellTop, cellLeft + cellWidth, cellTop + cellHeight, selectedCellCoverPaint);
        } else if (cell.isHighlighted()) {
            canvas.drawRect(cellLeft, cellTop, cellLeft + cellWidth, cellTop + cellHeight, highlightRowAndColumnCoverPaint);
        }
    }

    private void drawCellCandidates(Canvas canvas, Cell cell, int row, int column, HashSet<Integer> candidates) {
        float cellLeft = cellLefts[column] + candidateLeft;
        float cellTop = cellTops[row] + candidateTop;
        float txtSize = cellHeight / 3.3f;
        int i, j;
        float x, y;
        for (int candidate : candidates) {
            i = (candidate - 1) / 3;
            j = (candidate - 1) % 3;
            x = cellLeft + j * txtSize;
            y = cellTop + i * txtSize;
            canvas.drawText(String.valueOf(candidate), x, y, cellCandidatesPaint);
        }
    }

    private void drawCells(Canvas canvas) {
        if (cells != null && cells.length == 9) {
            for (int i = 0; i < 9; i++) {
                if (cells[i] != null && cells[i].length == 9) {
                    for (int j = 0; j < 9; j++) {
                        Cell cell = cells[i][j];
                        if (cell != null) {
                            drawCell(canvas, i, j, cell);
                        }
                    }
                }
            }
        }
    }

    private void drawBoard(Canvas canvas) {
        float halfBoarderWidth = boarderWidth * 0.5f;
        float halfInBoarderWidth = inBoarderWidth * 0.5f;
        float halfLineWidth = lineWidth * 0.5f;

        canvas.drawLine(paddingLeft + halfBoarderWidth, paddingTop, paddingLeft + halfBoarderWidth, bottom, boarderPaint); //left
        canvas.drawLine(paddingLeft, paddingTop + halfBoarderWidth, right, paddingTop + halfBoarderWidth, boarderPaint);   //top
        canvas.drawLine(right - halfBoarderWidth, paddingTop, right - halfBoarderWidth, bottom, boarderPaint);             //right
        canvas.drawLine(paddingLeft, bottom - halfBoarderWidth, right, bottom - halfBoarderWidth, boarderPaint);           //bottom

        canvas.drawLine(left1 + halfInBoarderWidth, paddingTop, left1 + halfInBoarderWidth, bottom, inBoarderPaint);           //vertical inner boarder 1
        canvas.drawLine(left2 + halfInBoarderWidth, paddingTop, left2 + halfInBoarderWidth, bottom, inBoarderPaint);           //vertical inner boarder 2

        canvas.drawLine(vLeft1 + halfLineWidth, paddingTop, vLeft1 + halfLineWidth, bottom, linePaint);           //vertical line 1
        canvas.drawLine(vLeft2 + halfLineWidth, paddingTop, vLeft2 + halfLineWidth, bottom, linePaint);           //vertical line 2
        canvas.drawLine(vLeft4 + halfLineWidth, paddingTop, vLeft4 + halfLineWidth, bottom, linePaint);           //vertical line 4
        canvas.drawLine(vLeft5 + halfLineWidth, paddingTop, vLeft5 + halfLineWidth, bottom, linePaint);           //vertical line 5
        canvas.drawLine(vLeft7 + halfLineWidth, paddingTop, vLeft7 + halfLineWidth, bottom, linePaint);           //vertical line 7
        canvas.drawLine(vLeft8 + halfLineWidth, paddingTop, vLeft8 + halfLineWidth, bottom, linePaint);           //vertical line 8

        canvas.drawLine(paddingLeft, top1 + halfBoarderWidth, right, top1 + halfBoarderWidth, inBoarderPaint);             //horizontal inner boarder 1
        canvas.drawLine(paddingLeft, top2 + halfBoarderWidth, right, top2 + halfBoarderWidth, inBoarderPaint);             //horizontal inner boarder 2

        canvas.drawLine(paddingLeft, hTop1 + halfLineWidth, right, hTop1 + halfLineWidth, linePaint);           //horizontal line 1
        canvas.drawLine(paddingLeft, hTop2 + halfLineWidth, right, hTop2 + halfLineWidth, linePaint);           //horizontal line 2
        canvas.drawLine(paddingLeft, hTop4 + halfLineWidth, right, hTop4 + halfLineWidth, linePaint);           //horizontal line 4
        canvas.drawLine(paddingLeft, hTop5 + halfLineWidth, right, hTop5 + halfLineWidth, linePaint);           //horizontal line 5
        canvas.drawLine(paddingLeft, hTop7 + halfLineWidth, right, hTop7 + halfLineWidth, linePaint);           //horizontal line 7
        canvas.drawLine(paddingLeft, hTop8 + halfLineWidth, right, hTop8 + halfLineWidth, linePaint);           //horizontal line 8
    }

    public void attachGame(SudokuGame game) {
        this.game = game;
        this.game.attach(this);
    }

    public void changeTheme() {
        // TODO: 2017/11/1
    }

    @Override
    public void startGame() {
        initCells(game.getPuzzle());
        invalidate();
    }

    @Override
    public void pauseGame() {
        invalidate();
    }

    @Override
    public void resumeGame() {
        invalidate();
    }

    @Override
    public void setCurCellValue(int value) {
        setCellValue(selectedRow, selectedColumn, value);
    }

    @Override
    public String serialize() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("selected_row", selectedRow);
            jsonObject.putOpt("selected_column", selectedColumn);
            JSONArray cellArray = new JSONArray();
            if (cells != null && cells.length == 9) {
                for (int i = 0; i < 9; i++) {
                    if (cells[i] != null && cells[i].length == 9) {
                        for (int j = 0; j < 9; j++) {
                            Cell cell = cells[i][j];
                            cellArray.put(new JSONObject(cell.serialize()));
                        }
                    }
                }
            }
            jsonObject.putOpt("cells", cellArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public int[][] deserialize(String serialized) {
        int[][] puzzle = null;
        if (!TextUtils.isEmpty(serialized)) {
            try {
                JSONObject object = new JSONObject(serialized);
                selectedRow = object.optInt("selected_row");
                selectedColumn = object.optInt("selected_column");

                JSONArray cellArray = object.optJSONArray("cells");
                int cellLen = cellArray != null ? cellArray.length() : 0;
                if (cellLen > 0) {
                    if (cells == null) {
                        cells = new Cell[9][9];
                    }

                    puzzle = new int[9][9];

                    for (int i = 0; i < cellLen; i++) {
                        JSONObject cellObj = cellArray.optJSONObject(i);
                        int row = i / 9;
                        int column = i % 9;
                        Cell cell = cellObj != null ? Cell.fromJson(cellObj.toString()) : null;
                        if (cell == null) {
                            cell = new Cell(row, column, 0);
                        }

                        puzzle[row][column] = cell.getValue();

                        cells[row][column] = cell;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return puzzle;
    }

    private void initCells(int[][] puzzle) {
        if (puzzle != null && puzzle.length == 9) {
            for (int i = 0; i < 9; i++) {
                if (puzzle[i] != null && puzzle[i].length == 9) {
                    for (int j = 0; j < 9; j++) {
                        int value = puzzle[i][j];
                        Cell cell= new Cell(i, j, value);
                        if (value > 0) {
                            cell.setFixed();
                        }
                        cells[i][j] = cell;
                    }
                }
            }
        }
    }

}
