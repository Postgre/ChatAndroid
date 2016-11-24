using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WebSocketsServerDemo
{
    class TemplateData
    {
        public const string TYPE = ":type:";
        public const string GROUP = ":group:";
        public const string CODEEMISOR = ":codeEmisor:";
        public const string CODERECEPTOR = ":codeReceptor:";
        public const string NICK = ":nick:";
        public const string SEND = ":send:";
        public const string FILE = ":file:";

        public String _JSON = @" data =
                                {
                                    'type': ':type:',
                                    'group': ':group:',
                                    'codeEmisor' : ':codeEmisor:',
                                    'codeReceptor' : ':codeReceptor:',
                                    'nick' : ':nick:',
                                    'send' : ':send:',
                                    'file' : ':file:'
                                }   
                             ";
     
        
    }
}
