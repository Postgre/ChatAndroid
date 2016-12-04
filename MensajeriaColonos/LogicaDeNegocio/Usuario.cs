using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MensajeriaColonos;
using MySql.Data.MySqlClient;
using System.Data;


namespace LogicaDeNegocio
{
    public class Usuario
    {
        public string nombre;
        public string apellido;
        public string rol;
        public int codigo;
        public byte[] foto;
      
        public static int Logeo(String usuario, String contraseña) {

            String consulta = "select code_user from udlamsg.user where user.name_user ='" + usuario + "' and user.password = aes_encrypt('" + contraseña + "','root');";
            string respuesta;
            int id_usuario;

            DataTable x = new DataTable();
            x = ConexionChat.EjecutarConsulta(consulta);
            respuesta = x.Rows[0][0].ToString();
            id_usuario = Convert.ToInt32((respuesta));

            return id_usuario;
           

        }

        public static String consultarusuario(int id)
        {
            string comando = "select person.name from  person where id = " + id + "; ";

            DataTable x = new DataTable();
            x = ConexionColonos.EjecutarConsulta(comando);
            String nom = x.Rows[0]["name"].ToString();

            return nom;
        }

        public static bool Actualizar_Perfil(byte[] imagen, int codigo_usuario)
        {
            string sql = "update profile set profile.photo_profile=@imgArr where profile.code_user=@id";
            List<MySqlParameter> param = new List<MySqlParameter>();
            param.Add(new MySqlParameter("@id", codigo_usuario));
            param.Add(new MySqlParameter("@imgArr", imagen));
          
            if (ConexionChat.EjecutarOperacionParametros(sql, param) > 0)
            {
                return true;
            }
            else
            {
                return false;
            }


        }


    }
}
