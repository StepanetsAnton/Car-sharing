package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        String dbPath = "jdbc:h2:./src/carsharing/db/" + (args.length > 0 ? args[1] : "carsharing");
        Connection connection = DriverManager.getConnection(dbPath);
        connection.setAutoCommit(true);
        Statement statement = connection.createStatement();

        /*String sql = "DROP TABLE IF EXISTS CUSTOMER;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE IF EXISTS CAR;";
        statement.executeUpdate(sql);
        sql = "DROP TABLE IF EXISTS COMPANY;";
        statement.executeUpdate(sql);*/

        String sql = "CREATE TABLE IF NOT EXISTS COMPANY (\n" +
                "    ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "    NAME VARCHAR NOT NULL UNIQUE\n" +
                ");";
        statement.executeUpdate(sql);

        sql = "ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1";
        statement.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS CAR (\n" +
                "    ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "    NAME VARCHAR NOT NULL UNIQUE,\n" +
                "    COMPANY_ID INT NOT NULL, FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)\n" +
                ");";
        statement.executeUpdate(sql);

        sql = "ALTER TABLE CAR ALTER COLUMN ID RESTART WITH 1";
        statement.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS CUSTOMER (\n" +
                "    ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "    NAME VARCHAR NOT NULL UNIQUE,\n" +
                "    RENTED_CAR_ID INT, FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)\n" +
                ");";
        statement.executeUpdate(sql);
        sql = "ALTER TABLE CUSTOMER ALTER COLUMN ID RESTART WITH 1";
        statement.executeUpdate(sql);

        CompanyDao companyDao = new CompanyDaoImpl(connection,statement);
        CarDB carDAO = new CarDB(connection,statement);
        CustomerDB customerDAO = new CustomerDB(connection,statement);

        String tableName = "COMPANY";
        boolean exit = false;
        Scanner sc = new Scanner(System.in);
        while(!exit){
            System.out.println("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");
            int opt = sc.nextInt();
            System.out.println();
            switch (opt){
                case 1:
                    boolean ex = false;
                    while(!ex) {
                        System.out.println("1. Company list\n2. Create a company\n0. Back");
                        int option = sc.nextInt();
                        System.out.println();
                        switch (option) {
                            case 1:
                                if(companyDao.isEmpty(tableName)){
                                    System.out.println("The company list is empty!");
                                }else{
                                    System.out.println("Choose the company:");
                                    companyDao.getAllCompanies(tableName);
                                    System.out.println("0. Back");
                                    int number = sc.nextInt();
                                    if(number!=0) {
                                        System.out.println();
                                        System.out.println(companyDao.getCompany(number, tableName).getName() + " company");

                                        boolean ex1 = false;
                                        while (!ex1) {
                                            System.out.println("1. Car list\n2. Create a car\n0. Back");
                                            int n = sc.nextInt();
                                            System.out.println();
                                            switch (n) {
                                                case 1:
                                                    if (carDAO.isEmpty("CAR",number)) {
                                                        System.out.println("The car list is empty!");
                                                        System.out.println();
                                                    } else {
                                                        System.out.println("Car list:");
                                                        carDAO.getAllCars("CAR", number);
                                                        System.out.println();
                                                    }
                                                    break;
                                                case 2:
                                                    System.out.println("Enter the car name:");
                                                    sc.nextLine();
                                                    String carName = sc.nextLine();
                                                    sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" + carName + "', " + number + ");";
                                                    statement.executeUpdate(sql);
                                                    System.out.println("The car was added!");
                                                    System.out.println();
                                                    break;
                                                case 0:
                                                    ex1 = true;
                                                    break;
                                            }
                                        }
                                    }
                                }
                                System.out.println();
                                break;
                            case 2:
                                System.out.println("Enter the company name:");
                                sc.nextLine();
                                String compName = sc.nextLine();
                                sql = "INSERT INTO " + tableName + " (NAME) VALUES ('" + compName + "');";
                                statement.executeUpdate(sql);
                                System.out.println("The company was created!");
                                System.out.println();
                                break;
                            case 0:
                                ex = true;
                                break;
                        }
                    }
                    break;
                case 2:
                    if(customerDAO.isEmpty("CUSTOMER")){
                        System.out.println("The customer list is empty!");
                        System.out.println();
                    }else {
                        System.out.println("Choose a customer:");
                        customerDAO.getAllCustomers("CUSTOMER");
                        System.out.println("0. Back");
                        int cust = sc.nextInt();
                        System.out.println();

                        int compForRentId = 0;

                        if (cust != 0) {

                            boolean ex3 = false;
                            while (!ex3) {
                                System.out.println("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");



                                int custOpt = sc.nextInt();
                                System.out.println();
                                switch (custOpt) {
                                    case 1:
                                        if (customerDAO.hasRent(cust)) {
                                            System.out.println("You've already rented a car!");
                                        } else {
                                            if (companyDao.isEmpty("COMPANY")) {
                                                System.out.println("The company list is empty!");
                                            } else {
                                                System.out.println("Choose a company:");
                                                companyDao.getAllCompanies("COMPANY");
                                                System.out.println("0. Back");
                                                compForRentId = sc.nextInt();
                                                System.out.println();
                                                if (compForRentId != 0) {
                                                    Company compForRent = companyDao.getCompany(compForRentId, "COMPANY");
                                                    if (carDAO.isEmpty("CAR", compForRentId)) {
                                                        System.out.println("No available cars in the " + compForRent.getName() + " company");
                                                    } else {
                                                        System.out.println("Choose a car:");
                                                        carDAO.getAvailableCars(compForRentId);
                                                        System.out.println("0. Back");
                                                        System.out.println();
                                                        int carForRentId = sc.nextInt();
                                                        if (carForRentId != 0) {
                                                            carDAO.rentCar(compForRentId, carForRentId, cust);
                                                            System.out.println();
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    case 2:
                                        if (!customerDAO.hasRent(cust)) {
                                            System.out.println("You didn't rent a car!");
                                            System.out.println();
                                        } else {
                                            sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID =" + cust;
                                            statement.executeUpdate(sql);
                                            System.out.println("You've returned a rented car!");
                                            System.out.println();
                                        }
                                        break;
                                    case 3:
                                        if (!customerDAO.hasRent(cust)) {
                                            System.out.println("You didn't rent a car!");
                                            System.out.println();
                                        } else {
                                            System.out.println("Your rented car:");
                                            System.out.println(customerDAO.getCustCar(cust));
                                            System.out.println("Company:");
                                            Company custComp = companyDao.getCompany(compForRentId, "COMPANY");
                                            System.out.println(custComp.getName());
                                            System.out.println();
                                        }
                                        break;
                                    case 0:
                                        ex3 = true;
                                        break;
                                }
                            }
                        }

                    }
                    break;
                case 3:
                    System.out.println("Enter the customer name:");
                    sc.nextLine();
                    String custName = sc.nextLine();
                    sql = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES ('" + custName + "', NULL);";
                    statement.executeUpdate(sql);
                    System.out.println("The customer was added!\n");
                    break;
                case 0:
                    exit = true;
                    break;
            }
        }


        statement.close();
        connection.close();
    }
}