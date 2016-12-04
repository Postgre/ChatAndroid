using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient;
using MensajeriaColonos;


namespace LogicaDeNegocio
{


    public class Mensaje


    {
        

        public int codigo_mensaje;

        public byte[] adjunto;
        public string nombre;
        public String fecha;
        public Usuario usuario;
        public string msg;


        public Mensaje()
        {

            usuario = new Usuario();
        }
        public static int EnviarMensaje(String mensaje, byte[] adjunto, double CodUsuario, int CodChat) {

            try { 

           MySqlCommand Consulta = new MySqlCommand("insert into message values (NULL, " + mensaje + ", @adjunto,now()," + CodUsuario + "," + CodChat + ");");
            Consulta.Parameters.AddWithValue("@adjunto", adjunto);
            ConexionChat.Agre(Consulta);

            return 1;

        } catch(Exception)
            {
            return 0;
            }
        }
    }
}
