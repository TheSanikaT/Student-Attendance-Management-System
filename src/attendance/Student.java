package attendance;

public class Student {
    private int id;
    private String name;
    private String standard;

    public Student(int id, String name, String standard){
        this.id = id;
        this.name = name;
        this.standard = standard;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getStandard(){
        return standard;
    }

}
