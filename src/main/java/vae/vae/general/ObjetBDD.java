package vae.vae.general;


import vae.vae.annotation.FieldDisable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ObjetBDD {

    public Field[] countEnableField(Field[] fields){
        ArrayList<Field> result = new ArrayList<Field>();
        for( Field f: fields ){
            f.setAccessible(true);
            if( !f.isAnnotationPresent(FieldDisable.class) )
                result.add(f);
        }
        Field[] response =  new Field[result.size()];
        result.toArray(response);
        return response;
    }

    public void create(Connection conn) throws Exception{
        Class<?> clazz = this.getClass();
        String sql = "insert into "+clazz.getSimpleName(), values = "";

        Field[] attribut = clazz.getDeclaredFields();
        attribut = countEnableField(attribut);
        for(int i = 1; i < attribut.length; i++ ){
            attribut[i].setAccessible(true);

            if( i == attribut.length - 1 ){
                values += modifiers(attribut[i].get(this).toString(), attribut[i].getType().getSimpleName());
            }
            else{
                values += modifiers(attribut[i].get(this).toString(), attribut[i].getType().getSimpleName())+",";
            }
        }
        sql += " values " + "(default, "+values+")";

        System.out.println(sql);
        conn.createStatement().executeUpdate(sql);
    }

    public void update(Connection conn) throws Exception{
        Class<?> clazz = this.getClass();
        String sql = "update "+clazz.getSimpleName()+" set ", values = "";

        Field[] attribut = this.getClass().getDeclaredFields();
        attribut = countEnableField(attribut);
        for(int i = 1; i < attribut.length; i++ ){
            attribut[i].setAccessible(true);
            if( i == attribut.length - 1 ){
                values = values + attribut[i].getName()+ " = " + modifiers(attribut[i].get(this).toString(),attribut[i].getType().getSimpleName());
            }
            else{
                values = values + attribut[i].getName()+ " = " + modifiers(attribut[i].get(this).toString(),attribut[i].getType().getSimpleName())+",";
            }
        }
        attribut[0].setAccessible(true);
        sql = sql + values + " where " + attribut[0].getName() +" = "+attribut[0].get(this);
        System.out.println(sql);
        conn.createStatement().executeUpdate(sql);
    }

    public void delete(Connection conn) throws Exception{
        Class<?> clazz = this.getClass();
        String sql = "delete "+clazz.getSimpleName();

        Field[] attribut = this.getClass().getDeclaredFields();
        attribut[0].setAccessible(true);
        sql = " where " + attribut[0].getName() +" = "+attribut[0].get(this);
        System.out.println(sql);
        conn.createStatement().executeUpdate(sql);
    }

    public ObjetBDD find(Connection conn) throws Exception{
        ObjetBDD result = this.getClass().newInstance();
        Class<?> clazz = this.getClass();
        Field[] attribut = clazz.getDeclaredFields();
        attribut[0].setAccessible(true);

        String query = "select * from "+clazz.getSimpleName()+" where "+attribut[0].getName()+" = "+attribut[0].get(this).toString();
        System.out.println(query);
        try{
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery(query);

            while( res.next() ){
                result = cast(res);
                return result;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ObjetBDD[] findAll(Connection conn) throws Exception{
        Class<?> clazz = this.getClass();

        String query = "select * from "+clazz.getSimpleName();
        System.out.println(query);
        ArrayList<ObjetBDD> result = new ArrayList<ObjetBDD>();

        try{
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery(query);

            while( res.next() ){
                ObjetBDD row = cast(res);
                result.add(row);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        ObjetBDD[] response =  new ObjetBDD[result.size()];
        result.toArray(response);
        return response;
    }

    public ObjetBDD cast(ResultSet res) throws Exception{
        ObjetBDD result = this.getClass().newInstance();
        Field[] fields = result.getClass().getDeclaredFields();

        for( Field field: fields ){
            field.setAccessible(true);
            if( !field.isAnnotationPresent(FieldDisable.class) ){
                field.set(result, res.getObject(field.getName()));
            }
        }
        return result;
    }

    public String modifiers(String s,String type){
        if( type.equals("String") || type.equals("Date") || type.equals("Timestamp") || type.equals("DateTime")  ){
            return "'"+s+"'";
        }
        return s;
    }
}
