package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 完成md5加密
        String encodePassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encodePassword.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();

        // 属性拷贝（需要属性同名）
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置账号状态（其实数据库已经有默认值了）
        employee.setStatus(StatusConstant.ENABLE);

        // 设置默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        // 保存到数据库
        employeeMapper.insert(employee);
    }

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 开始分页查询(基于拦截器动态拼接sql语句的limit部分)
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> employees = employeeMapper.pageQuery(employeePageQueryDTO);
        long total = employees.getTotal();
        List<Employee> records = employees.getResult();

        return new PageResult(total, records);
    }

    /**
     * 启用或停用员工账号
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // 为了提升update的复用性，我们可以将status和id封装成employee对象
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();

        // todo 防御性编程：参数校验：status只能是0或1；用户id必须存在
        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {

        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {

        // todo 防御性编程：修改的用户必须存在且修改的内容是合法的
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeMapper.update(employee);
    }

    /**
     * 修改员工密码
     * @param passwordEditDTO
     */
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        // todo 参数校验
//        Employee employee = employeeMapper.getById(passwordEditDTO.getEmpId());
        Employee employee = employeeMapper.getById(BaseContext.getCurrentId());
        if(employee == null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if(!Objects.equals(DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()), employee.getPassword())){
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
        Employee newEmployee = new Employee();
        newEmployee.setId(employee.getId());
        newEmployee.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));

        employeeMapper.update(newEmployee);
    }


}
