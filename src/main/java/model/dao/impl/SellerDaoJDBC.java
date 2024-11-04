package model.dao.impl;

import db.DB;
import db.exception.DbException;
import model.dao.interfaces.SellerDao;
import model.dao.mapper.DepartmentDaoMapper;
import model.dao.mapper.SellerDaoMapper;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private final Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(
                    "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "VALUES (?, ?, ?, ? , ?)");

            pstm.setString(1, seller.getName());
            pstm.setString(2, seller.getEmail());
            pstm.setDate(3, new Date(seller.getBirthDate().getTime()));
            pstm.setDouble(4, seller.getSalary());
            pstm.setInt(5, seller.getDepartment().getId());

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Unexpected error! No rows affected");
            }

        } catch (SQLException e) {
            throw new DbException("Failed to insert new seller. Caused by: " + e.getMessage());
        } finally {
            closeConnections(pstm, null);
        }

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            pstm = conn.prepareStatement(
                    "SELECT seller.*, department.name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?");
            pstm.setInt(1, id);
            rs = pstm.executeQuery();

            if (rs.next()) {
                Department department = DepartmentDaoMapper.map(rs);
                return SellerDaoMapper.map(rs, department);
            }

            return null;
        } catch (SQLException e) {
            throw new DbException("Failed to find seller with id: " + id + ". Caused by: " + e.getMessage());
        } finally {
            closeConnections(pstm, rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Seller> sellers = new ArrayList<>();
        Map<Integer, Department> departmentMap = new HashMap<>();

        try {
            pstm = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name");

            rs = pstm.executeQuery();

            while (rs.next()) {
                Department dep = getDepartment(departmentMap, rs);
                sellers.add(SellerDaoMapper.map(rs, dep));
            }

            return sellers;
        } catch (SQLException e) {
            throw new DbException("Failed to fetch all sellers. Caused by: " + e.getMessage());
        } finally {
            closeConnections(pstm, rs);
        }

    }

    @Override
    public List<Seller> findByDepartmentId(Integer id) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Seller> sellers = new ArrayList<>();
        Map<Integer, Department> departmentMap = new HashMap<>();

        try {
            pstm = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ? " +
                            "ORDER BY Name");
            pstm.setInt(1, id);
            rs = pstm.executeQuery();

            while (rs.next()) {
                Department dep = getDepartment(departmentMap, rs);
                sellers.add(SellerDaoMapper.map(rs, dep));
            }

            return sellers;
        } catch (SQLException e) {
            throw new DbException("Failed to find sellers by department: " + id + ". Caused by: " + e.getMessage());
        } finally {
            closeConnections(pstm, rs);
        }
    }

    private void closeConnections(PreparedStatement pstm, ResultSet rs) {
        if (pstm != null) DB.closeStatement(pstm);
        if (rs != null) DB.closeResultSet(rs);
    }

    private Department getDepartment(Map<Integer, Department> departmentMap, ResultSet rs) {
        try {
            Department department = departmentMap.get(rs.getInt("DepartmentId"));

            if (department == null) {
                department = DepartmentDaoMapper.map(rs);
                departmentMap.put(rs.getInt("DepartmentId"), department);
            }

            return department;

        } catch (SQLException e) {
            throw new DbException("Failed to get department from dictionary. Caused by " + e.getMessage());
        }
    }
}
