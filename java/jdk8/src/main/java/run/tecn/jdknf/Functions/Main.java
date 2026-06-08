package run.tecn.jdknf.Functions;

import run.tecn.jdknf.methodAndConstructorReferences.Person;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        String test="hello baby";
        System.out.println(test.substring(2));

        //Function 接口接受一个参数并生成结果。默认方法可用于将多个函数链接在一起（compose, andThen）
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        backToString.apply("123");     // "123"


        //Supplier 接口产生给定泛型类型的结果。 与 Function 接口不同，Supplier 接口不接受参数
        Supplier<Person> personSupplier = Person::new;
        personSupplier.get();   // new Person

        //Consumer 接口表示要对单个输入参数执行的操作。
        Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.getFullName());
        greeter.accept(new Person("Luke", "Skywalker"));

        //Comparator 是老Java中的经典接口， Java 8在此之上添加了多种默认方法：
        Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

        Person p1 = new Person("John", "Doe");
        Person p2 = new Person("Alice", "Wonderland");

        comparator.compare(p1, p2);             // > 0
        comparator.reversed().compare(p1, p2);  // < 0
    }
}
