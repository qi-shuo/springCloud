package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/30 上午10:20
 */
public class DateUtil {
    /**
     * 获取当前日期,格式:yyyyMMdd
     * @return
     */
    public static String getDate() {
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String formatDate = sdf.format(date);
        return formatDate;
    }
}
