package vn.nms.mypicker;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Calendar;

import vn.nms.mypicker.loop_view.LoopView;
import vn.nms.mypicker.loop_view.OnItemSelectedListener;

public class TLMonthYearPickerView extends FrameLayout {
    private static final Type DEFAULT_TYPE = Type.YEAR;
    private static final int DEFAULT_MIN_YEAR = 1890;
    private static final int DEFAULT_INIT_MONTH = 2;

    private int minYear;
    private int maxYear;
    private int initYear;
    private int selectMonth;
    private int selectYear;
    private Type type = DEFAULT_TYPE;

    LoopView loopViewYear;
    LoopView loopViewMonth;
    private IMonthYearPickerListener mListener;

    public enum Type {
        YEAR,
        MONTH_AND_YEAR,
    }

    public TLMonthYearPickerView(Context context) {
        super(context);
        init(context, null);
    }

    public TLMonthYearPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TLMonthYearPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.month_year_picker_view, null);
        loopViewYear = view.findViewById(R.id.loopViewYear);
        loopViewMonth = view.findViewById(R.id.loopViewMonth);
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs,
                    R.styleable.TLMonthYearPickerView);
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            minYear = attributes.getInt(R.styleable.TLMonthYearPickerView_myp_min_year, DEFAULT_MIN_YEAR);
            maxYear = attributes.getInt(R.styleable.TLMonthYearPickerView_myp_max_year, currentYear);
            initYear = attributes.getInt(R.styleable.TLMonthYearPickerView_myp_init_year, maxYear);
            if (initYear < minYear || initYear > maxYear) {
                initYear = maxYear;
            }
            if (maxYear <= minYear) maxYear++;
            int typeValue = attributes.getInt(R.styleable.TLMonthYearPickerView_myp_type, 0);
            type = typeValue == 0 ? Type.YEAR : Type.MONTH_AND_YEAR;
            attributes.recycle();
        }

        switch (type) {
            case YEAR:
                loopViewMonth.setVisibility(View.GONE);
                break;
            case MONTH_AND_YEAR:
                loopViewMonth.setVisibility(View.VISIBLE);
                break;
        }

        final ArrayList<String> listYear = new ArrayList<>();
        for (int i = minYear; i <= maxYear; i++) {
            listYear.add("" + i);
        }
        loopViewYear.setNotLoop();
        loopViewYear.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                selectYear = Integer.parseInt(listYear.get(index));
                switch (type) {
                    case YEAR:
                        onSelectDate(selectYear);
                        break;
                    case MONTH_AND_YEAR:
                        onSelectMonth(selectMonth, selectYear);
                        break;
                }
            }
        });

        int indexYear = listYear.indexOf("" + initYear);
        loopViewYear.setItems(listYear);
        loopViewYear.setInitPosition(indexYear);
        selectYear = Integer.parseInt(listYear.get(indexYear));

        if (loopViewMonth.getVisibility() == View.VISIBLE) {
            final ArrayList<String> listMonth = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                listMonth.add("" + i);
            }
            loopViewMonth.setNotLoop();
            loopViewMonth.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    selectMonth = Integer.parseInt(listMonth.get(index));
                    switch (type) {
                        case MONTH_AND_YEAR:
                            onSelectMonth(selectMonth, selectYear);
                            break;
                    }
                }
            });

            loopViewMonth.setItems(listMonth);
            loopViewMonth.setInitPosition(DEFAULT_INIT_MONTH - 1);
            selectMonth = Integer.parseInt(listMonth.get(DEFAULT_INIT_MONTH - 1));
        }

        this.addView(view);
    }

    private void onSelectDate(int year) {
        if (mListener != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            mListener.didSelectDate(calendar.getTime());
        }
    }

    private void onSelectMonth(int month, int year) {
        if (mListener != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, 1);
            mListener.didSelectDate(calendar.getTime());
        }
    }


    public void setListener(IMonthYearPickerListener mListener) {
        this.mListener = mListener;
    }

    public void show() {
        expand(this);
    }

    public void hide() {
        collapse(this);
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);

        ValueAnimator va = ValueAnimator.ofInt(1, targetHeight);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        va.setDuration(300);
        va.setInterpolator(new OvershootInterpolator());
        va.start();
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        ValueAnimator va = ValueAnimator.ofInt(initialHeight, 0);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        va.setDuration(300);
        va.setInterpolator(new DecelerateInterpolator());
        va.start();
    }
}



