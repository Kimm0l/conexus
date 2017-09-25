package es.demosix.conexus.Modelo;

import java.util.ArrayList;

/**
 * Created by Ximo on 20/01/2017.
 */

public class User {

    private String email;
    private String grupo;
    private ArrayList<String> dispositivos;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void setDispositivos(ArrayList<String> dispositivos) {
        this.dispositivos = dispositivos;
    }

    public String getEmail() {

        return email;
    }

    public String getGrupo() {
        return grupo;
    }

    public ArrayList<String> getDispositivos() {
        return dispositivos;
    }

    public User(String email, String grupo, ArrayList<String> dispositivos) {

        this.email = email;
        this.grupo = grupo;
        this.dispositivos = dispositivos;
    }
}
