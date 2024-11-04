package model.dao.impl;

import db.DbUtils;
import db.exception.DbException;
import model.dao.interfaces.DepartmentDao;
import model.entities.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private final Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(
                    "INSERT INTO department (Name) " +
                            "VALUES (?)"
            );

            pstm.setString(1, department.getName());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("Unexpected error! No rows affected");
            }
        } catch (SQLException e) {
            throw new DbException("Failed to insert new department. Caused by: " + e.getMessage());
        } finally {
            DbUtils.closeConnections(pstm, null);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(
                    "UPDATE department " +
                            "SET Name = ? " +
                            "WHERE Id = ?"
            );

            pstm.setString(1, department.getName());
            pstm.setInt(2, department.getId());

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) throw new DbException("Unexpected Error! No rows affected");

        } catch (SQLException e) {
            throw new DbException("Failed to update department. Caused by: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(
                    "DELETE FROM department " +
                            "WHERE Id = ?"
            );

            pstm.setInt(1, id);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) throw new DbException("Unexpected error! No rows affected");

        } catch (SQLException e) {
            throw new DbException("Failed to delete department. Caused by: " + e.getMessage());
        } finally {
            DbUtils.closeConnections(pstm, null);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            pstm = conn.prepareStatement(
                    "SELECT * FROM department " +
                            "WHERE Id = ?"
            );

            pstm.setInt(1, id);

            rs = pstm.executeQuery();

            if (rs.next()) {
                Department department = new Department();
                department.setId(rs.getInt("Id"));
                department.setName(rs.getString("Name"));

                return department;
            }

            return null;
        } catch (SQLException e) {
            throw new DbException("Failed to fetch department with id: " + id + ". Caused by: " + e.getMessage());
        } finally {
            DbUtils.closeConnections(pstm, rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Department> departments = new ArrayList<>();

        try {
            pstm = conn.prepareStatement("SELECT * FROM department");
            rs = pstm.executeQuery();

            while (rs.next()) {
                Department department = new Department();
                department.setId(rs.getInt("Id"));
                department.setName(rs.getString("Name"));

                departments.add(department);
            }

            return departments;
        } catch (SQLException e) {
            throw new DbException("Failed to fetch departments. Caused by: " + e.getMessage());
        } finally {
            DbUtils.closeConnections(pstm, rs);
        }
    }
}
