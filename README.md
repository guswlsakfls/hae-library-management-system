# 📚 도서관리시스템 프로젝트
기업 내 도서 및 자료를 효율적으로 관리하기 위한 도서관리시스템 개발
<div align="left">
<img width="600" alt="image" src="https://github.com/guswlsakfls/Reptile_website/assets/46312017/e8cf78f2-75bb-4a3c-bf53-0a9716e8d6d0">

</div>

# 🗓 버전 v0.1
> **42seoul 카뎃** <br/> **개발기간: 2023.06.12 ~ 2023.07.21**

## 📮 배포 주소

<!-- > **상용화 버전** : [http://voluntain.cs.skku.edu/](http://voluntain.cs.skku.edu/) <br> -->
> **상용화 버전** : 개발중... <br>

## 🧑‍💻 소개

|      조현진       |                                                                                                               
| :------------------------------------------------------------------------------: |
|   <img width="160px" src="https://github.com/guswlsakfls/hae-library-management-system/assets/46312017/0b4ebec3-22c0-44d6-b169-3ab30b4b2014" />    |
|   [@guswlsakfls](https://github.com/guswlsakfls)   |
| 42seoul 카뎃 |

## 🔍 시작 가이드
### 필요사항
웹 서비스를 실행하기 위한 빌드 및 설치

<!-- - [Node.js 14.19.3](https://nodejs.org/ca/blog/release/v14.19.3/)
- [Npm 9.2.0](https://www.npmjs.com/package/npm/v/9.2.0)
- [Strapi 3.6.6](https://www.npmjs.com/package/strapi/v/3.6.6) -->

### 설치
``` bash
$ git clone https://github.com/guswlsakfls/hae-library-management-system.git
$ cd hae-library-management-system
```

### 프론트엔드
<!-- #### Frontend
```
$ cd voluntain-app
$ nvm use v.14.19.3
$ npm install 
$ npm run dev
``` -->


### 백엔드
<!-- #### Backend
```
$ cd strapi-backend
$ nvm use v.14.19.3
$ npm install
$ npm run develop
``` -->

---

## 🐈 기술스택

### 환경
![Vscode](https://img.shields.io/badge/VSCODE-%23007ACC?style=for-the-badge&logo=visualstudiocode&logoColor=white)
![Intellij](https://img.shields.io/badge/INTELLIJ-000000?style=for-the-badge&logo=IntelliJ%20IDEA&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)             

### 개발
#### 프론트
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![TailWindCSS](https://img.shields.io/badge/TailwindCSS-%23008000?style=for-the-badge&logo=tailwindcss&logoColor=61DAFB)



#### 백엔드
![Java](https://img.shields.io/badge/Java-3766AB?style=for-the-badge&logo=Java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![Maria DB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)

---
## 🗒 ERD (Updated at 2023.06.12)
![erd_backend]()

## 📺 화면 구성
| 홈 페이지  |  도서 목록 페이지   |
| :-------------------------------------------: | :------------: |
|  <img width="329" src=""/> |  <img width="329" src=""/>|  
| 마이 페이지   |  대출 페이지   |  
| <img width="329" src=""/>   |  <img width="329" src=""/>     |

---
## 📦 주요 기능

### 도서 검색
- 옵션을 선택하여 원하는 도서 검색 가능

### 도서 대출 및 반납
- 원하는 도서를 대출 하고 반납하는 기능

### 대여 현황 관리
- 관리자가 사용자의 대여 현황들을 관리하는 기능

### 도서 구매 및 추가
- 사용자가 원하는 도서를 구매 요청 및 추가 기능

### 사용자 관리
- 관리자가 사용자 및 사서 유저들 권한을 관리한다.

---
## ⚒ 아키텍쳐

### 디렉토리 구조

<!--
```bash
├── README.md : 리드미 파일
│
├── strapi-backend/ : 백엔드
│   ├── api/ : db model, api 관련 정보 폴더
│   │   └── [table 이름] : database table 별로 분리되는 api 폴더 (table 구조, 해당 table 관련 api 정보 저장)
│   │       ├── Config/routes.json : api 설정 파일 (api request에 따른 handler 지정)
│   │       ├── Controllers/ [table 이름].js : api controller 커스텀 파일
│   │       ├── Models : db model 관련 정보 폴더
│   │       │   ├── [table 이름].js : (사용 X) api 커스텀 파일
│   │       │   └── [table 이름].settings.json : model 정보 파일 (field 정보)
│   │       └─── Services/ course.js : (사용 X) api 커스텀 파일
│   │ 
│   ├── config/ : 서버, 데이터베이스 관련 정보 폴더
│   │   ├── Env/production : 배포 환경(NODE_ENV = production) 일 때 설정 정보 폴더
│   │   │   └── database.js : production 환경에서 database 설정 파일
│   │   ├── Functions : 프로젝트에서 실행되는 함수 관련 정보 폴더
│   │   │   │   ├── responses : (사용 X) 커스텀한 응답 저장 폴더
│   │   │   │   ├── bootstrap.js : 어플리케이션 시작 시 실행되는 코드 파일
│   │   │   │   └── cron.js : (사용 X) cron task 관련 파일
│   │   ├── database.js : 기본 개발 환경(NODE_ENV = development)에서 database 설정 파일
│   │   └── server.js : 서버 설정 정보 파일
│   │  
│   ├── extensions/
│   │   └── users-permissions/config/ : 권한 정보
│   │ 
│   └── public/
│       └── uploads/ : 강의 별 사진
│
└── voluntain-app/ : 프론트엔드
    ├── components/
    │   ├── NavigationBar.js : 네비게이션 바 컴포넌트, _app.js에서 공통으로 전체 페이지에 포함됨.
    │   ├── MainBanner.js : 메인 페이지에 있는 남색 배너 컴포넌트, 커뮤니티 이름과 슬로건을 포함.
    │   ├── RecentLecture.js : 사용자가 시청 정보(쿠키)에 따라, 현재/다음 강의를 나타내는 컴포넌트 [호출: MainCookieCard]
    │   ├── MainCookieCard.js : 상위 RecentLecture 컴포넌트에서 전달받은 props를 나타내는 레이아웃 컴포넌트.
    │   ├── MainCard.js : 현재 등록된 course 정보를 백엔드에서 받아서 카드로 나타내는 컴포넌트 [호출: CourseCard]
    │   └── CourseCard.js : 상위 MainCard 컴포넌트에서 전달받은 props를 나타내는 레이아웃 컴포넌트
    │
    ├── config/
    │   └── next.config.js
    │
    ├── lib/
    │   └── ga/
    │   │   └── index.js
    │   └── context.js
    │
    ├── pages/
    │   ├── courses/
    │   │   └── [id].js : 강의 페이지
    │   ├── _app.js : Next.js에서 전체 컴포넌트 구조를 결정, 공통 컴포넌트(navbar, footer)가 선언되도록 customizing 됨.
    │   ├── _document.js : Next.js에서 전체 html 문서의 구조를 결정, lang 속성과 meta tag가 customizing 됨.
    │   ├── about.js : 단체 소개 페이지
    │   ├── index.js : 메인 페이지
    │   ├── question.js : Q&A 페이지
    │   └── setting.js : 쿠키, 구글 애널리틱스 정보 수집 정책 페이지
    │
    ├── public/
    │   ├── favicon.ico : 네비게이션바 이미지
    │   └── logo_about.png : about 페이지 로고 이미지
    │
    └── styles/
        └── Home.module.css

```
-->
