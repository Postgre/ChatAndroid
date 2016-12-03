using System;

using System.Collections.Generic;
using System.Linq;
using System.Text;
using SuperWebSocket;
using SuperSocket.SocketBase;
using System.Web.Script.Serialization;
using WebSocketsServerDemo.Controllers;
using System.Data;

namespace WebSocketsServerDemo
{
    class Server
    {
        private WebSocketServer appServer;
        private Controller _controller = new Controller();
        private List<Metadata> _userOnline = new List<Metadata>();
        private GlobalType _Global = new GlobalType();

        #region CONFIGURACIÓN DEL SERVIDOR
        public void Setup()
        {
            Console.WriteLine();

            appServer = new WebSocketServer();

            if (!appServer.Setup(2012)) //Setup with listening port
            {
                Console.ReadKey();
                return;
            }

            appServer.NewSessionConnected   += new SessionHandler<WebSocketSession>(NewSessionConnected);
            appServer.SessionClosed         += new SessionHandler<WebSocketSession, CloseReason>(SessionClosed);
            appServer.NewMessageReceived    += new SessionHandler<WebSocketSession, string>(NewMessageReceived);
            Console.WriteLine();
        }
        
        public void Start()
        {
            if (!appServer.Start())
            {
                Console.WriteLine("Failed to start!");
                Console.ReadKey();
                return;
            }

            Console.WriteLine("El servidor se ha iniciado satisfactoriamente!Pulse cualquier tecla para ver las opciones.");
            Console.ReadKey();
            ShowAvailableOptions(); 

            char keyStroked;

            while (true)
            {
                keyStroked = Console.ReadKey().KeyChar;

                if (keyStroked.Equals('q'))
                {
                    Stop();
                    return;
                }
                
                ShowAvailableOptions();
                continue;
            }
        }

        public void Stop()
        {
            appServer.Stop();

            Console.WriteLine();
            Console.WriteLine("The server was stopped!");
        }

        public void ShowAvailableOptions()
        {
            Console.WriteLine();
            Console.WriteLine("Press 'q' key to stop the server.");
        }

        #endregion 

        private void NewMessageReceived(WebSocketSession session, string message)
        {
            dynamic obj = ConvertJsonObject(message);

            string type = obj.type;
            string receptor = obj.codeReceptor;
            switch (type)
            {
                case GlobalType.CONNECT:
                
                    Metadata data = new Metadata{ 
                                                  CodeUser = obj.codeEmisor,
                                                  SessionId=session.SessionID.ToString(),
                                                  Ip = session.RemoteEndPoint.ToString()
                                                };
                 
                    _userOnline.Add(data);
                    enviarMensaje(session, message);
                    break;
                case GlobalType.MESSAGE:   // chat de uno a uno

                    Metadata _receiverUser = _userOnline.Find(x => x.CodeUser.Contains(receptor));
                    String idemisor = obj.codeEmisor;
                    String idreceptor = obj.codeReceptor;
                    String idgrupo = obj.group;
                    String mensaje = obj.send;
                    String estado = _receiverUser != null?"0":"1";
                    _controller.enviarMensaje(idemisor,idreceptor,mensaje,estado);
                    
                    enviarChat(session, message, _receiverUser);

                        
                    break;

                case GlobalType.NOTIFICATION:
                    
                    Console.WriteLine(message);

                    foreach (Metadata _user in _userOnline)
                    {
                        String _JSON = @"
                                {" +
                                           "\"type\": \"" + GlobalType.CONNECT + "\"," +
                                            "\"codeEmisor\" :\"" + _user.CodeUser + "\"" +
                                        "}";
                        session.Send(_JSON);
                        Console.WriteLine(_JSON);
                    }
                    
                    break;
                case GlobalType.STOPMESSEGE:   // descarga todo los mensajes pendiente guardados en la BD.

                   break;

            }
            
        }

        private void NewSessionConnected(WebSocketSession session)
        {
          
           // DataTable dt = _controller.NotificadorGrupo("", "");
            Console.WriteLine();
            Console.WriteLine("Usuario Conectado: " + appServer.SessionCount);

        }

        private void SessionClosed(WebSocketSession session, CloseReason value)
        {
            Metadata _useroffline = _userOnline.Find(x => x.SessionId.Contains(session.SessionID.ToString()));
          
            
            if (_useroffline != null)
            {
                String _JSON = @"
                                {" +
                                   "\"type\": \"" + GlobalType.DISCONECTED + "\"," +
                                    "\"codeEmisor\" :\""  + _useroffline.CodeUser + "\"" +
                                "}";
                
                         

                foreach (WebSocketSession sessions in appServer.GetAllSessions())
                {
                    sessions.Send(_JSON);
                    Console.WriteLine(_JSON);
                }
                _userOnline.Remove(_useroffline);
            }

            Console.WriteLine();
            Console.WriteLine("Usuario Desconectado: " + appServer.SessionCount);
        }

        private void enviarChat(WebSocketSession session,string json,Metadata metadata) {
            
            foreach (WebSocketSession sessions in appServer.GetAllSessions())
            {
                Console.WriteLine(json);
                if(metadata != null){
                    if (metadata.SessionId.Equals(sessions.SocketSession.SessionID.ToString())) {
                        sessions.Send(json);
                        
                        Console.WriteLine(json);
                    }
                }
            }
            session.Send(json);
        }
        private void enviarMensaje(WebSocketSession session, string json)
        {

            foreach (WebSocketSession sessions in appServer.GetAllSessions())
            {
                Console.WriteLine(json);
                     sessions.Send(json);
            }
        }
        private void enviarNotificacion(WebSocketSession session, string json)
        {

            foreach (WebSocketSession sessions in appServer.GetAllSessions())
            {
                if (sessions == session)
                {
                    sessions.Send(json);
                    Console.WriteLine(json);
                }
            }
        }

        #region ConvertJsonObject
        private dynamic ConvertJsonObject(String json)
        {
            
            var serializer = new JavaScriptSerializer();
            serializer.RegisterConverters(new[] { new DynamicJsonConverter() });
            dynamic obj = serializer.Deserialize(json, typeof(object));
            return obj;
          
        }
        #endregion
    }
}