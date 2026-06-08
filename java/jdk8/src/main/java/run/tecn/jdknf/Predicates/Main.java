package run.tecn.jdknf.Predicates;

import java.util.Objects;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        //Predicate 接口是只有一个参数的返回布尔类型值的 断言型 接口
        Predicate<String> predicate = (s) -> s.length() > 0;

        System.out.println(predicate.test("foo") );             // true
        System.out.println(predicate.negate().test("foo"));     // false

        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull = Objects::isNull;

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();
    }
}
