package model.dao.mapper;

import exceptions.MapperException;
import model.entities.Department;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentDaoMapper {
    public static Department map(ResultSet rs) {
        Department department = new Department();

        try {
            department.setId(rs.getInt("DepartmentId"));
            department.setName(rs.getString("DepName"));

            return department;
        } catch (SQLException e) {
            throw new MapperException("Failed to map department from DB: " + e.getMessage());
        }
    }
}
