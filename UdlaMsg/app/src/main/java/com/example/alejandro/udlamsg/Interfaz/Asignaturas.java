package com.example.alejandro.udlamsg.Interfaz;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.alejandro.udlamsg.Model.GlobalType;
import com.example.alejandro.udlamsg.Model.Message;
import com.example.alejandro.udlamsg.Model.SingletonListaIntegrantes;
import com.example.alejandro.udlamsg.Model.SingletonWebSocket;
import com.example.alejandro.udlamsg.R;
import com.example.alejandro.udlamsg.lista.Integrante;
import com.example.alejandro.udlamsg.lista.ListaCursos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Asignaturas extends AppCompatActivity {
    ListView listView_lista_de_amigos;
    Message _mensaje = new Message();
    ListAdapter adaptador;
    ImageView fotografia;
    TextView tes;
    String codEmisor;
    int numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaturas);
         SingletonWebSocket.getInstance().setWebsocket();

        tes = (TextView) findViewById(R.id.textView_nombreuser);
        Intent i =getIntent();
        Bundle Bun = i.getExtras();
        numero = (int) Bun.get("logueoResult");

        listView_lista_de_amigos = (ListView) findViewById(R.id.lista_asignaturas);

        new SoapCURSOS().execute(numero);
        new SoapPersona().execute(numero);

    }
    private class SoapPersona extends AsyncTask<Integer, String, String> {
        @Override
        protected String doInBackground(Integer... params) {
            String webResponse = "";
            Persona Webserponse;
            try {
                final String NAMESPACE = "http://duban.org/";
                final String URL = "http://52.36.238.241/DUban/Web_Service.asmx";
                final String SOAP_ACTION = "http://duban.org/consulta_usuario";
                final String METHOD_NAME = "consulta_usuario";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                PropertyInfo pi = new PropertyInfo();
                pi.setName("id");
                pi.setValue(params[0]);
                pi.setType(Integer.class);
                request.addProperty(pi);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);
                Object r =  envelope.getResponse();
                Persona Per = new Persona((SoapObject) envelope.getResponse());
                codEmisor = String.valueOf(Per.nombre);
                byte[] imagen = null;
                String name =Per.nombre;

                try {
                    imagen = Base64.decode(Per.foto,Base64.DEFAULT);
                    Bitmap m = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);

                }catch (Exception e){

                }
                SoapObject response = (SoapObject) envelope.getResponse();
                //webResponse = response.getPropertyAsString("nombre");
                Gson data = new Gson();
                _mensaje.setType(GlobalType.CONNECT);
                _mensaje.setCodeEmisor(codEmisor);
                String _mens = data.toJson(_mensaje);
                SingletonWebSocket.getInstance().setReceptorActual(codEmisor);
                SingletonWebSocket.getInstance().getWebsocket().send(_mens);
                webResponse = Per.nombre;


            } catch (Exception e) {
                webResponse = e.toString();
                return webResponse;
            }
            return "Bienvenido " + webResponse;
        }

        @Override
        protected void onPostExecute(String result) {

            tes.setText(result);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            SingletonWebSocket.getInstance().getWebsocket().close();
        }
        return super.onKeyDown(keyCode, event);
    }

    private class SoapCURSOS extends AsyncTask<Integer, ArrayList<Cursos>, ArrayList<Cursos>> {
        ArrayList<Cursos> webrespons;

        @Override
        protected ArrayList<Cursos> doInBackground(Integer... params) {
            webrespons = new ArrayList<Cursos>();
            try {
                final String NAMESPACE = "http://duban.org/";
                final String URL = "http://52.36.238.241/DUban/Web_Service.asmx";
                final String SOAP_ACTION = "http://duban.org/materias_matriculadas";
                final String METHOD_NAME = "materias_matriculadas";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                PropertyInfo pi = new PropertyInfo();
                pi.setName("id");
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

                Cursos curso = new Cursos((SoapObject) envelope.getResponse());
                ArrayList<Cursos> arrayList = new ArrayList<Cursos>();
                arrayList = oj.SoapDeserializeArray(Cursos.class, (SoapObject) (envelope.getResponse()));
                SoapObject response = (SoapObject) envelope.getResponse();
                int r = response.getPropertyCount();
                webrespons=arrayList;

            } catch (Exception e) {
                String exe = e.toString();
            }
            return webrespons;
        }

        @Override
        protected void onPostExecute(final ArrayList<Cursos> resultado) {

            int cantidad = resultado.size();
            final String[]  amigos = new String[cantidad];
            int[]  n = new int[cantidad];

            for (int i = 0; i < cantidad; i++) {
                amigos[i] = resultado.get(i).Materia;
                n[i] = resultado.get(i).NumeroCurso;
            }

            adaptador = new ListaCursos(getApplicationContext(), amigos);
            listView_lista_de_amigos.setAdapter(adaptador);
            listView_lista_de_amigos.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemPosition = position;
                    String itemValue = (String) listView_lista_de_amigos .getItemAtPosition(position);
                    Intent inten = new Intent(Asignaturas.this,ListaIntegrantes.class);
                    inten.putExtra("Materia",amigos[itemPosition]);
                    inten.putExtra("codigo", resultado.get(itemPosition).Codigo );
                    inten.putExtra("codigoemisor",codEmisor);
                    startActivity(inten);
                }
            });
        }
    }
}
