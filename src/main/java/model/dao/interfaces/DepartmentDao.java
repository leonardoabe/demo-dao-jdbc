package model.dao.interfaces;

import model.entities.Department;

import java.util.List;

public interface DepartmentDao {
    public void insert(Department department);

    public void update(Department department);

    public void deleteById(Integer id);

    public Department findById(Integer id);

    public List<Department> findAll();
}
