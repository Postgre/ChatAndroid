package com.example.alejandro.udlamsg.Model;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alejandro.udlamsg.Interfaz.ListaAdapterCompañeros;
import com.example.alejandro.udlamsg.R;
import com.example.alejandro.udlamsg.lista.Integrante;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by oswaldo on 18/11/2016.
 */
public class SingletonWebSocket {
    private static SingletonWebSocket ourInstance = new SingletonWebSocket();
    public Imagen _imagen = new Imagen();
    private WebSocketClient websocket;
    private AppCompatActivity actityActual;
    private ListAdapter adaptador;
    private String ReceptorActual;
    private String EmisorActual;
    private SingletonWebSocket() {
    }
    // <editor-fold defaultstate="collapsed" desc="METODOS WEBSOCKET">
    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://192.168.1.5:2012");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        websocket = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

            }

            @Override
            public void onMessage(String message) {

                Gson data = new GsonBuilder().create();
                String _nombreActividad =  actityActual.getClass().getSimpleName();

                Message m = data.fromJson(message, Message.class);

                if(m.getType().equals(GlobalType.MESSAGE)){      //grabo el mensaje en mi lista temporal
                    SingletonChatTemporal.getInstance().getListaChat().add(m);

                }
                if(_nombreActividad.equals(GlobalActivity.CHAT)){
                    eventosChat(data,message);
                }
                if(_nombreActividad.equals((GlobalActivity.GRUPO))){
                    NotificadorIntegrantes(data,message);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {

            }
        };
        websocket.connect();
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="EVENTOS EN LA ACTIVIDAD CHAT">
    private void eventosChat(Gson gson,String message){

        final Message data = gson.fromJson(message, Message.class);

        final TextView _estadoChat =(TextView) actityActual.findViewById(R.id.testado);
        final TextView _listChat = (TextView) actityActual.findViewById(R.id.messages);
        final ImageView _imagenChat = (ImageView) actityActual.findViewById(R.id.img_foto);
        actityActual.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (GlobalType.CONNECT.equals(data.getType()))
                    _estadoChat.setText(EstadoChat(data.getCodeEmisor(),data.getType()));
                if (GlobalType.DISCONECTED.equals(data.getType()))
                    _estadoChat.setText(EstadoChat(data.getCodeEmisor(),data.getType()));
                if(GlobalType.MESSAGE.equals(data.getType())){
                    if (data.getFile() !=null){
                        Bitmap img = _imagen.ConvertStringBItmap(data.getFile());
                        _imagenChat.setImageBitmap(img);
                    }
                    if(data.getSend()!=null){
                        _listChat.setText("");
                        List<Message> historico = SingletonChatTemporal.getInstance().
                                                        HistoricoMessages(data.getCodeEmisor(),data.getCodeReceptor());
                        for (Message men: historico) {
                            _listChat.setText(_listChat.getText() + "\n" + men.getCodeEmisor() + ":" +  men.getSend());
                        }
                    }

                }
            }
        });
    }

    private void AddMessageChat(Message data){
         String Cant =String.valueOf(SingletonChatTemporal.getInstance().getListaChat().size());
         data.setCodeEmisor(Cant);
         SingletonChatTemporal.getInstance().getListaChat().add(data);

    }
    private void NotificadorIntegrantes(Gson gson,String message){

        final Message data = gson.fromJson(message, Message.class);


        final ListView listViewcontactos = (ListView) actityActual.findViewById(R.id.lista_integrantes);

                if (GlobalType.CONNECT.equals(data.getType())){
                    Integrante _integrante =  CambiarEstadoIntegrante(data.getCodeEmisor(),GlobalType.DISPONIBLE);
                    RenderizeIntegrante(listViewcontactos,_integrante,actityActual);
                }
                if(GlobalType.DISCONECTED.equals(data.getType())){

                    Integrante _integrante =  CambiarEstadoIntegrante(data.getCodeEmisor(),GlobalType.DESCONECTADO);
                    RenderizeIntegrante(listViewcontactos,_integrante,actityActual);
                }
                if(GlobalType.NOTIFICATION.equals(data.getType())){
                    Integrante _integrante =  CambiarEstadoIntegrante(data.getCodeEmisor(),GlobalType.DISPONIBLE);
                    RenderizeIntegrante(listViewcontactos,_integrante,actityActual);
                }

    }
    public String EstadoChat(String Idemisor,String type){
        String mensaje="";
        String[] nombres = SingletonListaIntegrantes.getInstance().getIntegrante().getNombres();
        for (int i=0;i<nombres.length ; i++){
            if(nombres[i].equals(Idemisor)){
                String horaAndroid = getDateTime();
                SingletonListaIntegrantes.getInstance().getIntegrante().getHora()[i] = horaAndroid;
                String _type = type.equals(GlobalType.CONNECT)?GlobalType.DISPONIBLE:GlobalType.DESCONECTADO;
                SingletonListaIntegrantes.getInstance().getIntegrante().getEstado()[i] = _type;
                mensaje = _type  + ":" + horaAndroid;
            }
        }
        return mensaje;
    }
    public String EstadoChat(String Idemisor){
        String mensaje="";
        String[] nombres = SingletonListaIntegrantes.getInstance().getIntegrante().getNombres();
        for (int i=0;i<nombres.length ; i++){
            if(nombres[i].equals(Idemisor)){

                String _hora =  SingletonListaIntegrantes.getInstance().getIntegrante().getHora()[i];
                String _estado =  SingletonListaIntegrantes.getInstance().getIntegrante().getEstado()[i];
                mensaje = _estado  + ":" + _hora;
            }
        }
        return mensaje;
    }
    public Integrante CambiarEstadoIntegrante(String codIntegrante,String TypeConected){
        Integrante _integrante = SingletonListaIntegrantes.getInstance().getIntegrante();
        for (int i = 0; i < _integrante.getNombres().length; i++) {
            if (_integrante.getNombres()[i].equals(codIntegrante) ){
                SingletonListaIntegrantes.getInstance().getIntegrante().getEstado()[i] = TypeConected;
                SingletonListaIntegrantes.getInstance().getIntegrante().getHora()[i]= getDateTime();
                // falta agregar los ultimos chat....  Por aca se hacen jejeje...
            }
        }
        return _integrante;
    }

    public int IntegrantesOnline(){
        String[] _estados = SingletonListaIntegrantes.getInstance().getIntegrante().getEstado();
        int count=0;
        for (int i=0 ; i < _estados.length;i++){
            if (_estados[i].equals(GlobalType.DISPONIBLE) ){
                count++;
            }
        }
        return count;
    }
    //renderiza la lista de contactos..
    public void RenderizeIntegrante(ListView listViewcontactos, Integrante integrante,AppCompatActivity _actividad){
        final ListView _listViewcontactos = listViewcontactos;
        final Integrante _integrante = integrante;
        final AppCompatActivity _app = _actividad;
        final TextView _countUserOnline = (TextView) _app.findViewById(R.id.tuser);
         String _nombreActividad =  _actividad.getClass().getSimpleName();
        _app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _countUserOnline.setText("online:" + IntegrantesOnline());
                adaptador = new ListaAdapterCompañeros(_app.getApplicationContext(),_integrante);
                _listViewcontactos.setAdapter(adaptador);
            }
        });
    }

    private String getDateTime() {
        Calendar calendario = new GregorianCalendar();
        int hora, minutos;
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        return hora + ":" + minutos;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos Get y Set" de la actividad actual>

    public static SingletonWebSocket getInstance() {
        return ourInstance;
    }

    public WebSocketClient getWebsocket() {
        return websocket;
    }
    public void setWebsocket() {
        connectWebSocket();
    }

    public AppCompatActivity getActityActual() {
        return actityActual;
    }

    public void setActityActual(AppCompatActivity actityActual) {
        this.actityActual = actityActual;
    }

    public String getReceptorActual() {
        return ReceptorActual;
    }

    public void setReceptorActual(String receptorActual) {
        ReceptorActual = receptorActual;
    }
    // </editor-fold>
}






