# 테스트 조직

## AAA로 테스트 일관성 유지

- 테스트 코드를 가시적으로 `준비`, `실행`, `단언` 부분으로 조직 (트리플 -A)
    - 준비(Arrange): 테스트 코드를 실행하기 전에 시스템이 적절한 상태에 있는지 확인
    - 실행(Act): 테스트 코드를 실행, 보통은 단일 메서드를 호출
    - 단연(Assert): 실행한 코드가 기대한 대로 동작하는지 확인
    - 사후(After): 테스트를 실행할 때 특정 자원을 할당했으면 정리가 되었는지 확인 (때에 따라 필요한 단계)

## 동작 테스트 vs 메서드 테스트

- 테스트 작성시 클래스 동작에만 집중해야하며 개별 메서드를 테스트한다고 생각하면 안됨
- 은행 ATM의 예시
    - `deposit()`, `withdraw()`, `getBalance()`가 있다.
    - 테스트로 `makeSigleDeposit`과 `makeMultipleDeposits`이 있다.
    - 모든 출금 테스트는 잔고 확인이 필요 (그렇지 않으면 효과적인 테스트 코드 작성이 어려움)

## 테스트와 프로덕션 코드의 관계

- JUnit 테스트는 프로덕션 코드와 같은 프로젝트에 위치할 수 있음
- 테스트 코드는 프로덕션 코드와 분리는 되어야 함
- 테스트 코드는 프로덕션 코드에 의존하지만, 프로덕션 코드는 테스트 코드의 존재를 모름

### 테스트와 프로덕션 코드 분리

- 테스트 코드를 프로덕션에 포함하여 배포하면 성능 저하와, 코드 베이스의 공격 표면이 늘어나지만 넣지 않을 이유도 없다.
- 테스트 배포  여부를 고려하기보다는 테스트를 프로덕션 코드와 같은 프로젝트에 넣을지 고민해야함
    - 테스트를 프로덕션 코드와 같은 디렉터리 및 패키지에 넣기
        - 실제 배포시 테스트 코드를 걷어 내는 스크립트 필요
        - 테스트 클래스 여부를 식별할 수 있는 리플렉션 코드 작성
    - 테스트를 별도 디렉터리로 분리하지만 프로덕션 코드와 같은 패키지에 넣기
        - 대부분의 회사에서 선택
    - 테스트를 별도의 디렉터리와 유사한 패키지에 유지하기

### 내부 데이터 노출 vs 내부 동작 노출

- 내부의 세부 사항을 테스트하는 것은 저품질로 이어질수 있음
- 테스트 코드가 과도하게 내부적인 구현 사항을 알고있으면 작은 변경에도 테스트코드가 깨짐
- 리팩토링이 꺼려짐
- 외부로 필드를 공개하지 않을수록 편안해짐
- 테스트가 프로덕션 코드와 같은 패키지에 있으면 그 클래스에 대해 패키지 수준으로 접근할 수 있음
- 내부 행위를 테스트하려는 것은 설계에 문제가 있는 것

## 집중적인 단일 목적 테스트의 가치

```java
@Test
public void matches() {
    Profile profile = new Profile("Bull Hockey, Inc.");
    Question question = new BooleanQuestion(1, "Got milk?");
 
    // must-match 항목이 맞지 않으면 false
    profile.add(new Answer(question, Bool.FALSE));
    Criteria criteria = new Criteria();
    criteria.add(
        new Criterion(new Answer(question, Bool.TRUE),
                       Weight.MustMatch));
 
    assertFalse(profile.matches(criteria));
 
    // don't care 항목에 대해서는 true
    profile.add(new Answer(question, Bool.FALSE));
    criteria = new Criteria();
    criteria.add(
        new Criterion(new Answer(question, Bool.TRUE), 
                      Weight.DontCare));
 
    assertTrue(profile.matches(criteria));
}
```
- 테스트 코드를 합치자
- 공통 초기화의 부담은 줄였으나, 테스트 고립의 중요한 이점을 잃음

## 문서로서의 테스트

- 일관성 있는 이름으로 테스트 문서화
    - 나쁜 예) makeSingleWithdrawl
    - 좋은 예) withdrawalReducesBalanceByWithdrawnAmount
    - `doingSomeOperationGeneratesSomeResult` - 어떤 동작을 하면 어떤 결과가 나온다
    - `someResultOccursUnderSomeCondition` - 어떤 결과는 어떤 조건에서 발생한다
    - `givenSomeContextWhenDoingSomeBehaviorThenSomeResultOccurs` - 주어진 조건에서 어떤 일을 하면 어떤 결과가 나온다.
    - `whenDoingSomeBehaviorThenSomeResultOccurs` - 어떤 일을 하면 어떤 결과가 나온다
- 테스트를 의미 있게 만들기
    - 지역 변수 이름 개선하기
    - 의미 있는 상수 도입하기
    - 햄크레스트 단언 사용하기
    - 커다란 테스트를 작게 나누어 집중적인 테스트 만들기
    - 테스트 군더더기들을 도우미 메서드와 `@Before` 메서드로 이동하기

## `@Before`와 `@After` 공통 초기화와 정리 더 알기

- `@Before`
    - 중복된 코드를 줄여줌 (공통 초기화 코드)
    - `@Before` 메서드가 여러개가 있으면 순서를 보장하지 않는다.
- `@After`
    - 테스트가 실패하더라도 실행
    - 보통 테스트 진행시 발생하는 부산물들을 정리할때 사용
- `@BeforeClass`, `@AfterClass`
    - 클래스 수준의 초기화
    - 어떤 테스트를 처음 실행 또는 끝났을때 한번 동작

## 녹색이 좋다: 테스트를 의미 있게 유지

- 실패하는 테스트가 있으면 더 테스트 케이스를 늘리는게 아니라 고쳐서 테스트가 통과하도록 수정해야 함
- 다수 테스트가 실패시 `@Ignore`을 사용하여 피한 뒤 천천히 해결해 나가는게 좋음