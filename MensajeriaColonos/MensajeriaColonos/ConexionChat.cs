using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MensajeriaColonos
{
    public class ConexionChat
    {
        private static MySqlConnection conexion = new MySqlConnection("Database=udlamsg; Data Source= 52.53.151.16; User Id=admin; password=admin;");

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

        public static int EjecutarOperacion(string sentencia)
        {
            if (Conectar())
            {

                MySqlCommand comando = new MySqlCommand();
                comando.CommandText = sentencia;
                comando.Connection = conexion;
                int x = comando.ExecuteNonQuery();
                Desconectar();
                return x;
            }
            else
            {
                throw new Exception("No ha sido posible conectarse a la Base de Datos");
            }
            return -1;
        }

        public static int EjecutarOperacionParametros(String Query, List<MySqlParameter> param)
        {
            if (Conectar())
            {
                using (conexion)
                {
                    using (MySqlCommand cmd = new MySqlCommand())
                    {

                        cmd.Connection = conexion;
                        cmd.CommandText = Query;
                        foreach (MySqlParameter x in param)
                        {
                            cmd.Parameters.Add(x);

                        }
                        return cmd.ExecuteNonQuery();
                    }
                }
                Desconectar();
            }
            else
            {
                return -1;
            }

        }

    }
}
