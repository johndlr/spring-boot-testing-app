package com.juandlr.spring_boot_testing.controller;

import com.juandlr.spring_boot_testing.entity.Employee;
import com.juandlr.spring_boot_testing.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId){
        return employeeService.getEmployeeById(employeeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Employee employee){
        return employeeService.getEmployeeById(employeeId)
                .map(savedEmployee -> {
                    savedEmployee.setFirstName(employee.getFirstName());
                    savedEmployee.setLastName(employee.getLastName());
                    savedEmployee.setEmail(employee.getEmail());
                    Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
                    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId){
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body("Employee deleted successfully");
    }

}
