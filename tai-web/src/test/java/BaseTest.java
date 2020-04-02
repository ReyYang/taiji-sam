import com.taiji.boot.web.WebApplication;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Demo Test
 *
 * @author ydy
 * @date 2020/4/2 17:44
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@WebAppConfiguration
public abstract class BaseTest {

    @Resource
    private SecurityManager securityManager;

    @After
    public void tearDown() {
        //退出时请解除绑定Subject到线程 否则对下次测试造成影响
        ThreadContext.unbindSubject();
    }

    protected void login(String username, String password) {

        SecurityUtils.setSecurityManager(securityManager);

        //得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        subject.login(token);
    }

    public Subject subject() {
        return SecurityUtils.getSubject();
    }
}
