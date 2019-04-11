package cn.krisez.imchat.utils;

import android.content.Context;
import android.util.Log;

import cn.krisez.imchat.bean.MessageBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgParseUtils {
    //得到conversation的列表 只需要遍历value得到最后一个message就好了
    public static Map<String, List<MessageBean>> parse(List<MessageBean> msgList, Context context){
        Map<String,List<MessageBean>> map = new HashMap<>();
        return mergeMap(map,msgList,context);

    }

    public static Map<String,List<MessageBean>> mergeMap(Map<String, List<MessageBean>> map, List<MessageBean> list, Context context){
        String userId = SharePreferenceUtils.obj(context).getUserId();
        Log.d("MsgParseUtils", "mergeMap:" + list.size());
        for (int i = 0; i < list.size(); i++) {
            String key = !list.get(i).from.equals(userId)?list.get(i).from:list.get(i).to;
            if(!map.containsKey(key)){
                List<MessageBean> mb = new ArrayList<>();
                map.put(key,mb);
            }
            map.get(key).add(list.get(i));
        }
        return map;
    }
}
