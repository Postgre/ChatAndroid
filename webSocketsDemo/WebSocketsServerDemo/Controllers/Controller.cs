using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WebSocketsServerDemo.Controllers
{
    class Controller
    {
        connection _conection = new connection();

        public bool enviarMensaje(string emisor,string receptor,string idgrupo,string message, string status){
            String sql = "";

            return _conection.sendSetDataMariaDB(sql); ;
        }
        public DataTable NotificadorGrupo(String idemisor,String idgrupo) {
            String sql = "SELECT * FROM chat";
            return _conection.getDataMariaDB(sql).Tables[0];

        }
        public DataTable NotificadorChat(String idemisor,String idgrupo,String Idreceptor) {
            String sql = "";
            return _conection.getDataMariaDB(sql).Tables[0];
        }

    }
}
