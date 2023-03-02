/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfaihi;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import sfaihi.FXMLDocumentController;

/**
 *
 * @author Ahmed
 */
public class avis {

    
private final StringProperty idavis;
private final StringProperty commentaire;
private final StringProperty idportfolio;

public avis()
{
idavis = new SimpleStringProperty(this, "idavis" ) ;
commentaire= new SimpleStringProperty(this,"commentaire");
idportfolio= new SimpleStringProperty(this,"idportfolio");
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
return commentaire.get ();
}
public void setcommentaire (String newcommentaire)
{
    commentaire.set(newcommentaire);

}

public StringProperty idportfolioProperty()
{ 
    return idportfolio;
}
public String getidportfolio()
{
return idportfolio.get ();
}
public void setidportfolio (String newidportfolio)
{
    idportfolio.set(newidportfolio);

}

    
}
