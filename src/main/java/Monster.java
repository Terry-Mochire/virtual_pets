import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import java.util.List;

public class Monster {
    private String name;
    private int personId;
    private int id;

    private int foodLevel;
    private int sleepLevel;
    private int playLevel;

    public static final int MAX_FOOD_LEVEL = 3;
    public static final int MAX_SLEEP_LEVEL = 8;
    public static final int MAX_PLAY_LEVEL = 12;
    public static final int MIN_ALL_LEVELS = 0;

    public Monster(String name, int personId){
        this.name= name;
        this.personId= personId;
        this.playLevel = MAX_PLAY_LEVEL/2;
        this.sleepLevel = MAX_SLEEP_LEVEL/2;
        this.foodLevel = MAX_FOOD_LEVEL/2;
    }

    public String getName() {
        return name;
    }

    public int getPersonId() {
        return personId;
    }

    public int getId() {
        return id;
    }

    public int getPlayLevel() {
        return playLevel;
    }

    public int getSleepLevel() {
        return sleepLevel;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public boolean isAlive() {
        if (foodLevel <= MIN_ALL_LEVELS ||
                playLevel <= MIN_ALL_LEVELS ||
                sleepLevel <= MIN_ALL_LEVELS) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object otherMonster) {
        if(!(otherMonster instanceof Monster)){
            return false;
        } else {
            Monster newMonster = (Monster) otherMonster;
            return this.getName().equals(newMonster.getName()) &&
                    this.getPersonId() == newMonster.getPersonId();
        }
    }

    public void save(){
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO monsters (name, personId) VALUES (:name, :personId)";
            this.id =(int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .addParameter("personId", this.personId)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static List<Monster> all() {
        String sql = "SELECT * FROM monsters";
        try(Connection con = DB.sql2o.open()){
            return con.createQuery(sql).executeAndFetch(Monster.class);
        }
    }

    public static Monster find(int id) {
        try(Connection con = DB.sql2o.open()){
            String sql = "SELECT FROM monsters where id=:id";
            Monster monster = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Monster.class);
            return monster;
        }
    }

    public static void clearAll() {
        String sql = "DELETE from monsters *";
        try (Connection con = DB.sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    public void depleteLevels() {
        playLevel--;
        foodLevel--;
        sleepLevel--;
    }

    public void play() {
        if(playLevel >= MAX_PLAY_LEVEL){
            throw new UnsupportedOperationException("You cannot play with Monster anymore!");
        }
        playLevel ++;
    }


    public void sleep() {
        if (sleepLevel >= MAX_SLEEP_LEVEL){
            throw new UnsupportedOperationException("You cannot make your monster sleep anymore!");
        }
        sleepLevel ++;
    }

    public void feed() {
       if(foodLevel >= MAX_FOOD_LEVEL){
           throw new UnsupportedOperationException("You canot feed your monster anymore");
       }
       foodLevel ++;
    }
}
