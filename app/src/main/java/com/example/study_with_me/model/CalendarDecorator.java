package com.example.study_with_me.model;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

public class CalendarDecorator implements DayViewDecorator {
    private HashSet<CalendarDay> dates;

    public CalendarDecorator(Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);
    }

    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, Color.rgb(14, 71, 228)));
        view.addSpan(new ForegroundColorSpan(Color.rgb(95, 136, 250)));
    }

    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }
}
