package com.example.main.Support;

import com.example.main.Configuration.ConfigClass;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
public class DateTimeWeekDay implements Serializable {
    private Object dateTime;
    private Object weekDay;

    public DateTimeWeekDay(Instant instant) {
        List<Object> DTWD = ConfigClass.getDateTimeWeekDayFromInstant(instant);
        this.dateTime = DTWD.get(0);
        this.weekDay = DTWD.get(1);
    }
}
