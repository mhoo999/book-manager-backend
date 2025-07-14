# 북매니저(Book Manager)

도서관의 도서, 회원, 대여, 희망도서, 공지, 문의, 관리자 등을 통합 관리하는 Spring Boot 기반의 통합 도서관 관리 시스템입니다.

---

## 🏗️ 주요 기술 스택
- **Java 17**
- **Spring Boot 3.x**
- Spring Data JPA, MyBatis, QueryDSL
- Spring Security, JWT 인증
- MariaDB, Redis
- Thymeleaf (관리자 웹 UI)
- Docker, docker-compose
- Gradle

---

## 📁 폴더 구조

```
book-manager/
├── db-init/                # DB 초기화 및 샘플 데이터 SQL
├── src/
│   └── main/
│       ├── java/sesac/bookmanager/  # 백엔드 Java 소스
│       └── resources/
│           ├── application.yml      # 환경설정
│           ├── static/              # 정적 리소스(JS 등)
│           └── templates/           # Thymeleaf 템플릿(관리자 UI)
├── build.gradle            # Gradle 빌드 설정
├── docker-compose.yml      # DB/Redis 컨테이너 설정
└── ...
```

---

## 🚀 설치 및 실행 방법

1. **필수 환경**
   - Java 17 이상
   - Docker, docker-compose

2. **DB/Redis 컨테이너 실행**
   ```bash
   docker-compose up -d
   ```
   - MariaDB: 3306 포트, DB명: `book_manager`, 계정: `myuser`/`mypass`
   - Redis: 6379 포트

3. **DB 초기화**
   - `db-init/` 폴더의 SQL 파일이 컨테이너 기동 시 자동 실행되어 테이블/샘플 데이터가 생성됩니다.

4. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```
   - 기본 포트: 8080

5. **관리자 로그인**
   - 최초 관리자 계정:  
     - 아이디: `admin001`  
     - 비밀번호: `admin1234`

---

## 🗂️ 주요 기능 및 화면

### 1. 관리자 대시보드
- 대여/회원/도서/희망도서/문의/공지 등 통계 및 현황 요약 제공

### 2. 도서 관리
- 도서 등록/수정/삭제, 도서 목록/검색, 재고 관리, 카테고리 분류

### 3. 회원 관리
- 회원 목록/검색, 상세정보, 탈퇴 회원 관리, 비밀번호 변경 등

### 4. 대여 관리
- 대여 신청/반납/연장/미납 관리, 대여 이력 조회, 상태별 필터링

### 5. 희망도서 관리
- 회원 희망도서 신청 내역 확인 및 처리

### 6. 게시판 관리
- 공지사항, 문의(질문/오류신고), 답변(Reply) 등록 및 관리

### 7. 관리자 관리
- 관리자 계정 등록/수정/삭제, 목록, 상세정보

### 8. 인증 및 보안
- Spring Security 기반 로그인/로그아웃, JWT 토큰, 관리자/회원 권한 분리

---

## ⚙️ 환경설정(application.yml)
- DB, Redis, JWT, 파일 업로드 등 주요 설정은 `src/main/resources/application.yml`에서 관리합니다.

---

## 🗄️ 데이터베이스 구조 및 샘플 데이터
- `db-init/` 폴더 내 SQL 파일 참고 (테이블 생성, 카테고리/도서/회원/관리자/공지/문의/희망도서 등 샘플 데이터 포함)

---

## 📞 문의
- 시스템/설치/운영 관련 문의: admin@bookmanager.com

---

## 📝 기타
- 관리자 UI는 TailwindCSS + Thymeleaf 기반으로 반응형으로 제작되어 있습니다.
- 네비게이션 메뉴는 마우스 오버 시 하위 메뉴가 표시됩니다.
- 모든 기능은 관리자 인증 후 사용 가능합니다.

---

> 본 프로젝트는 도서관 관리 시스템의 예시/교육/실습용으로 제작되었습니다. 

---

## 🌐 프론트엔드(React) 안내

- 본 프로젝트의 프론트엔드는 별도 저장소(bookmanager-frontend)에서 관리됩니다.
- 주요 기술스택: React 19, Redux Toolkit, React Router v7, Styled-components, Axios 등
- 폴더 구조, 실행 방법, 배포 방식 등은 [프론트엔드 README](https://github.com/mhoo999/book-manager-frontend) 참고
- 프론트엔드 개발 서버: http://localhost:3000
- API 연동 기본 주소: http://localhost:8080 (Spring Boot 백엔드)

## 🖥️ 전체 시스템 구성

```
[사용자/관리자 브라우저]
        │
        ▼
[React 프론트엔드]  ←→  [Spring Boot 백엔드]  ←→  [MariaDB, Redis]
```

- 프론트엔드는 사용자/관리자 UI, API 요청 담당
- 백엔드는 비즈니스 로직, 데이터베이스, 인증/보안 담당

## 🧑‍💻 기여 방법

1. 이슈 등록 및 브랜치 생성
2. 기능 개발 및 커밋
3. PR(Pull Request) 생성

## 🪪 라이선스

- 별도 명시가 없는 한, 본 프로젝트는 MIT 라이선스를 따릅니다. 
