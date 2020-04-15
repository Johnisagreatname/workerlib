package yizhit.workerlib.uitls;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class DateUtils {

    public static final String formatPattern = "yyyy-MM-dd";

    public static final String formatPattern_Short = "yyyyMMdd";

    public static final String formatPattern_full = "yyyy-MM-dd HH:mm:ss";

    /**
     * 是否是同一天
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 相差在多少天以内
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2, int day) {
        long l = getTimeSubtract(date1, date2) / 60 / 60 / 24;
        return l <= day && l >= -day;
    }

    /**
     * 根据获取date日期
     * @param strdate stting 类型的日期
     * @param temp  //设置 格式 如"yyyy-MM-dd hh:mm:ss"与日期一致;
     * @return date
     * */
    public static Date getDate(String strdate, String temp) {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat(temp, Locale.CHINESE);// 设定格式
        dateFormat.setLenient(false);// 严格控制输入 比如2010－02－31，根本没有这一天 ，也会认为时间格式不对。
        Date timeDate = null;
        try {
            timeDate = dateFormat.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDate;
    }

    /**
     * 获取string日期
     * @param temp  //设置日期格式 如"yyyy-MM-dd hh:mm:ss";
     * @return String
     * */
    public static String getStringDate(String temp) {
        return getStringDate(new Date(), temp);
    }

    /**
     * 获取string日期
     * @param date
     * @param temp  //设置日期格式 如"yyyy-MM-dd hh:mm:ss";
     * @return String
     * */
    public static String getStringDate(Date date, String temp) {
        SimpleDateFormat formatter = new SimpleDateFormat(temp);
        return formatter.format(date);
    }

    /**
     * 获取当前日期 
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * @Author xieya
     * @Description 获取当前时间
     * @Date 2020/4/8 15:17
     * @param
     * @return java.util.Date
     **/
    public static Date getCurrentDateTime() {
        return new Date();
    }

    /**
     * 获取当前月第一天
     * @return
     */
    public static Date getMonthFirstDay() {
        Calendar c = Calendar.getInstance();
        // 这是已知的日期
        Date d = new Date();
        c.setTime(d);
        c.get(Calendar.YEAR);
        c.get(Calendar.MONTH);
        c.set(Calendar.DAY_OF_MONTH, 1);
        // 1号的日期
        d = c.getTime();
        return d;
    }

    /**
     * 获取制定毫秒数之前的日期 
     * @param timeDiff
     * @return
     */
    public static String getDesignatedDate(long timeDiff) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        long nowTime = System.currentTimeMillis();
        long designTime = nowTime - timeDiff;
        return format.format(designTime);
    }

    /**
     *
     * 获取前几天的日期 
     */
    public static String getPrefixDate(String count) {
        Calendar cal = Calendar.getInstance();
        int day = 0 - Integer.parseInt(count);
        cal.add(Calendar.DATE, day);   // int amount   代表天数
        Date datNew = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(datNew);
    }

    /**
     * 日期转换成字符串 
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(date);
    }

    /**
     * 日期转换成字符串 
     * @param date
     * @param formatprn 格式
     * @return
     */
    public static String dateToString(Date date, String formatprn) {
        SimpleDateFormat format = new SimpleDateFormat(formatprn);
        return format.format(date);
    }

    /**
     * 字符串转换日期 
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        //str =  " 2008-07-10 19:20:00 " 格式  
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        if (!str.equals("") && str != null) {
            try {
                return format.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Date stringToDate1(String str) {
        //str =  " 2008-07-10 19:20:00 " 格式  
        SimpleDateFormat format = new SimpleDateFormat(formatPattern_full);
        if (!str.equals("") && str != null) {
            try {
                return format.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 字符串转换日期 
     * @param str
     * @param formatprn 格式
     * @return
     */
    public static Date stringToDate(String str, String formatprn) {
        //str =  " 2008-07-10 19:20:00 " 格式  
        SimpleDateFormat format = new SimpleDateFormat(formatprn);
        if (!str.equals("") && str != null) {
            try {
                return format.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //指定日期分钟后的日期
    public static Date getTimeValuemm(Date d, long l) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.setTimeInMillis(cal.getTimeInMillis() + l * 60000);
        return cal.getTime();
    }

    //指定日期秒钟后的日期
    public static Date getTimeValuess(Date d, long l) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.setTimeInMillis(cal.getTimeInMillis() + l * 1000);
        return cal.getTime();
    }

    //指定日期月数后的日期
    public static Date getTimeMonths(Date d, int m) {
        Calendar c = Calendar.getInstance();
        //  c.set(Calendar.YEAR, 2013);
        // c.set(Calendar.MONTH, 1);
        //  c.set(Calendar.DATE, 3);
        c.setTime(d);
        c.add(Calendar.MONTH, m);
        //   System.out.println(m);
        return c.getTime();
    }

    /**
     * 指定日期两个日期相差的秒数
     * @param d
     * @param d2
     * @return
     */
    public static long getTimeSubtract(Date d, Date d2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        long l = cal.getTimeInMillis() - cal2.getTimeInMillis();
        return l / 1000;
    }

    /**
     * 获取当月的日期最后一秒 
     * */
    public static Date getMonthEnd(Date d) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为本月最后一天
        cale.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), cale.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);//设置为本月最后一天
        return cale.getTime();
    }

    /**
     * 获取当月日期开始一秒
     **/
    public static Date getMonthStart(Date d) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        cale.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), cale.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        return cale.getTime();
    }

    /**
     * 获取当日的最后一秒
     * */
    public static Date getDayEnd(Date d) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), cale.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);//设置为本月最后一天
        return cale.getTime();
    }

    /**
     *获取当日开始一秒
     */
    public static Date getDayStart(Date d) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.set(cale.get(Calendar.YEAR), cale.get(Calendar.MONTH), cale.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        return cale.getTime();
    }

    //java中怎样计算两个时间如：“21:57”和“08:20”相差的分钟数、小时数 java计算两个时间差小时 分钟 秒 .  
    public void timeSubtract() {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        Date end = null;
        try {
            begin = dfs.parse("2004-01-02 11:30:24");
            end = dfs.parse("2004-03-26 13:31:40");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒   

        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
        long second1 = between % 60;
        System.out.println("" + day1 + "天" + hour1 + "小时" + minute1 + "分"
                + second1 + "秒");
    }

    /**
     * 增加天数
     * @param date  时间
     * @param num   天数
     * @return
     */
    public static Date daa(Date date, Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, num);//当前时间前去一个月，即一个月前的时间    
        calendar.getTime();//获取一年前的时间，或者一个月前的时间  
        return calendar.getTime();
    }

    /**
     * 时间减去天数
     * @param date
     * @param num
     * @return
     */
    private static Date addDay(Date date, int num) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(Calendar.DATE, -num);// 增加n天
        return cd.getTime();
    }

//    public static void main(String[] args) {
//		System.out.println(DateUtils.daa(DateUtils.da(), 30));
////		System.out.println(DateUtils.dateToString(DateUtils.daa(DateUtils.da(), 30), "yyyy-MM-dd HH:mm:ss"));
//	}

    /**
     * date去掉时分秒
     */
    public static Date da() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(new Date());
        Date date = null;
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取cron表达式
     */
//	public static String getCron(Date date) {
//	   String cron= DateFormatUtils.format(date, "ss mm HH dd MM ? yyyy");
//	   return cron;
//	}

    /**
     * 获取两个时间串时间的差值，单位为秒
     *
     * @param startTime
     *            开始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime
     *            结束时间 yyyy-MM-dd HH:mm:ss
     * @return 两个时间的差值(秒)
     */
    public static long getDiff(Date startTime, Date endTime) {
        long diff = 0;
        try {
            diff = startTime.getTime() - endTime.getTime();
            diff = diff / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diff;
    }

    public static Date getDatetoLong(Long date) {
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTimeInMillis(date);

            return cd.getTime();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime, String[] weekDays) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static String zhdateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 获取当前日期前几天或后几天的日期
     * @param date 日期
     * @param num
     * @return
     */
    public static Date getNextDay(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        date = calendar.getTime();
        return date;
    }

    public static Date getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date);
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取第二天
     * @return
     */
    public static String tomorrowDate() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
        Date tomorrow = c.getTime();
        String tomorrowDate = f.format(tomorrow);
        return tomorrowDate;
    }

    /**
     * 获得当天24点时间
     * @return
     */
    public static Date getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 生成随机数字和字母
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //length为几位密码 
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字  
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 随机6位 小写字母
     * @param length
     * @return
     */
    public static String getStringRandomLetters(int length) {
        String val = "";
        for (int i = 0; i < length; i++) {
            char c = (char) (int) (Math.random() * 26 + 97);
            val += c;
        }
        return val;
    }

    /**
     * 给时间加上3个小时
     * @param hour 需要加的时间
     * @return
     */
    public static Date addDate3Hour(int hour) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制   
        date = cal.getTime();
        return date;
    }

    public static Date minute(int minute) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);// 24小时制
        date = cal.getTime();
        return date;
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.minute(30));
    }

    /**
     * 获取当天23点
     * @return
     */
    public static Date addDate23() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    /**
     * 获取当天23点5959
     * @return
     */
    public static Date addDate235959() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date date = cal.getTime();
        return date;
    }

    /**
     * 获取当天0点
     * @return
     */
    public static Date addDate000000() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    /**
     * 获取第二天11点
     * @return
     */
    public static Date getNextDay(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, +1);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    public int compareDate(Date dt1, Date dt2) {
        if (dt1.getTime() > dt2.getTime()) {
            System.out.println("dt1 在dt2前");
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            System.out.println("dt1在dt2后");
            return -1;
        } else {//相等
            return 0;
        }
    }

    public static Date timeToDate(Long time) {
        //时间戳转化为Sting或Date
        Long times = time * 1000;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(times);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    public static Map<String, String> getWeekOfDate(Date date) {
        String[] zhdateToWeek = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String[] endateToWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] thirddateToWeek = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String datetime = DateUtils.dateToString(date);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("zh_CN", DateUtils.dateToWeek(datetime, zhdateToWeek));
        map.put("en_US", DateUtils.dateToWeek(datetime, endateToWeek));
        map.put("THIRD", DateUtils.dateToWeek(datetime, thirddateToWeek));
        return map;
    }


    public static Date scheduledates(Date da) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = sdf.format(da);
        Date date = null;
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}  