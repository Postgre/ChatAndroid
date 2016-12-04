using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using LogicaDeNegocio;


namespace Servicios
{
    /// <summary>
    /// Summary description for WebService
    /// </summary>
    [WebService(Namespace = "http://tempuris.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class WebService : System.Web.Services.WebService
    {

       

        [WebMethod]

        public int Ingresar(String usuario, String contraseña) {
            return Usuario.Logeo(usuario, contraseña);
        }

        [WebMethod]

        public List<Cursos> ListadoCursos(int id_usuario){

            return Cursos.CursosLista(id_usuario);
            
            
            }

        [WebMethod]

        public int EnviarMensaje(String mensaje, byte[] adjunto, double CodUsuario, int CodChat) {

            return Mensaje.EnviarMensaje(mensaje, adjunto, CodUsuario, CodChat);
        }

        
        [WebMethod]
       public List<Chat> ConUltoMsg(int codigo_usuario)
        {

            return Chat.ConsUltMsg(codigo_usuario);
        }
      
        [WebMethod]
        public bool AbrirCrearChat(int codigo_usuario1, int codigo_usuario2)
        {
            return Chat.AbrirCrearChat(codigo_usuario1, codigo_usuario2);
        }

        [WebMethod]

        public bool CerrarChat(int codigo_usuario, int codigo_chat)
        {
            return Chat.CerrarChat(codigo_usuario, codigo_chat);
        }

        [WebMethod]

        public List<Estudiantes> EstudiantesDelCurso(int codigo)
        {

            return Estudiantes.ListaDeEstudiantes(codigo);
        }

        [WebMethod]
        public String consulta_usuario(int id)
        {
            return Usuario.consultarusuario(id);
        }
        [WebMethod]
        public bool ActualizarPerfil(byte[] imagen, int codigo_usuario)
        {
            return Usuario.Actualizar_Perfil(imagen, codigo_usuario);
        }
    }
}
