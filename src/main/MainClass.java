package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args){
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/School", "postgres", "2ed2bfA1");
            testConnectionToDatabase(con);
            Statement statement = con.createStatement();
            createGradesTable(statement);
            createSubjectsTable(statement);
            createStudentsTable(statement);
            writeDataToDatabase(statement);

            con.close();
        } catch (Exception e) {
            System.out.println("Error during operation on database");
            e.printStackTrace();
        }
    }

    private static void testConnectionToDatabase(Connection con) {
        if(!con.equals(null)) {
            System.out.println("Success");
        }
        else {
            System.out.println("No connection to database");
        }
    }

    private static void createGradesTable(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS grades (student_id int,"
                + "grade int,"
                + "date varchar(255),"
                + "subject_id int);");
    }

    private static void createSubjectsTable(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS subjects (subject_id int,"
                + "subject_name varchar(255),"
                + "teacher_firstName varchar(255),"
                + "teacher_lastName varchar(255));");
    }

    private static void createStudentsTable(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS students (student_id int,"
                + "student_lastName varchar(255),"
                + "student_firstName varchar(255),"
                + "street varchar(255),"
                + "street_number int,"
                + "class varchar(255));");
    }

    private static void writeDataToDatabase(Statement statement) throws SQLException {
        Scanner scanner = loadFile("oceny.txt");
        subWrite1(statement, scanner);
        scanner.close();

        scanner = loadFile("przedmioty.txt");
        subWrite2(statement, scanner);
        scanner.close();

        scanner = loadFile("uczniowie.txt");
        subWrite3(statement, scanner);
        scanner.close();
    }

    private static void subWrite3(Statement statement, Scanner scanner) throws SQLException {
        scanner.nextLine();
        while(scanner.hasNextLine()) {
            String[] singleRow = scanner.nextLine().split(";");
            statement.executeUpdate(String.format("INSERT INTO students(student_id, student_lastName, student_firstName, street, street_number, class) VALUES(%d, '%s', '%s', '%s', %d, '%s');",
                    Integer.parseInt(singleRow[0]),
                    singleRow[1],
                    singleRow[2],
                    singleRow[3],
                    Integer.parseInt(singleRow[4]),
                    singleRow[5]));
        }
    }

    private static void subWrite2(Statement statement, Scanner scanner) throws SQLException {
        scanner.nextLine();
        while(scanner.hasNextLine()) {
            String[] singleRow = scanner.nextLine().split(";");
            statement.executeUpdate(String.format("INSERT INTO subjects(subject_id, subject_name, teacher_firstName, teacher_lastName)"
                            + "VALUES(%d, '%s', '%s', '%s');",
                    Integer.parseInt(singleRow[0]),
                    singleRow[1],
                    singleRow[2],
                    singleRow[3]));
        }
    }

    private static void subWrite1(Statement statement, Scanner scanner) throws SQLException {
        scanner.nextLine();
        while(scanner.hasNextLine()) {
            String[] singleRow = scanner.nextLine().split(";");
            statement.executeUpdate(String.format("INSERT INTO grades(student_id, grade, date, subject_id)"
                            + "VALUES(%d, %d, '%s', %d);",
                    Integer.parseInt(singleRow[0]),
                    Integer.parseInt(singleRow[1]),
                    singleRow[2],
                    Integer.parseInt(singleRow[3])));
        }
    }

    private static Scanner loadFile(String fileName) {
        try {
            return new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Error during loading the file");
            return null;
        }
    }
}
