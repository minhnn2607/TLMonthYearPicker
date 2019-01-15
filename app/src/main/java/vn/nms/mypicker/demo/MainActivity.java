package vn.nms.mypicker.demo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vn.nms.mypicker.IMonthYearPickerListener;
import vn.nms.mypicker.TLMonthYearPickerView;

public class MainActivity extends AppCompatActivity {

    TextView tvSelectYear;
    TextView tvSelectMonthYear;
    TLMonthYearPickerView yearPicker;
    TLMonthYearPickerView monthYearPicker;
    String lang = "en";
    Date currentMonthYear;
    Date currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        yearPicker = findViewById(R.id.mpvYear);
        monthYearPicker = findViewById(R.id.mpvMonthYear);

        tvSelectYear = findViewById(R.id.tvSelectYear);
        tvSelectMonthYear = findViewById(R.id.tvSelectMonthYear);

        tvSelectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yearPicker.getVisibility() == View.VISIBLE) {
                    yearPicker.hide();
                } else {
                    yearPicker.show();
                }
            }
        });

        tvSelectMonthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monthYearPicker.getVisibility() == View.VISIBLE) {
                    monthYearPicker.hide();
                } else {
                    monthYearPicker.show();
                }
            }
        });

        yearPicker.setListener(new IMonthYearPickerListener() {
            @Override
            public void didSelectDate(Date date) {
                currentYear = date;
                setYear(date);
            }
        });

        monthYearPicker.setListener(new IMonthYearPickerListener() {
            @Override
            public void didSelectDate(Date date) {
                currentMonthYear = date;
                setMonthYear(date);
            }
        });
    }

    public void setYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        tvSelectYear.setText("" + calendar.get(Calendar.YEAR));
    }

    public void setMonthYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG,
                getResources().getConfiguration().locale);
        tvSelectMonthYear.setText(month + ", " + calendar.get(Calendar.YEAR));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_en:
                setLocale("en");
                updateLang();
                break;
            case R.id.menu_vn:
                setLocale("vi");
                updateLang();
                break;
            default:
                break;
        }
        return true;
    }

    public void setLocale(String lang) {
        this.lang = lang;
        Locale locale;
        locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    public void updateLang() {
        if (currentMonthYear != null) {
            setMonthYear(currentMonthYear);
        }
    }
}
