/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;


public class role {
    private int id_role ;
    private String type ;

    public role() {
    }

    public role(String type) {
        this.type = type;
    }

    public role(int id_role) {
        this.id_role = id_role;
    }

    public role(int id_role, String type) {
        this.id_role = id_role;
        this.type = type;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "role{" + "id_role=" + id_role + ", type=" + type + '}';
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
