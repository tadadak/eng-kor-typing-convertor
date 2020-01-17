# EngKorTypingConvertor
> 한글/영문자판 타이핑 문자열 상호변환처리

# Release Note

| version | release | changes |
|---|:---:|---:|
| v1.1.0 | 2020.01.18 | first commit: maven -> gradle project로 변경 후 재시작 |


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
# License
MIT License

Copyright (c) 2020 tadadak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
