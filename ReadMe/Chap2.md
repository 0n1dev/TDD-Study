# JUnit 진짜로 써보기

## 테스트 대상 이해: Profile 클래스

- 테스트 대상에 대해 이해를 하고 그에 대한 테스트 코드 작성

## 어떤 테스트를 작성할 수 있는지 결정

- 일부 복잡한 메서드에서는 테스트 코드를 작게는 수십개 혹은 수백 개 작성할 수 있다.
- 코드에서 분기점이나 잠재적으로 영향력이 큰 데이터 변형들도 고려 가능

### 테스트 고려 사항
- Criteria 인스턴스가 Criterion 객체를 포함하지 않을 때(27행)
- Criteria 인스턴스가 다수의 Criterion 객체를 포함할 때(27행)
- answers.get()에서 반환된 Answer 객체가 null일 때(29행)
- criterion.getAnswer() 혹은 criterion.getAnswer().getQuestion Text()의 반환값이 null일 때(29행)
- criterion.getWeight()의 반환값이 Weight.DontCare여서 match 변수가 true일 때(30행)
- value 변수와 criterion.getWeight()가 매칭되어 match 변수가 true일 때(30행)
- 두 조건문이 모두 false여서 결과적으로 match 변수가 false가 될 때(30행)
- match 변수가 false이고 criterion.getWeight()가 Weight.MustMatch여서 kill 변수가 true일 때(34행)
- match 변수가 true이기 때문에 kill 변수가 변하지 않을 때(34행)
- criterion.getWeight()가 Weight.MustMatch가 아니기 때문에 kill 변수가 변하지 않을 때(34행)
- match 변수가 true이기 때문에 score 변수가 업데이트되었을 때(37행)
- match 변수가 false이기 때문에 score 변수가 업데이트되지 않았을 때(37행)
- kill 변수가 true이기 때문에 matches 메서드가 false를 반환할 때(42행)
- kill 변수가 false이고 anyMatches 변수가 true이기 때문에 matches 메서드가 true를 반환할 때(42행과 44행)
- kill 변수가 false이고 anyMatches 변수가 false이기 때문에 matches 메서드가 false를 반환할 때(42행과 44행)

-> 테스트 코드 작성 시 가장 신경 쓰는 부분이 어디인지 알고 있어야 함

## 단일 경로 커버

```java
@Test
public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
    Profile profile = new Profile("Profile");
    Question question = new BooleanQuestion(1, "YesOrNoQuestion");
    Answer profileAnswer = new Answer(question, Bool.FALSE);
    profile.add(profileAnswer);
    Criteria criteria = new Criteria();
    Answer criteriaAnswer = new Answer(question, Bool.TRUE);
    Criterion criterion = new Criterion(criteriaAnswer, 
                                        Weight.MustMatch);
    criteria.add(criterion);
 
    boolean matches = profile.matches(criteria);
 
    assertFalse(matches);
}
```

- 20줄도 안되는 메서드를 15개씩 10줄 정도 되는 테스트를 만드는것은 너무 과하다..

## 두 번째 테스트 만들기

```java
@Test
public void matchAnswersTrueForAnyDontCareCriteria() {
    Profile profile = new Profile("Profile");
    Question question = new BooleanQuestion(1, "YesOrNoQuestion");
    Answer profileAnswer = new Answer(question, Bool.FALSE);
    profile.add(profileAnswer);
    Criteria criteria = new Criteria();
    Answer criteriaAnswer = new Answer(question, Bool.TRUE);
    Criterion criterion = new Criterion(criteriaAnswer, 
                                        Weight.DontCare);
    criteria.add(criterion);
 
    boolean matches = profile.matches(criteria);
 
    assertTrue(matches);
}
```

## @Before 메서드로 테스트 초기화

- 두 테스트 코드에 공통적인 초기화 코드 리팩토링

```java
public class ProfileTest {
    private Profile profile;
    private BooleanQuestion question;
    private Criteria criteria;
 
    @Before
    public void create() {
        profile = new Profile("Profile");
        question = new BooleanQuestion(1, "YesOrNoQuestion");
        criteria = new Criteria();
    }
 
    @Test
    public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
        Answer profileAnswer = new Answer(question, Bool.FALSE);
        profile.add(profileAnswer);
        Answer criteriaAnswer = new Answer(question, Bool.TRUE);
        Criterion criterion = new Criterion(criteriaAnswer, 
                                            Weight.MustMatch);
        criteria.add(criterion);
 
        boolean matches = profile.matches(criteria);
 
        assertFalse(matches);
    }
 
    @Test
    public void matchAnswersTrueForAnyDontCareCriteria() {
        Answer profileAnswer = new Answer(question, Bool.FALSE);
        profile.add(profileAnswer);
        Answer criteriaAnswer = new Answer(question, Bool.TRUE);
        Criterion criterion = new Criterion(criteriaAnswer, 
                                            Weight.DontCare);
        criteria.add(criterion);
 
        boolean matches = profile.matches(criteria);
 
        assertTrue(matches);
    }
}
```

- `@Before`는 `@Test` 어노테이션이 붙은 메서드보다 먼저 실행
    - `@Test` 어노테이션의 갯수만큼 실행되는 어노테이션
