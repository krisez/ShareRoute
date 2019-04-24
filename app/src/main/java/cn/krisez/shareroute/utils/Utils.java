package cn.krisez.shareroute.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static cn.krisez.framework.utils.DensityUtil.getTime;
public class Utils {

    public static String time2Add(String time,String past) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("zh","CN"));
        try {
            long now = format.parse(time).getTime();
            long p = (Long.parseLong(past.substring(0,past.length()-2))+1)*1000*60;
            return format.format(new Date(now+p));
        } catch (ParseException e) {
            e.printStackTrace();
            return getTime();
        }
    }
}
