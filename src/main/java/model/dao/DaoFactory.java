package model.dao;

import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;
import model.dao.interfaces.DepartmentDao;
import model.dao.interfaces.SellerDao;

public class DaoFactory {
    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC();
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC();
    }
}
