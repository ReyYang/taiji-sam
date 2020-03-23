import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Demo test
 *
 * @author ydy
 * @date 2020/2/24 14:56
 */
public class test {
    public static void main(String[] args) {
        List<Double> doubleList = Lists.newArrayList();
        for (int i = 0; i < 10000; i++) {
            doubleList.add(new Random().nextDouble());
        }
        long startTime = System.currentTimeMillis();
        String join1 = String.join(",", doubleList.parallelStream().map(Object::toString).collect(Collectors.toList()));
        String join = Joiner.on(",").join(doubleList);
        System.out.println("Spend time " + (System.currentTimeMillis() - startTime));
        System.out.println(join1);

    }

    private static boolean testRex(String str) {
        String regex = "^[【].+[】]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    private static String getRealDomain(String domain) {
        if (StringUtils.isBlank(domain)) {
            return domain;
        }
        if (domain.startsWith("http:")) {
            domain = domain.replaceAll("http://", "");
        }
        if (domain.startsWith("https:")) {
            domain = domain.replaceAll("https://", "");
        }
        // 判断是否有 restful 接口，如：www.zaimi.com/api/xxx/xxx
        if (domain.contains("/")) {
            domain = domain.substring(0, domain.indexOf("/"));
        }
        // 判断是否含有请求参数，如：www.zaimi.com?c=xxxxxx
        if (domain.contains("?")) {
            domain = domain.substring(0, domain.indexOf("?"));
        }
        String pattern  = "([a-z0-9-]{1,200})\\.(ac.cn|bj.cn|sh.cn|tj.cn|cq.cn|he.cn|sn.cn|sx.cn|nm.cn|ln.cn|jl.cn|hl.cn|js.cn|zj.cn|ah.cn|fj.cn|jx.cn|sd.cn|ha.cn|hb.cn|hn.cn|gd.cn|gx.cn|hi.cn|sc.cn|gz.cn|yn.cn|gs.cn|qh.cn|nx.cn|xj.cn|tw.cn|hk.cn|mo.cn|xz.cn" +
                "|com.cn|net.cn|org.cn|gov.cn|.com.hk|我爱你|在线|中国|网址|网店|中文网|公司|网络|集团" +
                "|com|cn|cc|org|net|xin|xyz|vip|shop|top|club|wang|fun|info|online|tech|store|site|ltd|ink|biz|group|link|work|pro|mobi|ren|kim|name|tv|red" +
                "|cool|team|live|pub|company|zone|today|video|art|chat|gold|guru|show|life|love|email|fund|city|plus|design|social|center|world|auto|.rip|.ceo|.sale|.hk|.io|.gg|.tm|.gs|.us)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(domain);
        while (m.find()){
            domain = m.group();
        }
//        domain = domain.substring(domain.indexOf(".") + 1);
        return domain;
    }
}
