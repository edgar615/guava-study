package base;

import com.google.common.net.InternetDomainName;

/**
 * Created by Edgar on 2017/8/23.
 * Another cool little utility with Guava is an InternetDomainName, which unsurprisingly helps
 * parse and manipulate domain names. If you’ve ever written a similar utility yourself, you’ll
 * appreciate how this helps solve it quickly and in an elegant way. And valid according to
 * updating RFC specifications, using the list of domains from the Public Suffix List, an
 * initiative by the Mozilla foundation. Overall it also has more specific methods than the
 * apache-commons validator equivalent. Let’s see a quick example:
 * <p>
 * InternetDomainName owner =
 * InternetDomainName.from("blog.takipi.com").topPrivateDomain(); // returns takipi.com
 * <p>
 * InternetDomainName.isValid(“takipi.monsters"); // returns false
 * <p>
 * A few concepts that can be confusing around domain names:
 * publicSuffix() – The top domain that is a separate entity according to the the Public Suffix
 * List. So we’ll have results like co.uk, .com, .cool (yes, it’s a real suffix and javais.cool,
 * scalais.cool & cppis.cool).
 * topPrivateDomain() – The top domain that is a separate entity according to the the Public
 * Suffix List (PSL). Applying it on blog.takipi.com returns takipi.com, BUT if you try it on a
 * Github pages site, username.github.io will retrurn username.github.io since it’s a separate
 * entity that appears on the PSL.
 *
 * @author Edgar  Date 2017/8/23
 */
public class NetTest {
  public static void main(String[] args) {
    InternetDomainName owner =
            InternetDomainName.from("blog.takipi.com").topPrivateDomain(); // returns takipi.com

    System.out.println(owner);
    InternetDomainName.isValid("takipi.monsters"); // returns false
  }
}
