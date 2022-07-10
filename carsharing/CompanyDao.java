package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDao {
    public void getAllCompanies(String name)throws SQLException;
    public Company getCompany(int id, String name)throws SQLException;
    public void updateCompany(Company company, String name)throws SQLException;
    public void deleteCompany(int id, String name)throws SQLException;
    public boolean isEmpty(String name)throws SQLException;
}
