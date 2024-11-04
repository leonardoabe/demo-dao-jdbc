package model.dao.interfaces;

import model.entities.Seller;

import java.util.List;

public interface SellerDao {
    public void insert(Seller seller);

    public void update(Seller seller);

    public void deleteById(Integer id);

    public Seller findById(Integer id);

    public List<Seller> findAll();

    public List<Seller> findByDepartmentId(Integer id);
}
