package io.zipcoder.services;

import io.zipcoder.persistenceapp.entities.Employee;
import io.zipcoder.persistenceapp.repositories.EmployeeRepository;
import io.zipcoder.persistenceapp.services.EmployeeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getAllEmployees() {
        when(employeeService.getAllEmployees()).thenReturn(stubEmployees());
        employeeService.getAllEmployees();
        verify(employeeRepository, times(1)).findAll();
    }

    private List<Employee> stubEmployees() {
        Employee e1 = new Employee("Kesha", "Johns", "Poobah", "12345678", "jim@aol.com");
        Employee e2 = new Employee("Kathleen", "Johns2", "Grunt", "87654321", "joe@aol.com");
        e1.setId(3);
        e2.setId(5);
        return Arrays.asList(e1,e2);
    }

    @Test
    public void getEmployee() {
        when(employeeService.getEmployee(5)).thenReturn(stubEmployees().get(1));
        employeeService.getEmployee(5);
        verify(employeeRepository, times(1)).findOne(5);
    }

    @Test
    public void createEmployee() {
        Employee expected = new Employee("Kesha", "Beale", "Writer", "2153002744", "bouncyslim@aol.com");
        doReturn(expected).when(employeeRepository).save(any(Employee.class));
        Employee actual = employeeService.createEmployee(expected);
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void createEmployeeNoReturn() {
        Employee expected = new Employee("Kesha", "Beale", "Writer", "2153002744", "bouncyslim@aol.com");
        doReturn(expected).when(employeeRepository).save(any(Employee.class));
        employeeService.createEmployee(expected);
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).save(captor.capture());
        Assert.assertEquals(expected.toString(), captor.getValue().toString());
    }

    @Test
    public void setManager() {
        Employee e = new Employee("Kesha", "Beale", "Analyst", "2153002744", "bouncyslim@aol.com");
        Employee m = new Employee("Kathleen", "Beale", "Admin", "2158330303", "bealek@hotmail.com");
        e.setId(3);
        m.setId(7);
        doReturn(e).when(employeeRepository).findOne(3);
        doReturn(m).when(employeeRepository).findOne(7);
        employeeService.setManager(3,7);
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).save(captor.capture());
        Assert.assertEquals(m, captor.getValue().getManager());
    }

    @Test
    public void setManagerNullE() {
        Employee e = new Employee("Kesha", "Beale", "Analyst", "2153002744", "bouncyslim@aol.com");
        Employee m = new Employee("Kathleen", "Beale", "Admin", "2158330303", "bealek@hotmail.com");
        e.setId(4);
        m.setId(7);
        doReturn(e).when(employeeRepository).findOne(4);
        doReturn(m).when(employeeRepository).findOne(7);
        employeeService.setManager(3,7);
        verify(employeeRepository, times(0)).save(e);
        Assert.assertNull(employeeService.setManager(3,7));
    }

    @Test
    public void updateEmployee() {
        Employee e = new Employee("Kesha", "Beale", "Analyst", "2153002744", "bouncyslim@aol.com");
        employeeService.updateEmployee(e);
        verify(employeeRepository, times(1)).save(e);
    }

    @Test
    public void getEmployeesManagedBy () {
        when(employeeService.getEmployeesManagedBy(7)).thenReturn(stubEmployees());
        employeeService.getEmployeesManagedBy(7);
        verify(employeeRepository, times(1)).findEmployeeByManagerId(7);
    }

    @Test
    public void getEmployeesOfDepartment () {
        when(employeeService.getEmployeesOfDepartment(7)).thenReturn(stubEmployees());
        employeeService.getEmployeesOfDepartment(7);
        verify(employeeRepository, times(1)).findEmployeeByDepartmentNum(7);
    }

    @Test
    public void getEmployeesNoManager () {
        when(employeeService.getEmployeesNoManager()).thenReturn(stubEmployees());
        employeeService.getEmployeesNoManager();
        verify(employeeRepository, times(1)).findEmployeeByManagerIsNull();
    }




}
