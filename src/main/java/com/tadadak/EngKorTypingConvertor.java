package com.tadadak;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : tadadak
 * Date : 2020-01-09
 * Desc : 영문->한글 / 한글->영문 타이핑 상호 변환모듈
 * <p>
 * <p>
 */
public class EngKorTypingConvertor {
    /**
     * 음절문자 테이블표 기준 값
     * - 참고: https://ko.wikipedia.org/wiki/%ED%95%9C%EA%B8%80_%EC%9D%8C%EC%A0%88
     */

     /**
     * 유니코드표내 한글문자 전체 개수
     * 초성 19, 중성 21, 종성 28개의 전체 조합.
     * 11172 = 19 * 21 * 28
     */
    private static final int TOTAL_HANGUL_CHAR  = 11172;

    /** 표음문자 **/
    private static final int PHONOGRAM          = 28;

    /** 모음수 **/
    private static final int VOWEL              = 21;

    /** 한글문자 시작코드 [가], 코드번호 = 44032 **/
    private static final int HANGUL_CHAR_START_CODE = 0xAC00;

    static Pattern ENG_PATTERN = Pattern.compile("[a-zA-Z]");

    /** 음절(Syllable)타입 **/
    enum HangulSyllableType {
        CHOSUNG, JUNGSUNG, JONGSUNG
    }

    /************************************************
     *  영문알파벳 대응 테이블
     ***********************************************/

    /** 초성(19)
     *  ㄱ      ㄲ      ㄴ      ㄷ      ㄸ
     *  ㄹ      ㅁ      ㅂ      ㅃ      ㅅ
     *  ㅆ      ㅇ      ㅈ      ㅉ      ㅊ
     *  ㅋ      ㅌ      ㅍ      ㅎ
     */
    public static String[] CHOSUNG_ENG = {
            "r", "R", "s", "e", "E",
            "f", "a", "q", "Q", "t",
            "T", "d", "w", "W", "c",
            "z", "x", "v", "g"
    };

    /** 중성(21)
     * ㅏ      ㅐ      ㅑ      ㅒ      ㅓ
     * ㅔ      ㅕ      ㅖ      ㅗ      ㅘ
     * ㅙ      ㅚ      ㅛ      ㅜ      ㅝ
     * ㅞ      ㅟ      ㅠ      ㅡ      ㅢ
     * ㅣ
     */
    public static String[] JUNGSONG_ENG = {
            "k",  "o",  "i", "O", "j",
            "p",  "u",  "P", "h", "hk",
            "ho", "hl", "y", "n", "nj",
            "np", "nl", "b", "m", "ml",
            "l"
    };

    /** 종성(1+27)
     * 없음    ㄱ      ㄲ      ㄳ      ㄴ
     * ㄵ      ㄶ      ㄷ      ㄹ      ㄺ
     * ㄻ      ㄼ      ㄽ      ㄾ      ㄿ
     * ㅀ      ㅁ      ㅂ      ㅄ      ㅅ
     * ㅆ      ㅇ      ㅈ      ㅊ      ㅋ
     * ㅌ      ㅍ      ㅎ
     */
    public static String[] JONGSUNG_ENG = {
            "",   "r",  "R",  "rt", "s",
            "sw", "sg", "e",  "f",  "fr",
            "fa", "fq", "ft", "fx", "fv",
            "fg", "a",  "q",  "qt", "t",
            "T",  "d",  "w",  "c",  "z",
            "x",  "v",  "g"
    };

    /** 단일 자음(30) - ㄸ,ㅃ,ㅉ 포함
     * ㄱ      ㄲ      ㄳ      ㄴ      ㄵ
     * ㄶ      ㄷ      ㄸ      ㄹ      ㄺ
     * ㄻ      ㄼ      ㄽ      ㄾ      ㄿ
     * ㅀ      ㅁ      ㅂ      ㅃ      ㅄ
     * ㅅ      ㅆ      ㅇ      ㅈ      ㅉ
     * ㅊ      ㅋ      ㅌ      ㅍ      ㅎ
     *
     */
    public static String[] SINGLE_JAUM_ENG = {
            "r",  "R",  "rt", "s",  "sw",
            "sg", "e",  "E" , "f",  "fr",
            "fa", "fq", "ft", "fx", "fv",
            "fg", "a",  "q",  "Q",  "qt",
            "t",  "T",  "d",  "w",  "W",
            "c",  "z",  "x",  "v",  "g"
    };


    /************************************************
     * 자음,모음 분리용 문자코드 테이블
     ***********************************************/

    /** 초성(19) */
    public static char[] SPLIT_CHOSUNG_CHAR = {
            0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
            0x3139, 0x3141, 0x3142, 0x3143, 0x3145,
            0x3146, 0x3147, 0x3148, 0x3149, 0x314a,
            0x314b, 0x314c, 0x314d, 0x314e
    };
    /** 중성(21)*/
    public static char[] SPLIT_JUNGSUNG_CHAR = {
            0x314f, 0x3150, 0x3151, 0x3152, 0x3153,
            0x3154, 0x3155, 0x3156, 0x3157, 0x3158,
            0x3159, 0x315a, 0x315b, 0x315c, 0x315d,
            0x315e, 0x315f, 0x3160, 0x3161, 0x3162,
            0x3163
    };
    /** 종성(1+27) */
    public static char[] SPLIT_JONGSUNG_CHAR = {
            0x0000, 0x3131, 0x3132, 0x3133, 0x3134,
            0x3135, 0x3136, 0x3137, 0x3139, 0x313a,
            0x313b, 0x313c, 0x313d, 0x313e, 0x313f,
            0x3140, 0x3141, 0x3142, 0x3144, 0x3145,
            0x3146, 0x3147, 0x3148, 0x314a, 0x314b,
            0x314c, 0x314d, 0x314e
    };

    public static Map<String, Integer> CHOSUNG_ENG_IDX;
    public static Map<String, Integer> JUNGSUNG_ENG_IDX;
    public static Map<String, Integer> JONGSUNG_ENG_IDX;

    /** 단일 자음 한글코드표 범위 **/
    public static int[] JAUM_CODE_AREA = {34097, 34126};

    /** 단일 모음 한글코드표 범위 **/
    public static int[] MOUM_CODE_AREA = {34127, 34147};

    static{
        /**
         * 초/중/종성 영문char코드 index맵 구성
         */
        CHOSUNG_ENG_IDX     = new HashMap();
        JUNGSUNG_ENG_IDX    = new HashMap();
        JONGSUNG_ENG_IDX    = new HashMap();

        for(int i=0; i<CHOSUNG_ENG.length; i++){
            CHOSUNG_ENG_IDX.put(CHOSUNG_ENG[i], i);
        }
        for(int i=0; i<JUNGSONG_ENG.length; i++){
            JUNGSUNG_ENG_IDX.put(JUNGSONG_ENG[i], i);
        }
        for(int i=0; i<JONGSUNG_ENG.length; i++){
            JONGSUNG_ENG_IDX.put(JONGSUNG_ENG[i], i);
        }
    }

    /**
     * 단일영문자에 대응하는 음절코드표 index값 추출.
     *
     * @param type      CHOSUNG: 초성 | JUNGSUNG: 중성 | JONGSUNG: 종성
     * @param engChar   단일영문자
     */
    static private int getSyllableCode(HangulSyllableType type, String engChar) {
        Integer idx;
        switch (type) {
            case CHOSUNG:
                idx = CHOSUNG_ENG_IDX.get(engChar);
                if(idx != null){
                    return idx * VOWEL * PHONOGRAM;
                }
                break;
            case JUNGSUNG:
                idx = JUNGSUNG_ENG_IDX.get(engChar);
                if(idx != null){
                    return idx * PHONOGRAM;
                }
                break;
            case JONGSUNG:
                // 종성의 첫번째값은 쓰지 않음.
                idx = JONGSUNG_ENG_IDX.get(engChar);
                if(idx != null){
                    return idx;
                }
                break;
        }
        return -1;
    }

    /**
     * 영문자열내 중성,종성 음절코드표 index값 추출
     *
     * @param type      CHOSUNG: 초성 | JUNGSUNG: 중성 | JONGSUNG: 종성
     * @param idx       문자열 index
     * @param engStr    영문자열
     * @param isDouble  한글자 or 두글자로 이루어졌는지 여부
     * @return
     */
    static private int getSyllableCode(HangulSyllableType type, int idx, String engStr, boolean isDouble){
        switch(type){
            // A. 중성코드 추출
            case JUNGSUNG:
                if(!isDouble){
                    // A.1. 한글자 중성코드
                    if((idx+1) <= engStr.length()){
                        return getSyllableCode(HangulSyllableType.JUNGSUNG, engStr.substring(idx, idx+1));
                    }
                }else{
                    // A.2. 두글자 중성코드
                    int result;
                    if ((idx+2) <= engStr.length()) {
                        result = getSyllableCode(HangulSyllableType.JUNGSUNG, engStr.substring(idx, idx+2));
                        if (result != -1) {
                            return result;
                        } else {
                            return -1;
                        }
                    }
                }
             break;

            // B. 종성코드 추출
            case JONGSUNG:
                if(!isDouble){
                    // B.1. 한글자 종성코드
                    if ((idx+1) <= engStr.length()) {
                        return getSyllableCode(HangulSyllableType.JONGSUNG, engStr.substring(idx, idx+1));
                    }
                }else{
                    // B.2. 두글자 종성코드
                    if ((idx+2) <= engStr.length()) {
                        return getSyllableCode(HangulSyllableType.JONGSUNG, engStr.substring(idx, idx+2));
                    }
                }
                break;
        }
        return -1;
    }

    /**
     * 한글문자열을, 한글조합되는 영문문자열로 변환
     * ex) 영어로 --> duddjfh
     *
     * @param hanStr
     * @return
     */
    public static String convertHan2Eng(String hanStr){
        StringBuffer sb     = new StringBuffer();
        StringBuffer split  = new StringBuffer();

        for (int i = 0; i < hanStr.length(); i++) {
            // 문자표번호 = 유니코드번호 - 0xAC00
            char chars = (char)(hanStr.charAt(i) - HANGUL_CHAR_START_CODE);

            if (chars >= 0 && chars <= TOTAL_HANGUL_CHAR) {
                // Case1. 자음+모음 조합글자인 경우

                // 1.1. 초/중/종성 분리
                int chosungIdx  = chars / (VOWEL * PHONOGRAM);
                int jungsungIdx = chars % (VOWEL * PHONOGRAM) / PHONOGRAM;
                int jongsungIdx = chars % (VOWEL * PHONOGRAM) % PHONOGRAM;

                // 1.2. 분리결과 담기
                split.append(SPLIT_CHOSUNG_CHAR[chosungIdx])
                        .append(SPLIT_JUNGSUNG_CHAR[jungsungIdx]);

                // 1.3. 자음분리
                if (jongsungIdx != 0) {
                    // 종성이 존재할경우, 분리결과 담는다
                    split.append(SPLIT_JONGSUNG_CHAR[jongsungIdx]);
                }

                // 1.4. 영문변환결과 담기
                sb.append(CHOSUNG_ENG[chosungIdx])
                        .append(JUNGSONG_ENG[jungsungIdx]);

                if (jongsungIdx != 0) {
                    // 종성이 존재할경우, 영문변환결과에 담는다
                    sb.append(JONGSUNG_ENG[jongsungIdx]);
                }

            } else {
                // Case2. 한글 이외의 문자 or 자음만 있는 경우
                // 2.1. 자음 분리 & 분리결과에 담기
                split.append(((char)(chars + HANGUL_CHAR_START_CODE)));

                // 2.2. 영문변환
                if( chars >= JAUM_CODE_AREA[0] && chars <= JAUM_CODE_AREA[1]){
                    // 2.2.1 단일자음 범위인 경우
                    int jaum = (chars-JAUM_CODE_AREA[0]);
                    sb.append(SINGLE_JAUM_ENG[jaum]);
                } else if( chars >= MOUM_CODE_AREA[0] && chars <= MOUM_CODE_AREA[1]) {
                    // 2.2.2 단일모음 범위인 경우
                    int moum = (chars-MOUM_CODE_AREA[0]);
                    sb.append(JUNGSONG_ENG[moum]);
                } else {
                    // 2.2.3 그 외 문자
                    sb.append(((char)(chars + HANGUL_CHAR_START_CODE)));
                }
            }//if
        }//for

        //System.out.println("> 자음/모음 분리결과: "+ split.toString());
        return sb.toString();
    }

    /**
     * 한글로 조합되는 영문문자열을, 한글문자열로 변환
     * ex) gksrmffh -> 한글로
     *
     * @param engStr
     * @param allowDoubleJaum
     *  - 복합자음 변환을 허용할지 여부.
     *  - (default)   false: 허용안함 ㄱㅅ  ㅂㅅ
     *                true : 허용함   ㄳ ㅄ
     * @return
     */
    public static String convertEng2Han(String engStr, boolean allowDoubleJaum) {
        StringBuffer sb = new StringBuffer();
        String currentChar;
        Matcher mc;

        int chosungCode;
        int jungsungCode;
        int jongsungCode;
        int tempJungsungCode;
        int tempJongsungCode;

        for (int i = 0; i < engStr.length(); i++) {
            currentChar = engStr.substring(i, i+1);
            mc = ENG_PATTERN.matcher(currentChar);
            // 영문자(a-zA-Z) 아니면, 그대로 넘긴다.
            if(!mc.find()){
                sb.append(currentChar);
                continue;
            }

            // 초성코드 추출
            chosungCode = getSyllableCode(HangulSyllableType.CHOSUNG, currentChar);

            // 1. 초성코드인 경우
            if(chosungCode != -1){
                i++; // 다음문자로

                // 1.1. 2자 조합의 중성코드 추출
                tempJungsungCode = getSyllableCode(HangulSyllableType.JUNGSUNG, i, engStr, true);

                if (tempJungsungCode != -1) {
                    // 1.1.1. 찾으면, 다다음으로 탐색 index 설정
                    jungsungCode = tempJungsungCode;
                    i += 2;
                } else {
                    // 1.1.2. 없다면, 단일조합 중성코드 추출
                    jungsungCode = getSyllableCode(HangulSyllableType.JUNGSUNG, i, engStr, false);
                    // 단일조합 중성코드도 없는 경우,
                    if(jungsungCode == -1){
                        char chars = (char)(SPLIT_CHOSUNG_CHAR[CHOSUNG_ENG_IDX.get(currentChar)] - HANGUL_CHAR_START_CODE);

                        if( chars >= JAUM_CODE_AREA[0] && chars <= JAUM_CODE_AREA[1]){
                            // allowDoubleJaum = true인 경우, 복합 초성코드 찾아봄 ex) ㄳ ㅄ
                            if (allowDoubleJaum && (i+1) <= engStr.length()) {
                                String chkCombChar = engStr.substring(i, i+1);
                                if(JONGSUNG_ENG_IDX.containsKey(currentChar + chkCombChar)){
                                    sb.append(SPLIT_JONGSUNG_CHAR[JONGSUNG_ENG_IDX.get(currentChar + chkCombChar)]);
                                    continue;
                                }
                            }//end if

                            sb.append(SPLIT_CHOSUNG_CHAR[CHOSUNG_ENG_IDX.get(currentChar)]);
                            i--;
                            continue;
                        }
                    }else{
                        i++;
                    }
                }

                // 1.2. 2자 조합의 종성코드 추출
                tempJongsungCode = getSyllableCode(HangulSyllableType.JONGSUNG, i, engStr, true);
                if (tempJongsungCode != -1) {
                    // 1.2.1. 종성코드 찾았으면, 바로 다음 중성문자에 대한 코드를 추출한다.
                    jongsungCode = tempJongsungCode;
                    tempJungsungCode = getSyllableCode(HangulSyllableType.JUNGSUNG, i+2, engStr, false);
                    if (tempJungsungCode != -1) { // 코드 값이 있을 경우
                        // 중성코드 찾았으면, 단일조합 종성코드값 저장
                        jongsungCode = getSyllableCode(HangulSyllableType.JONGSUNG, i, engStr, false);
                    } else {
                        i++;
                    }
                } else {
                    // 1.2.2. 종성코드 못찾았을 경우, 그 다음의 중성 문자에 대한 코드 추출.
                    tempJungsungCode = getSyllableCode(HangulSyllableType.JUNGSUNG, i+1, engStr, false);
                    if (tempJungsungCode != -1) {
                        // 중성 문자가 존재하면, 종성 문자는 없는 한글문자다.
                        jongsungCode = 0;
                        i--;
                    } else {
                        // 단일조합 종성코드 추출
                        jongsungCode = getSyllableCode(HangulSyllableType.JONGSUNG, i, engStr, false);
                        if (jongsungCode == -1){
                            // 못찾았다면, 초성,중성으로 이루어진 한글이거나.. 그 외의 문자다.
                            jongsungCode = 0;
                            i--;
                        }
                    }
                }
                // 추출한 (초성문자 코드, 중성문자 코드, 종성문자 코드) 합한 후 변환하여 넘김.
                sb.append((char)(HANGUL_CHAR_START_CODE + chosungCode + jungsungCode + jongsungCode));

            }else{
                // 2. 초성코드가 아닌 경우
                // 2.1. 단일 중성인지 검사
                Integer chkIdx = JUNGSUNG_ENG_IDX.get(currentChar);
                if(chkIdx != null){
                    char chars = (char)(SPLIT_JUNGSUNG_CHAR[JUNGSUNG_ENG_IDX.get(currentChar)] - HANGUL_CHAR_START_CODE);
                    if( chars >= MOUM_CODE_AREA[0] && chars <= MOUM_CODE_AREA[1]) {
                        sb.append(SPLIT_JUNGSUNG_CHAR[JUNGSUNG_ENG_IDX.get(currentChar)]);
                    }
                }
            }//end if

        }
        return sb.toString();
    }

    public static String convertEng2Han(String eng) {
        return convertEng2Han(eng, false);
    }


    /**
     * 한글문자가 포함된 텍스트들이, 완전히 조합된 한글로만 이루어져있는지 검사
     *
     * @param word
     * @param allowJustJaum
     *  - 자음만으로 된 한글문자도 정상으로 인식할지 여부.
     *  - (default) true :  자음만으로도 허용한다.
     *              false:  완전한글만 허용한다.
     *
     * @return
     */
    public static boolean isValidCompleteHangul(String word, boolean allowJustJaum){
        char chars;
        for (int i = 0; i < word.length(); i++) {
            chars = (char)(word.codePointAt(i) - HANGUL_CHAR_START_CODE);
            //자음모음 합친 한글문자코드가 아닐때 체크
            if ( !(chars >= 0 && chars <= TOTAL_HANGUL_CHAR) ) {
                if( chars >= JAUM_CODE_AREA[0] && chars <= JAUM_CODE_AREA[1]){
                    // 단일자음 범위인 경우, 자음단일 한글문자 허용하지 않으면 false
                    if(!allowJustJaum) return false;
                } else if( chars >= MOUM_CODE_AREA[0] && chars <= MOUM_CODE_AREA[1]) {
                    // 단일모음 범위인 경우
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidCompleteHangul(String word){
        return isValidCompleteHangul(word, true);
    }

}