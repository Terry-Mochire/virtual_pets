import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import java.util.List;

public class Person {
    private String name;
    private String email;
    private int id;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object otherPerson){
        if (!(otherPerson instanceof Person)) {
            return false;
        } else {
            Person newPerson = (Person) otherPerson;
            return this.getName().equals(newPerson.getName()) &&
                    this.getEmail().equals(newPerson.getEmail());
        }
    }

    public void save() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO persons (name, email) VALUES (:name, :email)";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .addParameter("email", this.email)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static List<Person> all() {
        String sql = "SELECT * FROM persons";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Person.class);
        }
    }

    public int getId() {
        return id;
    }

    public static Person find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM persons where id=:id";
            Person person = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Person.class);
            return person;
        }
    }

    public List<Monster> getMonsters() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM monsters where personId=:id";
            return con.createQuery(sql)
                    .addParameter("id", this.id)
                    .executeAndFetch(Monster.class);
        }
    }

    public static void clearAll() {
        String sql = "DELETE from persons";
        try (Connection con = DB.sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

}