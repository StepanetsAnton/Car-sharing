package carsharing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao{
    Connection conn = null;
    Statement stmt = null;

    public CompanyDaoImpl(Connection conn, Statement stmt) throws SQLException{
        this.conn=conn;
        this.stmt = stmt;
    }
    String sql;
    public void deleteCompany(int id, String name) throws SQLException{
        sql = "DELETE FROM "+name+" WHERE ID = "+id;
        stmt.executeUpdate(sql);
    }
    public void getAllCompanies(String name) throws SQLException{
        sql = "SELECT ID, NAME FROM "+name;
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            int id = rs.getInt("ID");
            String name1 = rs.getString("NAME");
            System.out.print(id);
            System.out.print(". "+name1);
            System.out.println();
        }
        rs.close();
    }
    public Company getCompany(int id, String name) throws SQLException{
        sql = "SELECT ID, NAME FROM "+name+" WHERE ID = "+id;
        ResultSet rs = stmt.executeQuery(sql);
        int id1 = 0;
        String name1 = "";
        while (rs.next()){
            id1 = rs.getInt("ID");
            name1 = rs.getString("NAME");
        }
        Company comp = new Company(id1,name1);
        rs.close();
        return comp;
    }
    public void updateCompany(Company company, String name){

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
