package carsharing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class CarDB {
    Connection conn = null;
    Statement stmt = null;

    public CarDB(Connection conn, Statement stmt) throws SQLException{
        this.conn=conn;
        this.stmt = stmt;
    }
    String sql;
    public void deleteCar(int id, String name) throws SQLException{
        sql = "DELETE FROM "+name+" WHERE ID = "+id;
        stmt.executeUpdate(sql);
    }
    public void getAllCars(String name, int companyId) throws SQLException{
        sql = "SELECT NAME FROM "+name+" WHERE COMPANY_ID = "+companyId;
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
    public void getAvailableCars(int companyId) throws SQLException{
        sql = "SELECT CAR.NAME FROM CAR LEFT JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID WHERE COMPANY_ID = "+companyId+" AND CUSTOMER.RENTED_CAR_ID IS NULL;";
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
    public void rentCar(int compId, int compCarId, int custId)throws SQLException{
        sql = "SELECT ID, NAME FROM CAR WHERE COMPANY_ID = "+compId;
        ResultSet rs = stmt.executeQuery(sql);
        Map<String, Integer> carsOfComp = new HashMap<>();
        List<String> carNames = new LinkedList<>();
        int tempId = 0;
        String tempName;
        while (rs.next()){
            tempId = rs.getInt("ID");
            tempName = rs.getString("NAME");
            carsOfComp.put(tempName,tempId);
            carNames.add(tempName);
        }
        rs.close();

        String carName = carNames.get(compCarId-1);

        int finalId = carsOfComp.get(carName);

        sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = "+finalId+" WHERE ID = "+custId;
        stmt.executeUpdate(sql);

        System.out.println("You rented '"+carName+"'");
    }
    public boolean isEmpty(String name, int companyId)throws SQLException{
        StringBuilder str = new StringBuilder();
        sql = "SELECT ID, NAME FROM "+name+" WHERE COMPANY_ID = "+companyId;
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