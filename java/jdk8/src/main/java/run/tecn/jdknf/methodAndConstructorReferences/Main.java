package run.tecn.jdknf.methodAndConstructorReferences;

import run.tecn.jdknf.functionalInterface.Converter;

public class Main {
    public static void main(String[] args) {
        //引用静态方法
        Converter<String, Integer> converter = Integer::valueOf;
        Integer converted = converter.convert("123");
        System.out.println(converted.getClass());

        Something something=new Something();
        //引用对象方法
        Converter<String,String> converter2=something::startsWith;
        String converted2=converter2.convert("Java");
        System.out.println(converted2);

        //引用构造函数
        PersonFactory<Person> personFactory=Person::new;
        Person person=personFactory.create("Peter","Park");
        System.out.println(person.getFullName());
    }
}
