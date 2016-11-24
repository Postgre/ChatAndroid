using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WebSocketsServerDemo
{
    class Mensaje
    {
        String type, group, codeEmisor, codeReceptor, nick, send, file;

        public String File
        {
            get { return file; }
            set { file = value; }
        }

        public String Send
        {
            get { return send; }
            set { send = value; }
        }

        public String Nick
        {
            get { return nick; }
            set { nick = value; }
        }

        public String CodeReceptor
        {
            get { return codeReceptor; }
            set { codeReceptor = value; }
        }

        public String CodeEmisor
        {
            get { return codeEmisor; }
            set { codeEmisor = value; }
        }

        public String Group
        {
            get { return group; }
            set { group = value; }
        }

        public String Type
        {
            get { return type; }
            set { type = value; }
        }
    }
}
