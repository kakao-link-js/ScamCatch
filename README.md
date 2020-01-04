# Scam Catch :mobile_phone_off:

------

## ![imge](https://img.shields.io/badge/ProjectType-TeamProject-green) ![imge](https://img.shields.io/badge/Language-Java-yellow) 
![imge](https://img.shields.io/badge/Tools-AndroidStudio-blue)

## 프로그램 소개 :thumbsup:

- 사용자가 통화 시 자동으로 켜져 통화내용을 **딥러닝 후** 분석 된 내용을 바탕으로 보이스 피싱 여부를 알려주는 애플리케이션 입니다.
- 기존 보이스 피싱 앱인 후후, T전화와 달리 **직접 통화내용을 분석**하여 알려주기 때문에 보이스피싱 신고 횟수만으로 알려주는 기존 앱 보다 신뢰도가 더 높습니다.
- CNN Text Classifiacion 딥러닝을 통해 **보이스피싱 감지 정확도 92%** 산출.
- FireBase 와 Server를 통해 미리 신고 횟수를 알 수 있어 **보이스피싱 의심 번호 사전 차단**이 가능

## 프론트 주요 기술 요소

### BroadcastReceiver 활용

- 휴대전화 통화 상태 확인 및 수신 시 앱 실행 기능 구현.

### Service 활용

- 앱이 종료 된 상태에서 전화 수신 시 팝업 디스플레이 구현

### Azure API 활용 실시간 STT 기능

- 통화 시 전화 내용을 녹음하지 않고 Azure API를 활용 STT 후 바로 서버에 전송

### FireBase 활용

- 신고 횟수 저장 DB를 FireBase로 이용

## 전체 시스템 흐름도

![image](https://user-images.githubusercontent.com/37828448/71764530-3a377180-2f2c-11ea-9334-014d685d0549.png)

## 프로그램 기능소개

|     상태     |                          기능 설명                           |
| :----------: | :----------------------------------------------------------: |
| 전화 수신 시 |               학번으로 회원가입 후 로그인 가능               |
| 전화 통화 시 | Azure STT 활용 통화 내용 문자 화, 가상 서버에 전송해 보이스 피싱 여부 확인 |

### 전화 수신 시

![전화수신](https://user-images.githubusercontent.com/37828448/71764874-11fe4180-2f31-11ea-8bf8-43bb87ab633a.png)

### 전화 통화 시

![스캠캐치](https://user-images.githubusercontent.com/37828448/71765456-47f2f400-2f38-11ea-9dc7-d98314c0c66d.gif)

- Azure STT를 활용해 사용자의 통화내용을 문자화. (통화 내용은 저장 되지 않음)
- 변환 된 내용은 바로 프로젝트 가상 서버에 보내 보이스 피싱 여부 확인.
- 보내진 데이터는 이미 교사 학습된 데이터를 이용한  Attention을 기반으로 한 모델을 사용해 딥러닝을 진행
- 사용자가 정한 일정 의심 확률을 넘어가면 팝업 알림 
