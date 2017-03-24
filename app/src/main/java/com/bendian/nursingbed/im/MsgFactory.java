package com.bendian.nursingbed.im;

import org.jetbrains.annotations.NotNull;

/**
 * 消息工厂类
 * 本类中所谓的消息，仅仅是符合某些特殊规范的字符串，接收端依据相应的字符串特点
 * 利用正则表达式或者其他字符串解析方式，抽取出其中的消息内容。
 * Created by Administrator on 2016/8/23.
 */
public class MsgFactory {

    /**
     * 根据入参生产相应的消息字符串
     * @param msg 消息内容
     * @param msgType 消息类型
     * @return 生成的消息字符串
     */
    public static String create(@NotNull String msg, @NotNull MsgType msgType) {
        String message = null;
        switch (msgType){
            case Control:
                message =  "[C]"+msg+"[C]";
                break;
            case AutoNursing:
                message =  "[A]"+msg+"[A]";
                break;
            case Normal:
                message =  "[N]"+msg+"[N]";
                break;
        }
        return message;
    }

}
