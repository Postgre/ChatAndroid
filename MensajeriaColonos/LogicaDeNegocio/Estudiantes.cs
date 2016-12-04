using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using MensajeriaColonos;


namespace LogicaDeNegocio
{
  public  class Estudiantes
    {
        
        public string NombreEstudiante { get; set; }
        public int IdentificacionEstudiante { get; set; }
        public Byte[] Foto { get; set; }

        public Estudiantes(int id, string nombreEstu, Byte[] foto)
        {
            IdentificacionEstudiante = id;
            NombreEstudiante = nombreEstu;
            Foto = foto;
        }
        public Estudiantes()
        {

        }

        public static List<Estudiantes> ListaDeEstudiantes(int Codigo_materia)
        {

            List<Estudiantes> Lista = new List<Estudiantes> { };
            DataTable datos = new DataTable();

            string consulta = "select  person.id, person.name from course  inner join enrollment on course.code_course = enrollment.code_course inner join person on person.id = enrollment.id_student where course.code_course=" + Codigo_materia + ";";
            datos = ConexionColonos.EjecutarConsulta(consulta);
            DataTable Fotos = new DataTable();
            byte[] imagenes = null;
            for (int i = 0; i < datos.Rows.Count; i++)
            {
                try
                {
                    string consultados = "SELECT photo_profile FROM profile inner join user on user.code_user = profile.code_user where user.code_user = " + datos.Rows[i]["id"].ToString() + ";";
                    Fotos = MensajeriaColonos.ConexionChat.EjecutarConsulta(consultados); 
                    imagenes = new byte[0];
                    imagenes = (Byte[])Fotos.Rows[i]["photo_profile"];

                }
                catch (Exception)
                {
                    imagenes = null;
                }
                Estudiantes estudiante = new Estudiantes(Convert.ToInt32(datos.Rows[i]["id"].ToString()), datos.Rows[i]["name"].ToString(), imagenes);



                Lista.Add(estudiante);
            }

            return Lista;


        }
    }
}


