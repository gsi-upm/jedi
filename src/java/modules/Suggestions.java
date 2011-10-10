/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modules;


/**
 *
 * @author nachomv
 */
public class Suggestions {

    private int id;
    private String name;

    public Suggestions(){
        id = 0;
        name = "";
    }
    public Integer getQuery(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setId( Integer id ){
        this.id = id;
    }

    public void setName( String name ){
        this.name = name;
    }

    


}
