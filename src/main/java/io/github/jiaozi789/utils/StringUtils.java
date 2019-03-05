package io.github.jiaozi789.utils;

/**
 * @Author 廖敏
 * @Date 2019-03-04 12:39
 **/
public class StringUtils {
    /**
     * 首字母转换成大写
     * @param str 被转换字符串 比如aaa
     * @return 转换后的字符串 Aaa
     */
    public static String initCap(String str){
       return str.substring(0,1).toUpperCase()+str.substring(1);
    }

    /**
     * 将分隔字符串转换成波峰波谷
     * @param src 原始字符串 比如 a_bbb_ccc
     * @param separt 分隔字符串 _
     * @return 返回波峰字符串  aBbbCcc
     */
    public static String toPeak(String src,String separt){
        int loc=0;
        int startIndex=0;
        StringBuffer endStr=new StringBuffer();
        while(startIndex<src.length()-1 &&(loc=src.indexOf(separt,startIndex))!=-1){
            if(startIndex==0) {
                endStr.append(src.substring(startIndex, loc));
            }else{
                endStr.append(initCap(src.substring(startIndex, loc)));
            }
            startIndex=loc+separt.length();
        }
        if((startIndex-1)!= src.length()-1){
            if(startIndex==0)
                endStr.append((src.substring(startIndex)));
            else
                endStr.append(initCap(src.substring(startIndex)));
        }
        return endStr.toString();
    }

    public static void main(String[] args) {
        System.out.println(toPeak("abccc","_"));
    }
}
