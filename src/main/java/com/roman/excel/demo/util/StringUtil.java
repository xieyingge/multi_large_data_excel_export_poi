package com.roman.excel.demo.util;


import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil {

    public static final Collator chinaCollator = Collator.getInstance(java.util.Locale.CHINA);


    /**
     * Collection==>String
     *
     * @param collections
     * @param fn
     * @param split
     * @param <T>
     * @return
     */
    public static <T> String collectionToSet(Collection<T> collections, Function<T, String> fn, String split) {
        if (collections == null || collections.isEmpty()) {
            return "";
        }
        Collection<String> strings = collections.stream().map(t -> fn.apply(t)).collect(Collectors.toList());
        return String.join(split, strings);
    }


    /**
     * 验证物理卡号的正确性
     *
     * @param cardNo
     * @return
     */
    public static boolean validateCardNo(String cardNo) {
        if (StringUtils.isEmpty(cardNo)) {
            return false;
        }
        String patten = "^\\d{10}$";
        boolean matches = Pattern.matches(patten, cardNo);
        if (!matches) {
            return false;
        }
        //最大卡号 FFFFFFFF =====>  4294967295
        long maxCardNo = Long.parseLong("4294967295");
        long thisCardNo = Long.parseLong(cardNo);
        return maxCardNo >= thisCardNo;
    }


    /**
     * 验证物理卡号的正确性
     *
     * @param cardNo
     * @return
     */
    public static boolean validateCardNoJunYa(String cardNo) {
        if (StringUtils.isEmpty(cardNo)) {
            return false;
        }
        String patten = "^\\d{10}$";
        boolean matches = Pattern.matches(patten, cardNo);
        if (!matches) {
            return false;
        }
        return true;
//        //最大卡号 FFFFFFFF =====>  4294967295
//        long maxCardNo = Long.parseLong("4294967295");
//        long thisCardNo = Long.parseLong(cardNo);
//        return maxCardNo >= thisCardNo;
    }

    /**
     * 比较 含有中文数字的字符串
     *
     * @param keyExtractor
     * @param <T>
     * @return Comparator<T>  返回比较对象
     * eg:
     * List<BaseDepart> collect = baseDeparts.stream().sorted(StringUtil.compareWithChinaNumber(BaseDepart::getName)).collect(Collectors.toList());
     * <p>
     * or:
     * Comparator<BaseDepart> chinaCollator = StringUtil.compareWithChinaNumber(BaseDepart::getName);
     * Collections.sort(baseDeparts, chinaCollator);
     */
    public static <T> Comparator<T> compareWithChinaNumber(Function<? super T, String> keyExtractor) {
        return (Comparator<T> & Serializable)
                (c1, c2) -> chinaCollator.compare(replaceChinaNumber(keyExtractor.apply(c1)),
                        replaceChinaNumber(keyExtractor.apply(c2)));
    }


    public static int compareWithChinaNumber(String s1, String s2) {
        return chinaCollator.compare(replaceChinaNumber(s1), replaceChinaNumber(s2));
    }


    private static String replaceChinaNumber(String s1) {
        if (s1 == null) {
            return "";
        }
        String temp = s1.replaceAll("零", "0")
                .replaceAll("一", "1")
                .replaceAll("二", "2")
                .replaceAll("三", "3")
                .replaceAll("四", "4")
                .replaceAll("五", "5")
                .replaceAll("六", "6")
                .replaceAll("七", "7")
                .replaceAll("八", "8")
                .replaceAll("九", "9");
        return temp;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatTime(String time) {
        if (time == null) {
            return "~";
        }
        if (time.length() == 14) {
            return time.substring(0, 4) + "年" + time.substring(4, 6) + "月" + time.substring(6, 8) + "日" + time.substring(8, 10) + "时" + time.substring(10, 12) + "分" + time.substring(12, 14) + "秒";
        }
        if (time.length() == 8) {
            return time.substring(0, 4) + "年" + time.substring(4, 6) + "月" + time.substring(6, 8) + "日";
        }
        if (time.length() == 6) {
            return time.substring(0, 2) + "时" + time.substring(2, 4) + "分" + time.substring(4, 6) + "秒";
        }
        return "";
    }


    public static String replaceDateAndTimeSplit(String dateString) {
        if (StringUtils.hasText(dateString))
            return dateString.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
        return dateString;
    }

    public static String formatPrice(Integer price) {
        float o1 = price == null ? 0 : price;
        return new DecimalFormat("#0.00").format(o1 / 100);
    }
}
