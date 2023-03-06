/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package panier.interfaces;

import java.util.List;

public interface interfaceCRUD<T> {
    public void ajouterEntite(T t);
    public List<T> listeDesEntites();
}


