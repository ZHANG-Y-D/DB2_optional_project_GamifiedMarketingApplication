package it.polimi.db2.GMA.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Administrator", schema = "db_gamified_marketing_application")
@NamedQuery(name = "Admin.checkCredentials", query = "SELECT r FROM Administrator r  WHERE r.name = ?1 and r.password = ?2")


public class Administrator implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String password;

    public Administrator() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}