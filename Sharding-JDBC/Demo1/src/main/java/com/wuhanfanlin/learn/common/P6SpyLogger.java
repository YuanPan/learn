package com.wuhanfanlin.learn.common;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class P6SpyLogger implements MessageFormattingStrategy {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
        if (!"".equals(sql.trim())) {
            return format.format(new Date()) + " | took " + elapsed + "ms |" + category + " | connection " + connectionId + " [" + sql + ";]";
        }
        return "";
    }
}
