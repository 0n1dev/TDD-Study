import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ScoreCollectionTest {

	@Test
	public void test() {
		// 준비
		ScoreCollection collection = new ScoreCollection();
		collection.add(() -> 5);
		collection.add(() -> 7);

		// 실행
		int actualResult = collection.arithmeticMean();

		// 단언
		assertThat(actualResult, equalTo(6));
	}
}