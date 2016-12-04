using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient;
using System.Data;

namespace MensajeriaColonos
{
    public class ConexionColonos
    {

        private static MySqlConnection conexion = new MySqlConnection("Database=colonosswiii; Data Source=52.53.151.16 ; User Id=admin; password=admin;");

        public static bool Conectar()
        {
            try
            {
                conexion.Open();
                return true;
            }
            catch (Exception)
            {

                return false;
            }

        }
        private static void Desconectar()
        {
            conexion.Close();
        }
        public static void Agregar(string sentencia)
        {
            if (Conectar())
            {

                MySqlCommand comando = new MySqlCommand();
                comando.CommandText = sentencia;
                comando.Connection = conexion;
                comando.ExecuteNonQuery();
                Desconectar();


            }
            else
            {
                throw new Exception("No ha sido posible conectarse a la Base de Datos");
            }
        }

        public static void Agre(MySqlCommand comando)
        {
            if (Conectar())
            {

                comando.Connection = conexion;
                comando.ExecuteNonQuery();
                Desconectar();


            }
            else
            {
                throw new Exception("No ha sido posible conectarse a la Base de Datos");
            }
        }
        public static DataTable EjecutarConsulta(string sentencia)
        {
            MySqlDataAdapter adaptador = new MySqlDataAdapter();
            adaptador.SelectCommand = new MySqlCommand(sentencia, conexion);


            DataTable resultado = new DataTable();
            adaptador.Fill(resultado);

            return resultado;
        }

    }
}
