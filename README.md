# FourBasicOpt

사칙연산(덧셈, 뺄셈, 곱셈, 나눗셈)을 지원하는 Android 계산기 앱입니다.

## 기능

- 숫자 0~9와 소수점 입력
- `+` `−` `×` `÷` 사칙연산
- `C`로 전체 지우기, `⌫`로 한 글자 지우기
- 위쪽 줄에 계산식, 아래쪽 줄에 입력값과 결과 표시
- 화면을 회전해도 입력 중인 값과 식 유지
- 라이트/다크 테마 지원

## 계산 규칙

| 상황 | 동작 |
| --- | --- |
| 연산 순서 | 누른 순서대로 계산합니다. `2 + 3 × 4 =`의 결과는 `20`입니다. |
| 연속 계산 | `=` 뒤에 연산자를 누르면 결과에 이어서 계산합니다. |
| 연산자 변경 | 연산자를 연달아 누르면 마지막 연산자만 적용됩니다. |
| 0으로 나누기 | "0으로 나눌 수 없습니다"를 표시하고, 다음 입력 때 초기화됩니다. |
| 소수 계산 | `BigDecimal`을 사용해 `0.1 + 0.2`가 정확히 `0.3`으로 계산됩니다. |
| 정밀도 | 결과는 유효숫자 15자리로 반올림됩니다. 입력은 최대 15자입니다. |

## 개발 환경

- 언어: Java 11
- UI: XML 레이아웃, Material 3
- minSdk 24 / targetSdk 34 / compileSdk 34
- Gradle 8.9, Android Gradle Plugin 8.7.2

## 실행 방법

Android Studio에서 프로젝트를 열고 Run을 누르거나, 터미널에서 다음을 실행합니다.

```bash
# debug APK 빌드
./gradlew assembleDebug

# 연결된 기기나 에뮬레이터에 설치
./gradlew installDebug
```

Windows에서는 `./gradlew` 대신 `gradlew.bat`을 사용합니다.

## 테스트

```bash
./gradlew testDebugUnitTest
```

계산 로직은 `CalculatorTest`의 단위 테스트로 검증합니다.

## 프로젝트 구조

```
app/src/main/
├── java/com/example/fourbasicopt/
│   ├── Calculator.java      # 계산 로직 (Android 의존성 없음)
│   └── MainActivity.java    # 버튼 연결과 화면 갱신
└── res/
    ├── layout/activity_main.xml   # 계산기 화면
    └── values/
        ├── strings.xml            # 버튼 문자열, 오류 메시지
        └── themes.xml             # 테마와 버튼 스타일
app/src/test/
└── java/com/example/fourbasicopt/
    └── CalculatorTest.java  # 계산 로직 단위 테스트
```
