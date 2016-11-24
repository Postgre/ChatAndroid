package com.example.alejandro.udlamsg.Interfaz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alejandro.udlamsg.Model.Imagen;
import com.example.alejandro.udlamsg.Model.SingletonListaIntegrantes;
import com.example.alejandro.udlamsg.R;
import com.example.alejandro.udlamsg.lista.Integrante;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListaAdapterCompañeros extends ArrayAdapter<String> {
    Integrante _integrantes;
    Context context;
    Imagen _img = new Imagen();
    public ListaAdapterCompañeros(Context context1,Integrante integrantes){
        super(context1, R.layout.activity_amigo, integrantes.getNombres());
        this._integrantes = integrantes;
        this.context = context1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.activity_amigo,parent, false);
        TextView nombre=(TextView)rowView.findViewById(R.id.Nomb);
        TextView  estado = (TextView)rowView.findViewById(R.id.Esta);
        TextView hora = (TextView) rowView.findViewById(R.id.horamensaje);
        ImageView imageView=(ImageView)rowView.findViewById(R.id.imageView_fotoperfilchat);
        Bitmap _Foto = _img.ConvertStringBItmap(SingletonListaIntegrantes.getInstance().getIntegrante().getImagenes()[position]);
        if (_Foto!=null)
            imageView.setImageBitmap(_Foto);

        nombre.setText(_integrantes.getNombres()[position]);
        estado.setText(_integrantes.getEstado()[position]);
        hora.setText(_integrantes.getHora()[position]);

        return rowView;
    }
}