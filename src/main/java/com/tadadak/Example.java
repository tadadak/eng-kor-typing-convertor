package com.tadadak;

/**
 * Author : tdadak (dev.tadadak@gmail.com)
 * Date : 2020-01-10
 * Desc :
 * <p>
 * <p>
 */
public class Example {

    public static void main(String[] args) {
        EngKorTypingConvertor hangulCharConvertors = new EngKorTypingConvertor();
//        String word     = "ㅋㅋ울렐럯 얼레릴럴ㅇㄴㄹ멍맘ㄴㄹㅁㄴ¼(주) 멍충아ㅏㅠkk!@#$%^&*()★";
        String word     = "ㄱㅅㄱㅅㄱㅅ ㅂㅅㅂㅅ 음하하하?!ㄹㅁ ㄹㅎ ㄹㄱ ㅘㅝ ";
        String result1;
        String result2;
        String result3;

        // 1. 기본 변환 테스트 --------------------------
        System.out.println("1. word : " + word);

        // 2. 변환: 한글 -> 영어
        result1 = hangulCharConvertors.convertKor2Eng(word);
        System.out.println("2. convertKor2Eng : " + result1);

        // 3-1. 변환: 영어 -> 한글 (복합자음 허용O)
        result2 = hangulCharConvertors.convertEng2Kor(result1, true);
        System.out.println("3-1. convertEng2Kor : " + result2);

        // 3-2. 변환: 영어 -> 한글 (복합자음 허용X)
        result2 = hangulCharConvertors.convertEng2Kor(result1, false);
        System.out.println("3-2. convertEng2Kor : " + result2);


        // 4. 변환: 한글 -> 자음,모음 분리
        word = "abcd...!? 자음모음문자열로~";
        result3 = hangulCharConvertors.separateHangul(word);
        System.out.println("4-2. separateHangul : " + result3);

        // 한글유효성 체크 테스트 --------------------------
        String word2 = "ㄱㄳㄴㄴ이런글자 포함되도 ㅇㅋ?";

        // 5-1. 자음만으로도 허용 O(=true)
        System.out.println("5-1. isValidCompleteHangul : "
                + hangulCharConvertors.isValidCompleteHangul(word2, true));

        // 5-2. 자음만으로도 허용 X(=false)
        System.out.println("5-2. isValidCompleteHangul : "
                + hangulCharConvertors.isValidCompleteHangul(word2, false));
    }
}
