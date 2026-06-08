package run.tecn.jdknf.functionalInterface;

@FunctionalInterface
public interface Converter<F,T> {
    T convert(F from);
}
