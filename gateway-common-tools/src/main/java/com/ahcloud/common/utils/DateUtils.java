package com.ahcloud.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/18 21:26
 **/
@Slf4j
public class DateUtils {


    /**
     * 字符串转为date
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date, DatePattern pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern.value);
            return sdf.parse(date);
        } catch (ParseException e) {
            log.error("date:" + date + " format error");
            return null;
        }
    }

    /**
     * 字符串转为date
     * @param dateString yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date parse(String dateString) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        return parse(dateString, DatePattern.PATTERN0);
    }

    /**
     * 获取当前时间 字符串
     *
     * @return
     */
    public static String getStrCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PATTERN0.value));
    }

    /**
     * 日期格式化
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, DatePattern.PATTERN0);
    }

    /**
     * 日期格式化
     * @param date
     * @return
     */
    public static String format(Date date, DatePattern pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern.value);
        return sdf.format(date);
    }

    /**
     * 将时间字串转为 LocalDateTime，时间字串的格式请用 pattern 指定
     */
    public static LocalDateTime str2LocalDateTime(String datetimeStr, DatePattern pattern) {
        return LocalDateTime.parse(datetimeStr, DateTimeFormatter.ofPattern(pattern.value));
    }

    /**
     * 将时间字串转为 LocalDate，时间字串的格式请用 pattern 指定
     */
    public static LocalDate str2LocalDate(String datetimeStr, DatePattern pattern) {
        return LocalDate.parse(datetimeStr, DateTimeFormatter.ofPattern(pattern.value));
    }

    /**
     * 将 java.time.LocalDateTime 转为指定格式的时间字串
     */
    public static String localDate2Str(LocalDate localDate, DatePattern pattern) {
        if (localDate == null || pattern == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern(pattern.value).format(localDate);
    }

    /**
     * 将 java.time.LocalDateTime 转为指定格式的时间字串
     */
    public static String localDateTime2Str(LocalDateTime localDateTime, DatePattern pattern) {
        if (localDateTime == null || pattern == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern(pattern.value).format(localDateTime);
    }

    /**
     * 获取utc时间
     * @param dateTime
     * @return
     */
    public static String getUtcTime(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);
        return localDateTime.toInstant(ZoneOffset.UTC).toString();
    }

    /**
     * 校验时间日期是否跨月
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean checkDateTimeIsAcrossMonth(String startTime, String endTime) {
        LocalDateTime startLocalDateTime = DateUtils.str2LocalDateTime(startTime, DatePattern.PATTERN0);
        LocalDateTime endLocalDateTime = DateUtils.str2LocalDateTime(endTime, DatePattern.PATTERN0);

        String start = DateUtils.localDateTime2Str(startLocalDateTime, DatePattern.PATTERN5);
        String end = DateUtils.localDateTime2Str(endLocalDateTime, DatePattern.PATTERN5);
        return StringUtils.equals(start,end);
    }

    /**
     * 校验时间日期是否跨月, 如果不跨月则返回当前日期(yyyyMM)
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Integer checkDateTimeIsAcrossMonthAndReturnDate(String startTime, String endTime) {
        LocalDateTime startLocalDateTime = DateUtils.str2LocalDateTime(startTime, DatePattern.PATTERN0);
        LocalDateTime endLocalDateTime = DateUtils.str2LocalDateTime(endTime, DatePattern.PATTERN0);

        String start = DateUtils.localDateTime2Str(startLocalDateTime, DatePattern.PATTERN5);
        String end = DateUtils.localDateTime2Str(endLocalDateTime, DatePattern.PATTERN5);
        if (StringUtils.equals(start,end)) {
            return null;
        }
        return Integer.valueOf(start);
    }

    /**
     * instant转为date
     * @param instant
     * @return
     */
    public static Date instantToDate(Instant instant) {
        return Date.from(instant);
    }

    /**
     * date转为instant
     * @param date
     * @return
     */
    public static Instant dateToInstant(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant();
    }

    /**
     * 当前日期是否在目标时间之前
     *
     * @param expireTime
     * @return
     */
    public static boolean beforeDate(Date expireTime) {
        Date date = new Date();
        return date.before(expireTime);
    }

    /**
     * 当前日期是否在目标时间之后
     *
     * @param expireTime
     * @return
     */
    public static boolean afterDate(Date expireTime) {
        Date date = new Date();
        return date.after(expireTime);
    }

    /**
     * 获取当前时间戳 (秒)
     * @return
     */
    public static Long getCurrentSeconds() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取当前date日期
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取当前date日期
     * @return
     */
    public static Integer getCurrentDateInt() {
        return Integer.valueOf(format(new Date(), DatePattern.PATTERN2));
    }

    /**
     * 获取上月日期
     * @param date
     * @return
     */
    public static Date getLastMonthDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, -1);
        return c.getTime();
    }

    /**
     * 当前时间 前多少分钟 的时间
     * @param minutes
     * @return
     */
    public static String getDateBeforeMin(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minutesBefore = now.minusMinutes(minutes);
        return localDateTime2Str(minutesBefore, DatePattern.PATTERN0);
    }

    /**
     * 计算两个时间差
     * @param startTime
     * @param endTime
     * @return 秒
     */
    public static Long betweenDuration(String startTime, String endTime) {
        LocalDateTime startLocalDateTime = str2LocalDateTime(startTime, DatePattern.PATTERN0);
        LocalDateTime endLocalDateTime = str2LocalDateTime(endTime, DatePattern.PATTERN0);
        Duration duration = Duration.between(startLocalDateTime, endLocalDateTime);
        return duration.getSeconds();
    }

    /**
     * 计算两个时间差
     * @param startTime
     * @param endTime
     * @return 秒
     */
    public static Long betweenDuration(Date startTime, Date endTime) {
        long endTimes = endTime.getTime();
        long startTimes = startTime.getTime();
        return (endTimes - startTimes) / 1000;
    }


    public enum DatePattern {

        /**
         * date格式
         */

        PATTERN0("yyyy-MM-dd HH:mm:ss"),
        PATTERN1("yyyy年MM月dd日HH时mm分ss秒"),
        PATTERN2("yyyyMMdd"),
        PATTERN3("yyyy-MM-dd"),
        PATTERN4("yyyy/MM/dd HH:mm:ss"),
        PATTERN5("yyyyMM"),
        ;

        private final String value;

        DatePattern(String value) {
            this.value = value;
        }

    }
}
