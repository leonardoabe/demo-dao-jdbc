package model.dao.impl;

import db.DB;
import db.exception.DbException;
import model.dao.interfaces.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private final Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {

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
                Department department = new Department();
                department.setId(rs.getInt("DepartmentId"));
                department.setName(rs.getString("DepName"));
                Seller seller = new Seller();
                seller.setId(rs.getInt("Id"));
                seller.setName(rs.getString("Name"));
                seller.setEmail(rs.getString("Email"));
                seller.setBirthDate(rs.getDate("BirthDate"));
                seller.setSalary(rs.getDouble("BaseSalary"));
                seller.setDepartment(department);

                return seller;
            }

            return null;
        } catch (SQLException e) {
            throw new DbException("Failed to find seller with id: " + id + ". Caused by: " + e.getMessage());
        } finally {
            if (pstm != null) DB.closeStatement(pstm);
            if (rs != null) DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}
