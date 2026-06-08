package run.tecn.jdknf.Optionals;

import run.tecn.jdknf.methodAndConstructorReferences.Person;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        //of（）：为非null的值创建一个Optional
        Optional<String> optional = Optional.of("bam");
        // isPresent（）： 如果值存在返回true，否则返回false
        optional.isPresent();           // true
        //get()：如果Optional有值则将其返回，否则抛出NoSuchElementException
        optional.get();                 // "bam"
        //orElse（）：如果有值则将其返回，否则返回指定的其它值
        optional.orElse("fallback");    // "bam"
        //ifPresent（）：如果Optional实例有值则为其调用consumer，否则不做处理
        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
    }
    public static String getName(Person u) {
        return Optional.ofNullable(u)
                .map(user->user.getFullName())
                .orElse("Unknown");
    }
    /*
    public static String getChampionName(Competition comp) throws IllegalArgumentException {
        if (comp != null) {
            CompResult result = comp.getResult();
            if (result != null) {
                User champion = result.getChampion();
                if (champion != null) {
                    return champion.getName();
                }
            }
        }
        throw new IllegalArgumentException("The value of param comp isn't available.");
    }

    public static String getChampionName(Competition comp) throws IllegalArgumentException {
        return Optional.ofNullable(comp)
                .map(c->c.getResult())
                .map(r->r.getChampion())
                .map(u->u.getName())
                .orElseThrow(()->new IllegalArgumentException("The value of param comp isn't available."));
    }

     */
}
