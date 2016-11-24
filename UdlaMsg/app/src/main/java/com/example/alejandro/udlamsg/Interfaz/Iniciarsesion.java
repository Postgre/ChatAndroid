package com.example.alejandro.udlamsg.Interfaz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.alejandro.udlamsg.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Iniciarsesion extends AppCompatActivity {
        EditText usua;
        EditText contr;
        ImageButton ingresar;
        TextView textomsge;
        public static String rslt="";    /** Called when the activity is first created. */
        String res2;
        String mensaje="";
        String usu="";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            usua=(EditText)findViewById(R.id.editText_user);
            contr=(EditText)findViewById(R.id.editText_contra);
            ingresar=(ImageButton)findViewById(R.id.Button_login);
            textomsge=(TextView)findViewById(R.id.textViewmens);
            //Eventos
            ingresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    usu=usua.getText().toString();
                    String pwd=contr.getText().toString();
                    SoapABUsuario h1=new SoapABUsuario();
                    h1.execute(usu,pwd,"");
                }
            });

        }
    private class SoapABUsuario extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }

        @Override
        protected  String doInBackground(String... params) {
            String webResponse = "";
            try{
                final String NAMESPACE = "http://duban.org/";
                final String URL = "http://52.36.238.241/DUban/Web_Service.asmx";
                final String SOAP_ACTION = "http://duban.org/logueo";
                final String METHOD_NAME = "logueo";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                PropertyInfo pi=new PropertyInfo();
                pi.setName("usuario");
                //pi.setValue(params[0]);
                pi.setValue("camila");
                pi.setType(Integer.class);
                request.addProperty(pi);
                pi=new PropertyInfo();
                pi.setName("Contraseña");
                //pi.setValue(params[1]);
                pi.setValue("munar");
                pi.setType(Integer.class);
                request.addProperty(pi);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                webResponse = response.toString();
                int resul=Integer.parseInt(webResponse);
                if (resul >0) {
                    Intent inten = new Intent(Iniciarsesion.this, Asignaturas.class);
                    Bundle b = new Bundle();
                    b.putInt("logueoResult",resul);
                    inten.putExtras(b);
                    startActivity(inten);

                } else {
                    webResponse = "error en usuario o contraseña";
                }
            }
            catch(Exception e){
                webResponse=e.toString();
                return webResponse;
                //Toast.makeText(getApplicationContext(),"Cannot access the web service"+e.toString(), Toast.LENGTH_LONG).show();
            }
            return webResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            textomsge.setText(result);
            //Toast.makeText(getApplicationContext(),"Bienvendio.."+usu, Toast.LENGTH_LONG).show();
        }

        public void accessWebService(String usu,String pwd) {
            SoapABUsuario task = new SoapABUsuario();
            //passes values for the urls string array
            task.execute(usu,pwd," ");
        }
    }
}