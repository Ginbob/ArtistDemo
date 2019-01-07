package de.burandt.artists.security.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ArtistUser {

    @Id
    private String username;
 
    @Column
    private String password;
 
    protected ArtistUser() {}
 
    public ArtistUser(String userName, String password) {
        this.username = userName;
        this.password = password;
    }
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
}
