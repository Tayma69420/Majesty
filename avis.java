/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfaihi;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sfaihi.FXMLDocumentController;

/**
 *
 * @author Ahmed
 */
public class avis {
private final StringProperty idavis;
private final StringProperty commentaire;

public avis()
{
idavis = new SimpleStringProperty(this, "idavis" ) ;
commentaire= new SimpleStringProperty(this,"commentaire");
}




 public StringProperty idavisProperty()
{ 
    return idavis;
}
public String getIdavis()
{
return idavis.get ();
}
public void setIdavis (String newIdavis)
{
    idavis.set(newIdavis);
}

public StringProperty commentaireProperty()
{ 
    return commentaire;
}
public String getcommentaire()
{
return idavis.get ();
}
public void setcommentaire (String newcommentaire)
{
    idavis.set(newcommentaire);

}

    
}
