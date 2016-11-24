package com.example.alejandro.udlamsg.Interfaz;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;


public class Estudiantes {


    public int Id_estudiantes;
    public String NombreDeEstudiantes;
    public String Foto;
    public String Hora;
    public String Mensaje;
    public String Estado;

    Deserilization obj=new Deserilization();

    public ArrayList<Estudiantes> children;


    public Estudiantes(SoapObject object){
        obj.SoapDeserilize(this,object);
    }





}
