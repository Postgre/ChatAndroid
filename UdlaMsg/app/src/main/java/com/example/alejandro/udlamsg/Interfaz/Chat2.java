package com.example.alejandro.udlamsg.Interfaz;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alejandro.udlamsg.Model.GlobalType;
import com.example.alejandro.udlamsg.Model.Imagen;
import com.example.alejandro.udlamsg.Model.Message;
import com.example.alejandro.udlamsg.Model.SingletonListaIntegrantes;
import com.example.alejandro.udlamsg.Model.SingletonWebSocket;
import com.example.alejandro.udlamsg.R;
import com.example.alejandro.udlamsg.lista.Integrante;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class Chat2 extends AppCompatActivity {
    private WebSocketClient mWebSocketClient;
    private GlobalType _GlobalType =new GlobalType();
    private Imagen  _img = new Imagen();
    private Message send = new Message();
    private Boolean StatusConected = false;
    public static TextView _estadoChat;
    String _mensaje;
    TextView nombreuser;
    TextView _listamessage;

    String nomb = "";
    String _foto;
    EditText editText;
    ImageButton botonenviar;
    String _codEmisor;
    String _codReceptor;
    String mPath;
    ImageView imgFoto;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_chat2);
        SingletonWebSocket.getInstance().setActityActual(this);
        imgFoto = (ImageView) findViewById(R.id.img_foto);

        iniciarDatos();

        botonenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _mensaje =  editText.getText().toString();
                Thread nt2 = new Thread() {



                    @Override
                    public void run() {

                        Gson data = new Gson();

                        setSend(GlobalType.MESSAGE,_mensaje,null);
                        try {
                            String Datamessage = data.toJson(getSend());
                            SingletonWebSocket.getInstance().getWebsocket().send(Datamessage);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _listamessage.setText(_listamessage.getText() + "\n" + getSend().getCodeEmisor() + ":" + getSend().getSend());
                                editText.setText("");
                            }
                        });
                    }
                };
                nt2.start();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {

            SingletonWebSocket.getInstance().setActityActual(SingletonListaIntegrantes.getInstance().getActivityIntegrante());
            AppCompatActivity act = SingletonWebSocket.getInstance().getActityActual();
            Integrante  _integrante =  SingletonListaIntegrantes.getInstance().getIntegrante();
            ListView listaContactos = (ListView) act.findViewById(R.id.lista_integrantes);
            SingletonWebSocket.getInstance().RenderizeIntegrante(listaContactos,_integrante,act);

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("onCreateOptionsMenu", "create menu");
        MenuInflater inflater = getMenuInflater();
        android.support.v7.widget.Toolbar barra = (android.support.v7.widget.Toolbar) findViewById(R.id.barrachat);
         inflater.inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.item_galeria:
                openGallery();
                return true;
            case R.id.item_camara:
                 openCam();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Obtener la foto de la geleria de imagenes" >
    private void openGallery()
    {
        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryintent, 1);
    }
    private void openCam()
    {
        Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri output = Uri.fromFile(new File(name));
        startActivityForResult(intent, 2);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        ImageView img_foto = (ImageView) findViewById(R.id.img_foto);
        String StringImagen = null;

        Gson _data = new Gson();



        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            String imgDecodableString;
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             imgDecodableString = cursor.getString(columnIndex);
            Bitmap bitImagen = _img.Urlmagen(imgDecodableString);
            StringImagen = _img.BitMapToString(bitImagen);
            img_foto.setImageBitmap(bitImagen);
            cursor.close();
        }
        if(requestCode == 2){
            img_foto.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
            Bitmap bitmap = BitmapFactory.decodeFile(mPath);
            if (data != null) {
                if (data.hasExtra("data")) {
                    StringImagen = _img.BitMapToString(bitmap);
                    img_foto.setImageBitmap(bitmap);

                }
            } else {
                Bitmap img =  BitmapFactory.decodeFile(name);
                StringImagen = _img.encodeImage(name);
                img_foto.setImageBitmap(img);
            }
        }
        setSend(GlobalType.MESSAGE,null,StringImagen);
        String Datamessage = _data.toJson(getSend());
        if (Datamessage!=null){
            setSend(GlobalType.MESSAGE,null,Datamessage);
            SingletonWebSocket.getInstance().getWebsocket().send(Datamessage);
        }



    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos Get y Set del Chat" >
    private void iniciarDatos(){
        Intent i = getIntent();
        Bundle Bun = i.getExtras();
        nomb = (String) Bun.get("nombre");
        _codEmisor = (String) Bun.get("codemisor");
        _codReceptor = (String) Bun.get("codigorecep");
        _foto = (String) Bun.get("foto");
        SingletonWebSocket.getInstance().setReceptorActual(_codReceptor);
        nombreuser = (TextView) findViewById(R.id.textView_nombrechat);
        nombreuser.setText(nomb);
        botonenviar = (ImageButton) findViewById(R.id.button_enviarmensaje);
        editText = (EditText) findViewById(R.id.editText_Escribirmensaje);
        _listamessage = (TextView) findViewById(R.id.messages);
        _estadoChat = (TextView) findViewById(R.id.testado);
        _estadoChat.setText(SingletonWebSocket.getInstance().EstadoChat(_codReceptor));

        ImageView imageView=(ImageView) findViewById(R.id.ImageView_fotochatperfil);
        Bitmap _Foto = _img.ConvertStringBItmap(_foto);
        if(_Foto!=null)
            imageView.setImageBitmap(_Foto);

    }

    public Message getSend() {
        return send;
    }

    public void setSend(String type,String message,String Foto) {
        send.setCodeEmisor(_codEmisor);
        send.setType(type);
        send.setSend(message);
        send.setFile(Foto);
        send.setNick(_codEmisor);
        send.setCodeReceptor(_codReceptor);

    }
    // </editor-fold>
}
