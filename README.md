# upstyle-android


### <Github 환경 설정>

- 사용할 기술 스택: Kotlin
- 사용할 라이브러리
    - Glide: 이미지 처리를 위해 사용
    - Retrofit: HTTP API를 쉽게 호출하기 위해 사용
    - flexbox: 레이아웃 구성을 위해 사용

---
#### Branch 전략

Git-Flow.

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
### <Android Studio 환경 설정>

- Android Studio 버전: Koala Feature Drop 2024.1.2

- targetSDK: 35

- minSDK: 24

- IDE 내 Emulator로 테스트
