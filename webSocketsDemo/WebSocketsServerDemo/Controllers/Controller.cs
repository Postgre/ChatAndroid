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

        public bool enviarMensaje(string emisor,string receptor,string message, string status){
            String sql = @"INSERT
                            INTO
                                MESSAGE
                                (
                                    code_message,
                                    MESSAGE,
                                    date_message,
                                    user_sends,
                                    state,
                                    code_chat
                                )
                            SELECT
                                (
                                    SELECT
                                        COALESCE(MAX(m.code_message), 0) + 1
                                    FROM
                                        MESSAGE m
                                )
                                ,
                                '" + message + @"',
                                NOW(),
                                
                                (
                                    SELECT
                                        DISTINCT s.code_user
                                    FROM
                                        USER s
                                    WHERE
                                        s.name_user='" + emisor + @"'
                                ),
                                " + status + @",
                                (
                                    SELECT DISTINCT
                                        uc.code_chat
                                    FROM
                                        user_chat AS uc
                                    INNER JOIN USER u
                                    ON
                                        uc.code_user = u.code_user
                                    WHERE
                                        uc.code_chat IN
                                        (
                                            SELECT
                                                user_chat.code_chat
                                            FROM
                                                user_chat
                                            INNER JOIN USER
                                            ON
                                                user_chat.code_user = user.code_user
                                            WHERE
                                                user.name_user = '" + emisor + @"'
                                        )
                                    AND u.name_user = '" + receptor + @"'
                                )";


            return _conection.sendSetDataMariaDB(sql); ;
        }
      
        public DataTable NotificadorChat(String idemisor,String idgrupo,String Idreceptor) {
            String sql = "";
            return _conection.getDataMariaDB(sql).Tables[0];
        }

    }
}
