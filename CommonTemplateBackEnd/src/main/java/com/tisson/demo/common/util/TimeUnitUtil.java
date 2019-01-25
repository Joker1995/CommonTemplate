package com.tisson.demo.common.util;

import java.util.concurrent.TimeUnit;

/**  
* @Title: TimeUnitUtil.java  
* @Package com.tisson.crawler.common  
* @Description: TODO(用一句话描述该文件做什么)  
* @author Joker1995
* @date 2019年1月7日  
* @version V1.0  
*/
public class TimeUnitUtil {
	/**
     * 时间秒数计算
     *
     * @param timeUnit 单位枚举
     * @param duration 时间量
     * @return 秒数
     */
    public static int getSeconds(TimeUnit timeUnit, int duration) {
        return (int) timeUnit.toSeconds(duration);
    }
    /**
     * 时间毫秒数计算
     *
     * @param timeUnit 单位枚举
     * @param duration 时间量
     * @return 毫秒数
     */
    public static long getMillis(TimeUnit timeUnit, int duration) {
        return timeUnit.toMillis(duration);
    }
}
