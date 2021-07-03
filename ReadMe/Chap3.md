# JUnit 단언 깊게 파기

### 단언

- 테스트에 넣을 수 있는 정적 메서드 호출
- 어떤 조건이 참인지 검증하는 방법
- 단언한 조건이 참이 아니면 테스트는 그자리에 멈추고 실패를 보고

## assertTrue

- `assertTrue`, `assertFalse` 메서드는 boolean 값으 리턴하는 메서드를 테스트하기 적합
- 테스트 코드는 특정 사례에 해당하기 때문에 검증하는 기댓값 또한 `명시적으로 지정`하는 것이 좋음

## assertThat

- 특정 값보다 크다고 하기보다는 명시적으로 기대하는 값을 단언

```java
assertThat(account.getBalance(), equalTo(100));
```

- 첫 번째 인자는 실제 값 (Actual)
- 두 번째 인자는 비교 값 (Matcher)
- 매처가 여러개 도입할수록 테스트 코드의 표현력이 깊어짐
- `equalTo`, `is`, `not`

## 예외를 기대하는 세 가지 방법

- 어노테이션 사용
    - `@Test(expected=InsufficientFundsException.class)`
    - 예외가 발생하면 테스트 통과
- 옛 방식) try/catch와 fail
    - `try/catch` 블록을 활용
    - 예외 처리가 발생하지 않으면 강제로 `fail()`메서드 호출
- ExpectedException
```java
@Rule
public ExpectedException thrown = ExpectedException.none();

@Test
public void exceptionRule() {
    thrown.expect(InsufficientFundsException.class);
    thrown.expectMessage("Balance only 0");

    account.withdraw(100);
}