package com.zjk.hy.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类，可以用已经有的规则，或者自己写正则规则来做匹配.
 */
public class RegexUtil {
    /**
     * "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位.
     */
    // public static final String MOBILEPHONENUM = "^[1](([3][0-9])|([4][0-9])|([5][0-9])|([6][0-9])|([7][0-9])|([8][0-9])|([9][8,9]))[0-9]{8}$";
    public static final String MOBILEPHONENUM ="^[1][0-9]{10}$";
    /**
     * 8-16位数字加字母（区分大小写）
     */
    public static final String CHECKPASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";

    /**
     * 账号密码注册时验证姓名
     */
    public static final String REGISTERNICKNAME = "^[\\u4E00-\\u9FA5]{2,6}";
    /**
     * 验证码校验6位数字
     */
    public static final String VERIFICATION = "^[0-9]{6}$";
    /**
     * 身份证认证
     */
    public static final String IDCARDNUM = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
    /**
     * 验证是否中国电信的手机号
     */
    public static final String IS_CHINA_TELECOM = "^[1](33|49|53|([7][37])|([8][019])|([9][1,9]))[0-9]{8}$";

    /**
     * 通过正则表达式匹配字符串.
     *
     * @param content 要匹配的内容
     * @param regex   正则表达式
     * @return true 如果匹配
     */
    public static boolean matcher(String content, String regex) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(regex)) {
            return false;
        }
        return Pattern.compile(regex).matcher(content).matches();
    }
    /**
     * 测试合到4.0.0会不会冲突
     */
}