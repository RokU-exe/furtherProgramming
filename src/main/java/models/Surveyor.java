package models;

public class Surveyor {
    private int id;
    private String name;

    // Constructor that takes an integer ID
    public Surveyor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor that takes a string ID and converts it to an integer
    public Surveyor(String id, String name) {
        this.id = Integer.parseInt(id);
        this.name = name;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Setter for ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for Name
    public String getName() {
        return name;
    }

    // Setter for Name
    public void setName(String name) {
        this.name = name;
    }

    // Override toString for better readability if needed
    @Override
    public String toString() {
        return "Surveyor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
