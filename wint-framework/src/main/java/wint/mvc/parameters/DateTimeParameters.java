package wint.mvc.parameters;

import wint.lang.convert.ConvertUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

/**
 * Created by songlihuang on 2022/11/19.
 */
public class DateTimeParameters {

    private Parameters parameters;

    public DateTimeParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    private String getString(String name) {
        return parameters.getString(name);
    }

    public LocalDate getLocalDate(String name, LocalDate defaultValue) {
        return ConvertUtil.toLocalDate(getString(name), defaultValue);
    }

    public LocalDate getLocalDate(String name, String format, LocalDate defaultValue) {
        return ConvertUtil.toLocalDate(getString(name), format, defaultValue);
    }

    public LocalDate getLocalDate(String name) {
        return getLocalDate(name, (LocalDate) null);
    }

    public LocalDate getLocalDate(String name, String format) {
        return getLocalDate(name, format, null);
    }


    public LocalDateTime getLocalDateTime(String name) {
        return getLocalDateTime(name, (LocalDateTime) null);
    }

    public LocalDateTime getLocalDateTime(String name, LocalDateTime defaultValue) {
        return ConvertUtil.toLocalDateTime(getString(name), defaultValue);
    }

    public LocalDateTime getLocalDateTime(String name, String format, LocalDateTime defaultValue) {
        return ConvertUtil.toLocalDateTime(getString(name), format, defaultValue);
    }

    public LocalDateTime getLocalDateTime(String name, String format) {
        return getLocalDateTime(name, format, null);
    }


    public LocalTime getLocalTime(String name) {
        return getLocalTime(name, (LocalTime) null);
    }

    public LocalTime getLocalTime(String name, LocalTime defaultValue) {
        return ConvertUtil.toLocalTime(getString(name), defaultValue);
    }

    public LocalTime getLocalTime(String name, String format, LocalTime defaultValue) {
        return ConvertUtil.toLocalTime(getString(name), format, defaultValue);
    }

    public LocalTime getLocalTime(String name, String format) {
        return getLocalTime(name, format, null);
    }

    public YearMonth getYearMonth(String name, YearMonth defaultValue) {
        return ConvertUtil.toYearMonth(getString(name), defaultValue);
    }

    public YearMonth getYearMonth(String name) {
        return getYearMonth(name, (YearMonth) null);
    }

    public YearMonth getYearMonth(String name, String format, YearMonth defaultValue) {
        return ConvertUtil.toYearMonth(getString(name), format, defaultValue);
    }

    public YearMonth getYearMonth(String name, String format) {
        return getYearMonth(name, format, null);
    }

}
