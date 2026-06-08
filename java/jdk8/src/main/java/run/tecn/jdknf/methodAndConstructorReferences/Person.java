package run.tecn.jdknf.methodAndConstructorReferences;

import java.util.List;

public class Person {
    public String firstName;
    public String lastName;

    private List<String> names;
    public void setNames(List<String> names){
        this.names=names;
    }
    public List<String> getNames(){
        return names;
    }

    public Person() {}

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
