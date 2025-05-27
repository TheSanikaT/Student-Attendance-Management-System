package attendanceGUI;

public class Student {
    private int id;
    private String name;
    private String standard;
    private String imagePath;

    public Student(int id, String name, String standard, String imagePath){
        this.id = id;
        this.name = name;
        this.standard = standard;
        this.imagePath = imagePath;
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
    public String getImagePath(){
        return  imagePath;
    }
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }
}

