package com.example.alejandro.udlamsg.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oswaldo on 25/11/2016.
 */
public class SingletonChatTemporal {
    private static SingletonChatTemporal ourInstance = new SingletonChatTemporal();
    private List<Message> ListaChat = new ArrayList<Message>() {
    };

    public static SingletonChatTemporal getInstance() {
         return ourInstance;
    }

    private SingletonChatTemporal() {
    }

    public List<Message> getListaChat() {
        return ListaChat;
    }

    public void setListaChat(List<Message> listaChat) {
        ListaChat = listaChat;
    }
    public List<Message> HistoricoMessages(String CodeEmisor,String Receptor){
        List<Message> historicoChat = new ArrayList<Message>();

        for (Message _send:ListaChat) {
             if (_send.getType().equals(GlobalType.MESSAGE)){
                 if ((_send.getCodeEmisor().equals(CodeEmisor) && _send.getCodeReceptor().equals(Receptor)) || (_send.getCodeEmisor().equals(Receptor) && _send.getCodeReceptor().equals(CodeEmisor))){
                    historicoChat.add(_send);
                 }
             }

        }
        return historicoChat;
    }


}
