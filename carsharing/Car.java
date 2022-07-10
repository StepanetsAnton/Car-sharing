package carsharing;

public class Car {
    private int id;
    private String name;
    private int companyId;
    Car(int id, int comp, String name){
        this.id = id;
        this.name = name;
        this.companyId=comp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
