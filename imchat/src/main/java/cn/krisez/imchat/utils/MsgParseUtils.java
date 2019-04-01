package cn.krisez.imchat.utils;

import android.content.Context;
import cn.krisez.imchat.bean.MessageBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgParseUtils {
    //得到conversation的列表 只需要遍历value得到最后一个message就好了
    public static Map<String, List<MessageBean>> parse(List<MessageBean> msgList, Context context){
        Map<String,List<MessageBean>> map = new HashMap<>();
        String userId = SharePreferenceUtils.obj(context).getUserId();
        for (int i = 0; i < msgList.size(); i++) {
            String key = !msgList.get(i).from.equals(userId)?msgList.get(i).from:msgList.get(i).to;
            if(!map.containsKey(key)){
                List<MessageBean> list = new ArrayList<>();
                map.put(key,list);
            }
            map.get(key).add(msgList.get(i));
        }
        return map;
    }
}
