package cn.xhmao.libs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import cn.xhmao.libs.drawable.AlphaPatternDrawable;

/**
 * Created by xhmao on 7/16/13.
 */
public class ColorPickerView extends View {
    private Rect mDrawingRect = new Rect();

    private Paint mAlphaPaint = new Paint();
    private AlphaPatternDrawable mAlphaPatternDrawable;
    private int mAlpha = 0xff;
    private float mHue = 360;
    private float mSat = 0;
    private float mVal = 0;

    private OnColorChangedListener mListener;

    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context) {
        float value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
        mAlphaPatternDrawable = new AlphaPatternDrawable((int) value);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        getDrawingRect(mDrawingRect);
        mAlphaPatternDrawable.setBounds(mDrawingRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mAlphaPatternDrawable == null) {
            return;
        }

        final Rect rect = mDrawingRect;

        mAlphaPatternDrawable.draw(canvas);

        LinearGradient alphaShader = new LinearGradient(rect.left, rect.top, rect.right, rect.bottom,
                0, 0xff000000, Shader.TileMode.CLAMP);
        mAlphaPaint.setShader(alphaShader);
        canvas.drawRect(rect, mAlphaPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean update = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                update = moveTrackIfNeeded(event);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                update = moveTrackIfNeeded(event);
                break;
            }
            case MotionEvent.ACTION_UP: {
                update = moveTrackIfNeeded(event);
                break;
            }
            default:
                break;
        }
        if (update) {
            if (mListener != null) {
                mListener.onColorChanged(getColor());
            }

            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean moveTrackIfNeeded(MotionEvent event) {
        mAlpha = pointToAlpha(event.getX());

        return true;
    }

    private int pointToAlpha(float x) {
        final Rect rect = mDrawingRect;
        final int width = rect.width();
        if (x < rect.left) {
            x = 0;
        } else if (x > rect.right) {
            x = width;
        } else {
            x = x - rect.left;
        }

        return (int) (x * 0xff / width);
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        mListener = listener;
    }

    public OnColorChangedListener getOnColorChangedListener() {
        return mListener;
    }

    public void setColor(int color) {
        setColor(color, false);
    }

    public void setColor(int color, boolean callback) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        mAlpha = alpha;
        mHue = hsv[0];
        mSat = hsv[1];
        mVal = hsv[2];

        if (callback && mListener != null) {
            mListener.onColorChanged(color);
        }

        invalidate();
    }

    public int getColor() {
        float[] hsv = {mHue, mSat, mVal};
        return Color.HSVToColor(mAlpha, hsv);
    }
}
