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

        public bool enviarMensaje(string emisor,string receptor,string message, string status){  // se graban los mensajes en la bd.. 
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

        public DataTable MensajesPendientes(String idemisor, String Idreceptor)   //los mensajes pendiente se envian al celular
        {
            String sql = @"SELECT
                                m.code_message,
                                m.message,
                                m.user_sends AS emisor,
                                rm.code_user AS receptor
                            FROM
                                record_message rm
                            INNER JOIN chat c
                            ON
                                rm.code_chat = c.code_chat
                            INNER JOIN MESSAGE m
                            ON
                                rm.code_message = m.code_message
                            INNER JOIN USER u
                            ON
                                rm.code_user = u.code_user
                            INNER JOIN USER us
                            ON
                                m.user_sends = us.code_user
                            WHERE
                                us.name_user = '" + idemisor + @"'
                            AND u.name_user='" + Idreceptor + @"'
                            AND m.state=1";
            return _conection.getDataMariaDB(sql).Tables[0];
        }

        public bool MensajesLeidos(string emisor,string receptor){   // Los mensajes que estaban pendiente pasan a estado leidos. 
            string sql = "";
            return _conection.sendSetDataMariaDB(sql);
        }
        

    }
}
