package run.tecn.jdknf.methodAndConstructorReferences;

public interface PersonFactory<P extends Person> {
    P create(String firstName,String lastName);
}
