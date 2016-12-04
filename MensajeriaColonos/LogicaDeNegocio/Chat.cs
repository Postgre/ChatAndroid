using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using MensajeriaColonos;
using MySql.Data.MySqlClient;

namespace LogicaDeNegocio
{
    public class Chat
    {

        public int CodigoChat;
        public String NombreChat;
        public Usuario Recibe;
        public List<Mensaje> Mensajes;

        public Chat()
        {
            Mensajes = new List<Mensaje>();
            Recibe = new LogicaDeNegocio.Usuario();
        }

       
        public static bool AbrirCrearChat(int CodigoUsuario1, int CodigoUsuario2)
        {
            string sql = "call create_chat(@usuario1,@usuario2)";
            List<MySqlParameter> parametro = new List<MySqlParameter>();
            parametro.Add(new MySqlParameter("@usuario1", CodigoUsuario1));
            parametro.Add(new MySqlParameter("@usuario2", CodigoUsuario2));
            if (ConexionChat.EjecutarOperacionParametros(sql, parametro) > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public static bool CerrarChat(int codigo_usuario, int codigo_chat)
        {
            string consulta = "update user_chat set user_chat.state_chat=0 where user_chat.code_user=" + codigo_usuario + " and user_chat.code_chat=" + codigo_chat + "";
            if (ConexionChat.EjecutarOperacion(consulta) > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public static List<Chat> ConsUltMsg(int CodigoUsuario)
        {
            List<Chat> MensajeDelChat = new List<Chat>();
            string sql = "select  MAX(m.message) as 'mensaje',MAX(u.name_user) as 'envia',MAX(m.attached) as 'adjunto',MAX(m.date_message) as 'fecha',rm.code_chat" + " from record_message as rm" + " natural join message as m" + " inner join user as u on(u.code_user = m.user_sends)" + " where rm.code_user = " + CodigoUsuario + " and rm.state_message = 1 group by rm.code_chat  ";
            DataTable Mensaje = ConexionChat.EjecutarConsulta(sql);
            foreach (DataRow i in Mensaje.Rows)
            {
                Mensaje temp = new Mensaje();
                if (i["mensaje"].ToString() == "")
                { }
                else {
                    temp.msg = i["mensaje"].ToString();
                }if (i["adjunto"].ToString() == "")
                {
                    temp.adjunto = null;
                }
                else
                {
                    temp.adjunto = (byte[])i["adjunto"];
                }
                temp.usuario.nombre = i["envia"].ToString();
                Chat chatt = new Chat();
                chatt.CodigoChat = (int)i["code_chat"];
                temp.fecha = i["fecha"].ToString();
                chatt.Mensajes.Add(temp);
                MensajeDelChat.Add(chatt);

            }

            return MensajeDelChat;

        }

    }
}
