using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using MensajeriaColonos;

namespace LogicaDeNegocio

{
   
    
 public   class Cursos
    {

      public int CodCurso { get; set;}
      public   String NombreCurso { get; set; }
      public String NombreAsignatura { get; set; }

        public Cursos(int Codcurso, String Nombrecurso, String Nombreasignatura) {
            CodCurso = Codcurso;
            NombreCurso = Nombrecurso;
            NombreAsignatura = Nombreasignatura;
            }

        public Cursos() { }

       



        public static List<Cursos> CursosLista(int id)
        {
            List<Cursos> listadoCursos = new List<Cursos> { };

            DataTable Datos = new DataTable();

            String consulta = "select course.number_course, course.code_course, subject.name_subject from course  inner join enrollment on course.code_course = enrollment.code_course inner join person on person.id = enrollment.id_student inner join  detail_curriculum on detail_curriculum.code_curriculum= course.code_curriculum and course.code_subject = detail_curriculum.code_subject  inner join subject on subject.code_subject= detail_curriculum.code_subject where person.id =" + id + ";";
            Datos = ConexionColonos.EjecutarConsulta(consulta);

            for (int i = 0; i < Datos.Rows.Count; i++)
            {
                Cursos c = new Cursos(Convert.ToInt32(Datos.Rows[i]["code_course"].ToString()), Datos.Rows[i]["number_course"].ToString(), Datos.Rows[i]["name_subject"].ToString());
                listadoCursos.Add(c);

            }
            return listadoCursos;



        }

    }
}





