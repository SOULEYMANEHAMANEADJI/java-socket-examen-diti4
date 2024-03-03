package isi.dev.javasocketexam.entities;




import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "membre")

public class Membre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",length = 50,unique = true)
    private String name;

    @Column(name = "full_name",length = 50)
    private String fullName;

    @Column(name = "password", length = 200)
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "gender",length = 10)
    private String gender;

    @Column(name = "phone_no",length = 10)
    private String phoneNo;

    @OneToMany(mappedBy = "membre")
    private List<Commentaire> commentaires;



    public Membre() {}

    public Membre(Long id, String name, String fullName, String password, String email, String gender, String phoneNo, List<Commentaire> commentaires) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.phoneNo = phoneNo;
        this.commentaires = commentaires;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }
}
