package com.yilnz.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.temporal.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DateUtil {
    public static Date getYesterDayNow(){
        return Date.from(Instant.now().plus(-1, ChronoUnit.DAYS));
    }

    public static Date getYesterDay0Hour(){
        return Date.from(Instant.now().with(new TemporalAdjuster() {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                temporal.with(ChronoField.HOUR_OF_DAY, 0);
                temporal.with(ChronoField.MINUTE_OF_DAY, 0);
                temporal.with(ChronoField.SECOND_OF_DAY, 0);
                temporal.with(ChronoField.MILLI_OF_DAY, 0);
                return temporal;
            }
        }).plus(-1, ChronoUnit.DAYS));
    }

    public static String formatDate(Date date){
        final LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public static List<String> between(String startDate, String endDate){
        DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
        formatterBuilder.append(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DateTimeFormatter formatter = formatterBuilder.toFormatter();
        final LocalDate start = LocalDate.parse(startDate, formatter);
        final LocalDate end = LocalDate.parse(endDate, formatter);
        List<LocalDate> dateList = new ArrayList<>();
        for(LocalDate i = start;i.compareTo(end) <= 0; i = i.plusDays(1)){
            dateList.add(i);
        }
        return dateList.stream().map(LocalDate::toString).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.formatDate(new Date()));
    }

    public static String now() {
        return new DateTimeFormatterBuilder().toString();
    }
}
