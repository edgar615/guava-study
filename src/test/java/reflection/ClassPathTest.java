package reflection;

import com.google.common.reflect.ClassPath;

import java.io.IOException;

/**
 * Created by Edgar on 2017/8/23.
 * lassPath Reflection: Mirror mirror on the wall
 * <p>
 * When inspecting Java’s Reflection capabilities, the ability to inspect our own code, you’ll
 * find that there’s no simple way to get a list of all the classes in your package or project.
 * This is one of the Guava features we really like, as it helps get more information about the
 * environment you’re running on. It works as simple as that:
 * <p>
 * ClassPath classpath = ClassPath.from(classloader);
 * for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClasses("com.mycomp.mypackage")) {
 * System.out.println(classInfo.getName());
 * }
 * <p>
 * This snippet will loop through and print out all the class names in the package we specified.
 * One thing worth mentioning here is that the scan includes only the classes that are physically
 * under the package we mention. It will not include classes loaded from other places, so be
 * careful with what you use it for as it will sometimes give you an incomplete picture.
 *
 * @author Edgar  Date 2017/8/23
 */
public class ClassPathTest {
  public static void main(String[] args) throws IOException {
    ClassPath classpath = ClassPath.from(ClassPathTest.class.getClassLoader());
    for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClasses("base")) {
      System.out.println(classInfo.getName());
    }
  }
}
