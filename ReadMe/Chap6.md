# Right-BICEP: 무엇을 테스트할 것인가?

> Right-BICEP 무엇을 테스트할지에 대해 쉽게 선별

- Right 결과가 올바른가?
- B 경계 조건(boundary conditions)은 맞는가?
- I 역 관계(inverse relationship)를 검사할 수 있는가?
- C 다른 수단을 활용하여 교차 검사(cross-check)할 수 있는가?
- E 오류 조건(error conditions)을 강제로 일어나게 할 수 있는가?
- P 성능 조건(performance characteristics)은 기준에 부합하는가?

## [Right]-BICEP 결과가 올바른가?

테스트 코드는 무엇보다도 먼저 기대한 결과를 산출하는지 검증할 수 있어야 함

```java
@Test
public void answersArithmeticMeanOfTwoNumbers() {
    ScoreCollection collection = new ScoreCollection();
    collection.add(() -> 5);
    collection.add(() -> 7);
 
    int actualResult = collection.arithmeticMean();
 
    assertThat(actualResult, equalTo(6));
}
```

- 테스트 코드는 먼저 기대한 결과를 선출하는지 검증할 수 있어야 함
- "해당 코드가 정상적으로 동작한다면 어떠한 입력 값이 들어오더라도 정상적으로 출력되는 결과를 알수 있나?"

## Right-[B]ICEP 경계 조건은 맞는가?

> 수많은 결함은 경계 조건에 해당하는 테스트 케이스에서 많이 나오기 때문에 경계 조건을 처리해야 함

- 모호하고 일관성 없는 값
- 잘못된 양식의 데이터
- 수치적 오버플로를 일으키는 계산
- 비거나 빠진 값
- 이성적인 기댓값을 훨씬 벗어나는 값
- 중복이 허용 안되는 목록에 중복이 있는 경우
- 정렬이 안 된 정렬 리스트 혹은 그 반대.
- 시간 순이 맞지 않는 경우

