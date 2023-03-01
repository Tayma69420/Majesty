/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfaihi;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Ahmed
 */
public class portfolio {
    private final StringProperty id;
private final StringProperty desc;
private final StringProperty cv;
private final StringProperty img;


public portfolio()
{
id = new SimpleStringProperty(this, "idportfolio" ) ;
desc= new SimpleStringProperty(this,"description");
cv= new SimpleStringProperty(this, "cv");
img=new SimpleStringProperty(this,"image");

}

public StringProperty idProperty()
{ 
    return id;
}
public String getId()
{
return id.get ();
}
public void setId (String newId)
{
    id.set(newId);
}


public StringProperty descProperty()
{ 
    return desc;
}
public String getDesc()
{
return desc.get ();
}
public void setDesc (String newDesc)
{
    desc.set(newDesc);
}



public StringProperty cvProperty()
{ 
    return cv;
}
public String getCv()
{
return cv.get ();
}
public void setCv (String newCv)
{
    cv.set(newCv);
}


public StringProperty imgProperty()
{ 
    return img;
}
public String getImg()
{
return img.get ();
}
public void setImg (String newImg)
{
    img.set(newImg);
}




}
