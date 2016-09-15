package fr.intech.discoverylife.Classes;

/**
 * Created by famille on 14/09/2016.
 */
public class Landmark {

    private int id;
    private String title;
    private String description;
    private int idUser;

    public Landmark()
    {
    }
    public Landmark(int id, String title, String description, int idUser)
    {
        this.id=id;
        this.title=title;
        this.description=description;
        this.idUser = idUser;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getIdUser(){
        return idUser;
    }
}
