package com.example.alejandro.udlamsg.Interfaz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.alejandro.udlamsg.Model.GlobalType;
import com.example.alejandro.udlamsg.Model.Message;
import com.example.alejandro.udlamsg.Model.SingletonListaIntegrantes;
import com.example.alejandro.udlamsg.Model.SingletonWebSocket;
import com.example.alejandro.udlamsg.R;
import com.example.alejandro.udlamsg.lista.Integrante;
import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class ListaIntegrantes extends AppCompatActivity {
    Message _mensaje = new Message();
    ListView listViewcontactos;
    Integrante _integrantes;
    ListAdapter adaptador;
    TabHost tab;
    String nommateria="";
    TextView nombregrupo;
    String codigemisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_integrantes);
        Intent i = getIntent();
        Bundle Bun = i.getExtras();
        codigemisor=(String)Bun.get("codigoemisor");

        int numero = (int) Bun.get("codigo");
        nommateria=(String)Bun.get("Materia");
        nombregrupo = (TextView)findViewById(R.id.textView_titulointegrantes);
        nombregrupo.setText(nommateria);
        listViewcontactos = (ListView) findViewById(R.id.lista_integrantes);

        SoapContactos c = new SoapContactos();
        SingletonWebSocket.getInstance().setActityActual(this);
        SingletonListaIntegrantes.getInstance().setActivityIntegrante(this);
        c.execute(numero);
    }

    private class SoapContactos extends AsyncTask<Integer, ArrayList<Estudiantes>, ArrayList<Estudiantes>> {
        ArrayList<Estudiantes> webrespons;

        @Override
        protected ArrayList<Estudiantes> doInBackground(Integer... params) {
            webrespons = new ArrayList<Estudiantes>();
            try {
                final String NAMESPACE = "http://duban.org/";
                final String URL = "http://52.36.238.241/DUban/Web_Service.asmx";
                final String SOAP_ACTION = "http://duban.org/matriculados";
                final String METHOD_NAME = "matriculados";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                PropertyInfo pi = new PropertyInfo();
                pi.setName("codigo");
                //int j= Integer.parseInt(params[0]);
                pi.setValue(params[0].intValue());
                pi.setType(Integer.class);
                request.addProperty(pi);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                Deserilization oj = new Deserilization();
                androidHttpTransport.call(SOAP_ACTION, envelope);

                Persona person = new Persona((SoapObject) envelope.getResponse());
                ArrayList<Estudiantes> arrayList = new ArrayList<Estudiantes>();
                arrayList = oj.SoapDeserializeArray(Estudiantes.class, (SoapObject) (envelope.getResponse()));
                SoapObject response = (SoapObject) envelope.getResponse();

                Gson data = new Gson();
                _mensaje.setType(GlobalType.NOTIFICATION);
                _mensaje.setCodeEmisor(codigemisor);
                String _mens = data.toJson(_mensaje);
                SingletonWebSocket.getInstance().setReceptorActual(codigemisor);
                SingletonWebSocket.getInstance().getWebsocket().send(_mens);

                webrespons = arrayList;


            } catch (Exception e) {
                String exe = e.toString();
            }
            return webrespons;
        }


        @Override
        protected void onPostExecute(final ArrayList<Estudiantes> resultado) {

            int cantidad = resultado.size();
            _integrantes = new Integrante();
            _integrantes.setNombres(new String[cantidad]);
            _integrantes.setCodigo(new int[cantidad]);
            _integrantes.setImagenes(new String[cantidad]);
            _integrantes.setEstado(new String[cantidad]);
            _integrantes.setLmensaje(new String[cantidad]);
            _integrantes.setHora(new String[cantidad]);

            for (int i = 0; i < cantidad; i++) {

                _integrantes.getNombres()[i] = resultado.get(i).NombreDeEstudiantes;
                _integrantes.getCodigo()[i] = resultado.get(i).Id_estudiantes;
                _integrantes.getImagenes()[i] = resultado.get(i).Foto;
                _integrantes.getEstado()[i] = "DESCONECTADO";
                _integrantes.getLmensaje()[i] ="";
                _integrantes.getHora()[i]="";
            }
            SingletonListaIntegrantes.getInstance().setIntegrante(_integrantes);
            SingletonListaIntegrantes.getInstance().set_context(getApplicationContext());
            adaptador = new ListaAdapterCompañeros(getApplicationContext(),_integrantes);
            SingletonListaIntegrantes.getInstance().set_adapter(adaptador);

            listViewcontactos.setAdapter(adaptador);

            listViewcontactos.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int itemPosition = position;
                    String _codigoreceptor =String.valueOf(_integrantes.getNombres()[itemPosition]);
                    SingletonWebSocket.getInstance().setReceptorActual(_codigoreceptor);
                    String itemValue = (String) listViewcontactos.getItemAtPosition(position);
                    Intent inten = new Intent(ListaIntegrantes.this,Chat2.class);
                    inten.putExtra("nombre",_integrantes.getNombres()[itemPosition]);
                    inten.putExtra("foto",_integrantes.getImagenes()[itemPosition]);
                    inten.putExtra("codigorecep",_codigoreceptor);
                    inten.putExtra("codemisor",codigemisor);
                    startActivity(inten);
                }
            });
        }
    }
}



