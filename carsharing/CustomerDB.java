package carsharing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class CustomerDB {
    Connection conn = null;
    Statement stmt = null;

    public CustomerDB(Connection conn, Statement stmt) throws SQLException{
        this.conn=conn;
        this.stmt = stmt;
    }
    String sql;
    public void deleteCust(int id, String name) throws SQLException{
        sql = "DELETE FROM "+name+" WHERE ID = "+id;
        stmt.executeUpdate(sql);
    }
    public void getAllCustomers(String name) throws SQLException{
        sql = "SELECT NAME FROM "+name;
        ResultSet rs = stmt.executeQuery(sql);
        int count = 1;
        while(rs.next()){
            String name1 = rs.getString("NAME");
            System.out.print(count);
            System.out.print(". "+name1);
            System.out.println();
            count++;
        }
        rs.close();
    }
    public String getCustCar(int id) throws SQLException{
        sql = "SELECT CAR.NAME FROM CAR LEFT JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID WHERE CUSTOMER.ID = "+id;
        ResultSet rs = stmt.executeQuery(sql);
        String name1 = "";
        while (rs.next()){
            name1 = rs.getString("NAME");
        }

        rs.close();
        return name1;
    }
    public boolean hasRent(int custId) throws SQLException{
        StringBuilder str = new StringBuilder();
        sql = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = "+custId+" AND RENTED_CAR_ID IS NULL";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            int rent = rs.getInt("RENTED_CAR_ID");
            str.append(rent);
        }
        rs.close();
        if(str.length()==0){
            return true;
        }else{
            return false;
        }
    }

    public boolean isEmpty(String name)throws SQLException{
        StringBuilder str = new StringBuilder();
        sql = "SELECT ID, NAME FROM "+name;
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            int id = rs.getInt("ID");
            String name1 = rs.getString("NAME");
            System.out.print(id);
            str.append(id);
            str.append(name1);
        }
        rs.close();
        if(str.length()==0){
            return true;
        }else{
            return false;
        }
    }
}
