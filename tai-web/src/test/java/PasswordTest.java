import com.taiji.boot.common.beans.exception.BaseException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Demo PasswordTest
 *
 * @author ydy
 * @date 2020/4/2 17:49
 */

public class PasswordTest extends BaseTest {

    @Test
    public void testLogin() {
        // Yang970913..@hui；
        login("yangyihui", "Yang970913..@hui");
    }
}
