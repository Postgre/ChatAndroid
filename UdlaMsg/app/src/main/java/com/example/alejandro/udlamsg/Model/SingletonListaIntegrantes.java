package com.example.alejandro.udlamsg.Model;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;

import com.example.alejandro.udlamsg.Interfaz.ListaAdapterCompañeros;
import com.example.alejandro.udlamsg.lista.Integrante;

/**
 * Created by oswaldo on 19/11/2016.
 */
public class SingletonListaIntegrantes {
    private static SingletonListaIntegrantes ourInstance = new SingletonListaIntegrantes();
    private Integrante integrante;
    private ListAdapter _adapter;
    private Context _context;
    private AppCompatActivity activityIntegrante;

    public static SingletonListaIntegrantes getInstance() {
        return ourInstance;
    }

    private SingletonListaIntegrantes() {
    }

    public ListAdapter get_adapter() {
        return _adapter;
    }

    public void set_adapter(ListAdapter _listAdapter) {
        this._adapter = _listAdapter;
    }

    public Context get_context() {
        return _context;
    }

    public void set_context(Context _context) {
        this._context = _context;
    }

    public Integrante getIntegrante() {
        return integrante;
    }

    public void setIntegrante(Integrante integrante) {
        this.integrante = integrante;
    }

    public AppCompatActivity getActivityIntegrante() {
        return activityIntegrante;
    }

    public void setActivityIntegrante(AppCompatActivity activityIntegrante) {
        this.activityIntegrante = activityIntegrante;
    }
}
