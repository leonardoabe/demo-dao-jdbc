package model.dao.mapper;

import exceptions.MapperException;
import model.entities.Department;
import model.entities.Seller;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SellerDaoMapper {

    public static Seller map(ResultSet rs, Department department) {
        Seller seller = new Seller();
        try {
            seller.setId(rs.getInt("Id"));
            seller.setName(rs.getString("Name"));
            seller.setEmail(rs.getString("Email"));
            seller.setBirthDate(rs.getDate("BirthDate"));
            seller.setSalary(rs.getDouble("BaseSalary"));
            seller.setDepartment(department);

            return seller;
        } catch (SQLException e) {
            throw new MapperException("Failed to map seller from DB: " + e.getMessage());
        }
    }
}
