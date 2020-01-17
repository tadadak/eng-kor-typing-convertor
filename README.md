# EngKorTypingConvertor
> 한글/영문자판 타이핑 문자열 상호변환처리

# Release Note

| version | release | changes |
|---|:---:|---:|
| v1.1.0 | 2020.01.10 | first commit |

# How To Use
> Eng -> Kor
- 영문입력 대응 한글문자 변환
 
```java
String word = "rtgkqslek";  
// 복합자음 변환을 허용X (default = false)
EngKorTypingConvertor.convertEng2Han(result1);
// result:  ㄱㅅ합니다

// 복합자음 변환을 허용O
EngKorTypingConvertor.convertEng2Han(rsesult1, true);
// result:  ㄳ합니다
```

> Kor -> Eng
- 한글입력 대응 영문자 변환 
```java
String word = "영어로 바꿔봅시다!";
EngKorTypingConvertor.convertHan2Eng(result1);
// result:  duddjfh qkRnjqhqtlek!
```

> Kor validataion check 
- 조합형한글 유효성 체크
```java
// 자음만으로도 구성된 한글문자 허용O (default = true)
String word = "ㄳㄴㄴ글자 포함되도 괜찮은가?";
EngKorTypingConvertor.isValidCompleteHangul(word);
// result:  true

// 자음만으로도 구성된 한글문자 허용X
EngKorTypingConvertor.isValidCompleteHangul(word, false);
// result:  false
```