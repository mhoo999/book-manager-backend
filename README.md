# 📚 북매니저(Book Manager) - 포트폴리오 프로젝트

> **Spring Boot + React 기반 통합 도서관 관리 시스템**

도서관의 도서, 회원, 대여, 희망도서, 공지, 문의, 관리자 등을 통합 관리하는 풀스택 도서관 관리 시스템입니다.

---

## 🎯 프로젝트 개요

### 📋 프로젝트 목적
- **도서관 관리의 디지털화**: 기존 수동 관리 방식을 자동화하여 효율성 증대
- **사용자 경험 개선**: 직관적인 UI/UX로 도서 검색 및 대여 프로세스 간소화
- **관리자 업무 효율화**: 통합 대시보드와 자동화된 관리 기능 제공
- **확장 가능한 아키텍처**: 마이크로서비스 아키텍처 패턴 적용으로 시스템 확장성 확보

### 🏗️ 시스템 구성
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Frontend │◄──►│ Spring Boot API │◄──►│   MariaDB/Redis │
│   (User/Admin)   │    │   (Backend)     │    │   (Database)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🛠️ 기술 스택

### Backend (Spring Boot)
| 분류 | 기술 | 버전 | 용도 |
|------|------|------|------|
| **Language** | Java | 17 | 백엔드 개발 언어 |
| **Framework** | Spring Boot | 3.x | 웹 애플리케이션 프레임워크 |
| **ORM** | Spring Data JPA | - | 데이터 접근 계층 |
| **Query** | QueryDSL | - | 동적 쿼리 생성 |
| **Security** | Spring Security | - | 인증/인가 처리 |
| **Database** | MariaDB | - | 메인 데이터베이스 |
| **Cache** | Redis | - | 세션/캐시 저장소 |
| **Template** | Thymeleaf | - | 관리자 웹 UI |
| **Build** | Gradle | - | 프로젝트 빌드 도구 |
| **Container** | Docker | - | 개발 환경 표준화 |

### Frontend (React)
| 분류 | 기술 | 버전 | 용도 |
|------|------|------|------|
| **Framework** | React | 19 | 프론트엔드 라이브러리 |
| **State** | Redux Toolkit | - | 전역 상태 관리 |
| **Routing** | React Router | v7 | 클라이언트 사이드 라우팅 |
| **Styling** | Styled-components | - | CSS-in-JS 스타일링 |
| **HTTP Client** | Axios | - | API 통신 |
| **Deploy** | GitHub Pages | - | 정적 사이트 호스팅 |

---

## 🏛️ 시스템 아키텍처

### 📁 프로젝트 구조
```
book-manager-backend/
├── src/main/java/sesac/bookmanager/
│   ├── admin/           # 관리자 관련 기능
│   ├── auth/            # 인증/인가 처리
│   ├── book/            # 도서 관리
│   ├── category/        # 카테고리 관리
│   ├── notice/          # 공지사항
│   ├── question/        # 문의사항
│   ├── rent/            # 대여 관리
│   ├── reply/           # 답변 관리
│   ├── user/            # 사용자 관리
│   ├── wish/            # 희망도서
│   ├── security/        # 보안 설정
│   └── global/          # 전역 설정
├── src/main/resources/
│   ├── application.yml  # 환경 설정
│   ├── static/          # 정적 리소스
│   └── templates/       # Thymeleaf 템플릿
├── db-init/             # 데이터베이스 초기화
└── docker-compose.yml   # 컨테이너 설정
```

### 🔄 데이터 흐름
1. **사용자 요청** → React Frontend
2. **API 호출** → Spring Boot Backend
3. **비즈니스 로직 처리** → Service Layer
4. **데이터 접근** → Repository Layer
5. **데이터베이스 조작** → MariaDB/Redis
6. **응답 반환** → Frontend 렌더링

---

## 🚀 주요 기능

### 📖 도서 관리
- **도서 CRUD**: 등록, 조회, 수정, 삭제
- **재고 관리**: 도서 수량 및 상태 관리
- **카테고리 분류**: 체계적인 도서 분류 시스템
- **검색 기능**: 제목, 저자, 카테고리별 검색

### 👥 회원 관리
- **회원 가입/로그인**: JWT 기반 인증 시스템
- **프로필 관리**: 개인정보 수정, 비밀번호 변경
- **회원 상태 관리**: 활성/비활성/탈퇴 상태 관리

### 📚 대여 시스템
- **대여/반납**: 도서 대여 및 반납 처리
- **연체 관리**: 자동 연체 계산 및 알림
- **대여 이력**: 개인별 대여 기록 조회
- **상태 추적**: 대여 중/반납 완료/연체 상태 관리

### 💭 게시판 시스템
- **공지사항**: 관리자 공지 등록 및 관리
- **문의사항**: 사용자 문의 등록 및 답변
- **희망도서**: 사용자 희망도서 신청 및 관리

### 🔐 보안 및 인증
- **JWT 토큰**: Stateless 인증 방식
- **권한 관리**: 관리자/일반 사용자 권한 분리
- **Spring Security**: 보안 필터 및 설정

---

## 🔌 API 설계

### RESTful API 패턴
```
GET    /api/books          # 도서 목록 조회
POST   /api/books          # 도서 등록
GET    /api/books/{id}     # 도서 상세 조회
PUT    /api/books/{id}     # 도서 수정
DELETE /api/books/{id}     # 도서 삭제

GET    /api/rents          # 대여 목록 조회
POST   /api/rents          # 대여 신청
PUT    /api/rents/{id}     # 대여 상태 변경
```

### 응답 형식
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "도서 제목",
    "author": "저자명"
  },
  "message": "처리 완료"
}
```

---

## 🗄️ 데이터베이스 설계

### 주요 엔티티 관계
```
User (사용자)
├── Rent (대여) → Book (도서)
├── Wish (희망도서)
├── Question (문의)
└── Notice (공지사항)

Book (도서)
├── Category (카테고리)
└── BookItem (도서 항목)

Admin (관리자)
├── Notice (공지사항)
└── Reply (답변)
```

### 핵심 테이블
- **users**: 사용자 정보
- **books**: 도서 정보
- **rents**: 대여 기록
- **categories**: 도서 카테고리
- **notices**: 공지사항
- **questions**: 문의사항
- **wishes**: 희망도서
- **admins**: 관리자 정보

---

## 🔒 보안 및 인증

### JWT 토큰 기반 인증
```java
// 토큰 생성
String token = jwtTokenProvider.createToken(userDetails);

// 토큰 검증
if (jwtTokenProvider.validateToken(token)) {
    // 인증 성공
}
```

### Spring Security 설정
- **인증 필터**: JWT 토큰 검증
- **권한 관리**: ROLE_ADMIN, ROLE_USER 분리
- **CORS 설정**: 프론트엔드와의 통신 허용
- **CSRF 보호**: API 기반 시스템으로 비활성화

---

## 🔗 프론트엔드 연동

### API 통신 설정
```javascript
// Axios 인스턴스 설정
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  }
});
```

### 주요 연동 기능
- **도서 검색**: 실시간 검색 및 필터링
- **대여 관리**: 대여/반납 상태 실시간 업데이트
- **사용자 인증**: 로그인/로그아웃 상태 관리
- **게시판**: 공지사항 및 문의사항 CRUD

---

## 🚀 설치 및 실행

### 1. 필수 환경
```bash
# Java 17 이상
java -version

# Docker & Docker Compose
docker --version
docker-compose --version
```

### 2. 데이터베이스 실행
```bash
# MariaDB & Redis 컨테이너 실행
docker-compose up -d
```

### 3. 애플리케이션 실행
```bash
# Gradle 빌드 및 실행
./gradlew bootRun
```

### 4. 프론트엔드 실행
```bash
# 프론트엔드 저장소 클론
git clone https://github.com/mhoo999/book-manager-frontend.git

# 의존성 설치 및 실행
npm install
npm start
```

### 5. 접속 정보
- **백엔드 API**: http://localhost:8080
- **프론트엔드**: http://localhost:3000
- **관리자 계정**: admin001 / admin1234

---

## 📈 개발 과정 및 학습 내용

### 🎯 주요 학습 포인트
1. **Spring Boot 3.x**: 최신 Spring Boot 기능 활용
2. **JPA & QueryDSL**: 효율적인 데이터 접근 패턴
3. **JWT 인증**: Stateless 인증 시스템 구현
4. **RESTful API**: 표준 API 설계 패턴 적용
5. **Docker**: 컨테이너 기반 개발 환경 구축
6. **프론트엔드 연동**: CORS 및 API 통신 구현

### 🔧 기술적 도전과 해결
- **성능 최적화**: QueryDSL을 활용한 동적 쿼리 최적화
- **보안 강화**: Spring Security와 JWT를 통한 안전한 인증
- **확장성 고려**: 마이크로서비스 아키텍처 패턴 적용
- **사용자 경험**: 직관적인 UI/UX 설계

---

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

---

> **포트폴리오 프로젝트** - Spring Boot와 React를 활용한 풀스택 도서관 관리 시스템  
> 개발 기간: 2024년 | 기술 스택: Java, Spring Boot, React, MariaDB, Docker 
