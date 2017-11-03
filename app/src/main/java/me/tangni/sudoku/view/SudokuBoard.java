package me.tangni.sudoku.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;

import me.tangni.sudoku.R;
import me.tangni.sudoku.game.SudokuGame;
import me.tangni.sudoku.Utils.TLog;
import me.tangni.sudoku.game.Cell;
import me.tangni.sudoku.game.SudokuGameListener;

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
    private int numColor;
    private int invalidNumColor;
    private int highlightNumColor;
    private int fixedCellBgColor;
    private int normalCellBgColor;
    private int highlightRowAndColumnCoverColor;
    private int selectedCellCoverColor;

    private Paint boarderPaint;                   // 边框
    private Paint inBoarderPaint;                 // 线
    private Paint linePaint;                      // 细线
    private Paint normalCellBgPaint;                      //
    private Paint fixedCellBgPaint;                      //
    private Paint highlightRowAndColumnCoverPaint;     // 手指按下时的行列 Cover
    private Paint selectedCellCoverPaint;              // 选中的Cell Cover
    private TextPaint cellNumberPaint;            // 数字
    private TextPaint invalidNumPaint;            // 不可用数字
    private TextPaint highlightNumPaint;          // 高亮数字
    private TextPaint cellCandidatesPaint;        // 候选数字

    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int bottom;
    private int right;
    private float left1, left2, top1, top2, vLeft1, vLeft2, vLeft4, vLeft5, vLeft7, vLeft8, hTop1, hTop2, hTop4, hTop5, hTop7, hTop8;
    private float[] cellLefts, cellTops;

    private int numberLeft, numberTop, candidateLeft, candidateTop;
    private int contentWidth, contentHeight;

    private Cell[][] cells;

    private SudokuGameListener listener;
    private int selectedRow = -1, selectedColumn = -1;

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
        boarderWidth = (int) (3 * density + 0.5f);
        inBoarderWidth = (int) (3 * density + 0.5f);
        lineWidth = (int) (1 * density + 0.5f);

        boarderColor = Color.BLACK;
        numColor = Color.WHITE;
        invalidNumColor = Color.RED;
        highlightNumColor = Color.GREEN;
        selectedCellCoverColor = Color.parseColor("#60459b6f");
        highlightRowAndColumnCoverColor = Color.parseColor("#600000ff");
        fixedCellBgColor = Color.parseColor("#5e6063");
        normalCellBgColor = Color.parseColor("#898a8c");

        boarderPaint = new Paint();
        boarderPaint.setColor(boarderColor);
        boarderPaint.setStyle(Paint.Style.FILL);
        boarderPaint.setStrokeWidth(boarderWidth);

        inBoarderPaint = new Paint();
        inBoarderPaint.setColor(boarderColor);
        inBoarderPaint.setStyle(Paint.Style.FILL);
        inBoarderPaint.setStrokeWidth(inBoarderWidth);

        linePaint = new Paint();
        linePaint.setColor(boarderColor);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(lineWidth);

        normalCellBgPaint = new Paint();
        normalCellBgPaint.setColor(normalCellBgColor);
        normalCellBgPaint.setStyle(Paint.Style.FILL);

        fixedCellBgPaint = new Paint();
        fixedCellBgPaint.setColor(fixedCellBgColor);
        fixedCellBgPaint.setStyle(Paint.Style.FILL);

        highlightRowAndColumnCoverPaint = new Paint();
        highlightRowAndColumnCoverPaint.setColor(highlightRowAndColumnCoverColor);
        highlightRowAndColumnCoverPaint.setStyle(Paint.Style.FILL);

        selectedCellCoverPaint = new Paint();
        selectedCellCoverPaint.setColor(selectedCellCoverColor);
        selectedCellCoverPaint.setStyle(Paint.Style.FILL);

        selectedCellCoverPaint = new Paint();
        selectedCellCoverPaint.setColor(selectedCellCoverColor);
        selectedCellCoverPaint.setStyle(Paint.Style.FILL);

        cellNumberPaint = new TextPaint();
        cellNumberPaint.setAntiAlias(true);
        cellNumberPaint.setColor(numColor);

        invalidNumPaint = new TextPaint();
        invalidNumPaint.setAntiAlias(true);
        invalidNumPaint.setColor(invalidNumColor);

        highlightNumPaint = new TextPaint();
        highlightNumPaint.setAntiAlias(true);
        highlightNumPaint.setColor(highlightNumColor);

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
        cellNumberPaint.setTextSize(cellTextSize);
        invalidNumPaint.setTextSize(cellTextSize);
        highlightNumPaint.setTextSize(cellTextSize);
        cellCandidatesPaint.setTextSize(cellHeight / 3.3f);

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

    public void setCellValue(int row, int column, int value) {
        if (row < 0 || row > 8 || column < 0 || column > 8 || value < 0 || value > 9) {
            return;
        }

        Cell cell = cells[row][column];
        boolean invalid = false;
        if (!cell.isFixed() && cell.isSelected()) {
            cell.setValue(value);
            if (isInvalid(row, column, value)) {
                invalid = true;
                cell.setInvalid();
            } else {
                cell.clearInvalid();
            }
        }

//        if (invalid) {
            invalidate();
//        } else {
//
//        }

        checkComplete();
    }

    private void checkComplete() {
        if (listener != null && game.isSolved(cells)) {
            listener.onGameSolved();
        }
    }

    private boolean isInvalid(int row, int column, int value) {
        return !game.isSafe(cells, row, column, value);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (game != null && game.isStarted()) {
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
        Cell cell = getTouchedCell(x, y);
        if (cell == null) {
            return;
        }

        int row = cell.getRow(), column = cell.getColumn();

        TLog.d(TAG, "handleTouchDown, row: " + row + ", column: " + column);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].clearSelected();
                if (i == row || j == column) {
                    cells[i][j].setHighlighted();
                }
            }
        }

        cell.setSelected();
        selectedRow = row;
        selectedColumn = column;

        invalidate((int) (cellWidth * column), paddingTop, (int) (cellWidth * (column + 1)), bottom);
        invalidate(paddingLeft, (int) (cellHeight * row), right, (int) (cellHeight * (row + 1)));
    }

    private void handleTouchUp(float x, float y) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].clearHighlight();
            }
        }

        invalidate();
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

        drawCells(canvas);

    }

    private void drawCell(Canvas canvas, int row, int column, Cell cell) {
        float cellLeft = cellLefts[column];
        float cellTop = cellTops[row];
        float x = cellLeft + numberLeft;
        float numberAscent = cellNumberPaint.ascent();
        float y = cellTop + numberTop - numberAscent;


        // draw bg
        if (cell.isFixed()) {
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
                if (cell.isInvalid()) {
                    canvas.drawText(String.valueOf(value), x, y, invalidNumPaint);
                } else if (cell.isHighlighted() || cell.isSelected()) {
                    canvas.drawText(String.valueOf(value), x, y, highlightNumPaint);
                } else if (cell.isFixed()) {
                    canvas.drawText(String.valueOf(value), x, y, cellNumberPaint);
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
        for (int candidate : candidates) {
            float x = cellLeft + (candidate - 1) * txtSize;
            float y = cellTop + (candidate - 1) * txtSize;
            canvas.drawText(String.valueOf(candidate), x, y, cellCandidatesPaint);
        }
    }

    private void drawCells(Canvas canvas) {
        int state = SudokuGame.GAME_STATE_EMPTY;
        if (game != null) {
            state = game.gameState();
        }

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
    public void setCurCellValue(int value) {
        setCellValue(selectedRow, selectedColumn, value);
    }

    public void setListener(SudokuGameListener listener) {
        this.listener = listener;
    }

    private void initCells(int[][] puzzle) {
        for (int i = 0; i < 9; i++) {
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
