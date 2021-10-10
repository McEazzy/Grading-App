/**
 * PURPOSE: storing table schema's definition for database storage.
 * AUTHOR: MINH VU
 * LAST MODIFIED DATE: 5/09/2021
 */
package curtin.edu.assignment01.database;

public class CourseSchemas
{
    public static class AdminTable
    {
        //Relational schema's table with 2 attributes:
        public static final String NAME = "admin";
        public static final class Cols{
            public static final String PIN = "pin";
            public static final String NAME = "name";
        }
    }

    public static class StudentTable
    {
        //Relational schema's table with 2 attributes:
        public static final String NAME = "student";
        public static final class Cols{
            public static final String USER = "user_name";
            public static final String PIN = "pin";
            public static final String NAME = "full_name";
            public static final String EMAIL = "email";
            public static final String COUNTRY = "country";
            public static final String PRAC = "practical_marks";
            public static final String BY = "create_by";
        }
    }

    public static class InstructorTable
    {
        //Relational schema's table with 2 attributes:
        public static final String NAME = "instructor";
        public static final class Cols{
            public static final String USER = "user_name";
            public static final String PIN = "pin";
            public static final String NAME = "full_name";
            public static final String EMAIL = "email";
            public static final String COUNTRY = "country";
        }
    }

    public static class PracticalTable
    {
        //Relational schema's table with 2 attributes:
        public static final String NAME = "practical";
        public static final class Cols{
            public static final String TITLE = "title";
            public static final String DES = "description";
            public static final String MARK = "max_mark";
            public static final String START = "start_marking";
        }
    }
}
