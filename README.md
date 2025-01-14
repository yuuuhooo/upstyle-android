# upstyle-android


### <Github 환경 설정>

- 사용할 기술 스택: Kotlin
- 사용할 라이브러리
    - Glide: 이미지 처리를 위해 사용
    - Retrofit: HTTP API를 쉽게 호출하기 위해 사용
    - flexbox: 레이아웃 구성을 위해 사용
    - navigation component: Fragment 전환을 위해 사용

---
#### Branch 전략

Git-Flow 전략을 사용.

- master : 제품으로 출시될 수 있는 브랜치
- develop : 다음 출시 버전을 개발하는 브랜치
- feature : 기능을 개발하는 브랜치
- release : 이번 출시 버전을 준비하는 브랜치
- hotfix : 출시 버전에서 발생한 버그를 수정하는 브랜치

⇒ 작업은 각자 `feature_(n주차)_(작업자명)` 브랜치에서 하고
다 끝나면 `develop` 브랜치에 merge

(merge 이후에는 작업한 feature 브랜치 삭제)

---
#### Commit convention

- 기본적인 형식은 제목, 본문, 꼬리말 세 가지 파트
- 제목 형식 `Type: 제목 내용`
    
    Type:(공백 1칸)제목 내용. 

  ex) Feat: 로그인 기능 구현
    
- **Type 목록**
    - Feat : 새로운 기능 추가
    - Fix : 버그 수정
    - Docs : 문서 수정
    - Style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
    - Refactor : 코드 리펙토링
    - Test : 테스트 코드, 리펙토링 테스트 코드 추가
    - Chore : 빌드 업무 수정, 패키지 매니저 수정
 
---

#### Code convention
기본적으로 코틀린 공식 문서에서 제공하는 코드 스타일 가이드(https://kotlinlang.org/docs/coding-conventions.html#source-code-organization)를 따름

- **소스파일 이름**
    - Kotlin 파일에 단일 클래스 또는 인터페이스(관련된 최상위 선언을 포함할 수도 있음)가 포함되어 있는 경우, 해당 파일의 이름은 클래스의 이름과 동일하며 확장자로 .kt를 추가해야 함.
    - 첫 글자는 대문자로 쓰고 나머지는 대문자와 소문자를 혼용하는 대문자 카멜 표기법을 사용함.

 - **클래스 레이아웃**
   - 클래스의 내용은 다음 순서로 작성되어야 함.
        1. 속성 선언과 초기화 블록
        2. 보조 생성자
        3. 메서드 선언
        4. Companion object
    
- **네이밍 룰**
    - 패키지 이름은 항상 소문자로 작성하고 언더스코어를 사용하지 않음. (org.example.project)
    - 여러 단어를 사용하는 것은 일반적으로 권장되지 않지만, 필요한 경우 단어를 연결하거나 카멜 케이스를 사용할 수 있음. (org.example.myProject).
    - 클래스와 객체의 이름은 대문자로 시작하고 카멜 케이스를 사용함.
    - 메소드, 속성 및 지역 변수의 이름은 소문자로 시작하고 캐멀 케이스(camel case)를 사용하며 밑줄은 사용하지 않음.
    - 상수의 이름(값이 변경되지 않는 속성으로 const로 표시되거나 사용자 지정 get 함수가 없는 최상위 또는 객체 val 속성)은 대문자 밑줄로 구분된 이름(Screaming Snake Case)을 사용해야 함.
    - xml 파일명은 <WHAT>_<WHERE>_<DESCRIPTION>_<SIZE> 순으로 작성 (iv_main_background.xml)

---

### <Android Studio 환경 설정>

- Android Studio 버전: Koala Feature Drop 2024.1.2

- targetSDK: 35

- minSDK: 24

- IDE 내 Emulator로 테스트
