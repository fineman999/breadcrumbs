# [팀 과제] 노션에서 브로드 크럼스(Breadcrumbs) 만들기

## 목표

---

노션과 유사한 간단한 페이지 관리 API를 구현해주세요. 각 페이지는 제목, 컨텐츠, 그리고 서브 페이지를 가질 수 있습니다. 또한, 특정 페이지에 대한 브로드 크럼스(Breadcrumbs) 정보도 반환해야 합니다.

## 요구사항

---

**페이지 정보 조회 API**: 특정 페이지의 정보를 조회할 수 있는 API를 구현하세요.

- 입력: 페이지 ID
- 출력: 페이지 제목, 컨텐츠, 서브 페이지 리스트, **브로드 크럼스 ( 페이지 1 > 페이지 3 > 페이지 5)**
- **** 컨텐츠 내에서 서브페이지 위치 고려  X*

## 제출 방법 (팀단위)

---

- **과제 내용을 노션 혹은 github 등에 문서화해서 제출해주세요. (마감 9월 5일 오전 10시)**
- **필수**
    - 테이블 구조
    - 비지니스 로직 (Raw 쿼리로 구현 → ORM (X))
    - 결과  정보

        ```java
        {
        		"pageId" : 1,
        		"title" : 1,
        		"subPages" : [],
        		"breadcrumbs" : ["A", "B", "C",] // 혹은 "breadcrumbs" : "A / B / C"
        }
        ```

## 테이블 구조
<p align="center">
  <img src="https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F8f9374ea-d950-4add-878f-648f2923ed06%2F5a9ae71a-21ed-4ce8-9b45-5c6ca179f525%2FUntitled.png?table=block&id=e57e001e-1884-47e4-9423-bc3b6fa362b8&spaceId=8f9374ea-d950-4add-878f-648f2923ed06&width=1740&userId=2f121881-2289-461c-a96c-58cde2646312&cache=v2" alt="TABLE" width="500px" />
</p>

### 테이블 설명
1. id: 
- 이 열은 게시물 또는 게시글의 고유한 식별자
2. title:
- 게시물의 제목을 저장합니다.
3. content:
- 게시물의 내용을 저장
4. created_at:
- 게시물이 작성된 날짜와 시간을 저장
5. parent_id:
- 게시물의 부모 게시물을 나타낸다.
- 부모 게시물이 없는 경우 NULL 값을 가질 수 있다. 
- 이 열은 자기 참조하는 외래 키(Foreign Key)로 사용되며, boards 테이블 내의 다른 게시물의 id를 참조
- 이를 통해 계층적인 게시물 구조를 만들 수 있다.
6. FOREIGN KEY (parent_id) REFERENCES boards(id):
- 이 제약 조건은 parent_id 열이 boards 테이블의 id 열을 참조하도록 설정
- 즉, 부모 게시물의 id가 현재 테이블 내의 다른 게시물의 id를 참조할 수 있도록 한다.
- 게시물 간의 계층적인 관계를 나타내는 핵심 부분

## 주요 비지니스 로직
### BoardRepositoryImpl.Class 중
```java
@Override
public List<BoardSummary> findParentPagesById(Long id) {
    String sql = "WITH RECURSIVE breadcrumbs AS (" +
            "    SELECT id, parent_id, title " +
            "    FROM boards " +
            "    WHERE id = ? " +
            "    UNION ALL " +
            "    SELECT b.id, b.parent_id, b.title" +
            "    FROM boards AS b " +
            "    INNER JOIN breadcrumbs AS c ON b.id = c.parent_id " +
            ")" +
            "SELECT id, title FROM breadcrumbs";
    return jdbcTemplate.query(sql, summaryRowMapper(), id);
}
```
### SQL 쿼리 문자열 (sql 변수): 
- 이 코드에서 SQL 쿼리는 CTE(Common Table Expression)를 사용하여 재귀적으로 부모 페이지를 찾는다.
- 쿼리는 다음과 같은 단계로 실행된다.
1. 먼저, WITH RECURSIVE breadcrumbs AS ... 부분은 CTE를 정의(CTE는 재귀적으로 사용될 수 있는 임시 테이블처럼 동작)
2. 첫 번째 SELECT 문은 시작 지점으로부터 재귀를 시작. 
3. boards 테이블에서 id가 주어진 id와 일치하는 행의 id, parent_id, 그리고 title 열을 선택
4. UNION ALL을 사용하여 재귀적으로 다음 단계를 수행
5. 다음 단계에서는 boards 테이블과 breadcrumbs CTE를 조인하고, 현재 행의 parent_id가 이전 단계의 id와 일치하는 행을 선택
6. 이러한 재귀 단계는 부모 페이지를 찾을 때까지 반복

## 결과 정보
### 주요 테스트 코드
```java
@Sql("/insertBoardTest.sql")
@Test
@DisplayName("게시글 조회")
void get()  {
    BoardPage pageInfoById = boardService.findPageInfoById(30L);

    assertAll(
            () -> assertThat(pageInfoById.getId()).isEqualTo(30L),
            () -> assertThat(pageInfoById.getTitle()).isEqualTo("title3"),
            () -> assertThat(pageInfoById.getContent()).isEqualTo("content3"),
            () -> assertThat(pageInfoById.getSubPages().toString()).isEqualTo("[[\"id\": 70, \"title\": \"title7\"], [\"id\": 80, \"title\": \"title8\"]]"),
            () -> assertThat(pageInfoById.getBreadcrumbs().toString()).isEqualTo("title1 / title3")
    );
}
```
### 전체 결과
<p align="center">
  <img src="https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F8f9374ea-d950-4add-878f-648f2923ed06%2F4e8cb855-6a94-4bcc-864a-3caa778eb137%2FUntitled.png?table=block&id=11e96083-d772-4040-b11f-67b68ab0f6a6&spaceId=8f9374ea-d950-4add-878f-648f2923ed06&width=1740&userId=2f121881-2289-461c-a96c-58cde2646312&cache=v2" alt="TABLE" width="500px" />
</p>
