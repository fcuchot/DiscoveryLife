package fr.intech.discoverylife.Classes;

/**
 * Created by famille on 14/09/2016.
 */
public class Landmark {

    private int id;
    private String title;
    private String description;
    private int idUser;
    private float latitude;
    private float longitude;

    public Landmark()
    {
    }
    public Landmark(int id, String title, String description, int idUser, float latitude, float longitude)
    {
        this.id=id;
        this.title=title;
        this.description=description;
        this.idUser = idUser;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Landmark(String title, String description, int idUser, float latitude, float longitude)
    {
        this.title=title;
        this.description=description;
        this.idUser = idUser;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public void setLatitude(float latitude) { this.latitude = latitude;}

    public  void setLongitude(float longitude) {this.longitude = longitude;}

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

    public float getLongitude() { return  longitude;}

    public float  getLatitude() { return latitude;}
}
