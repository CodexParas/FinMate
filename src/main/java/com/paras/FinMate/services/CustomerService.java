package com.paras.FinMate.services;

import com.paras.FinMate.DTO.CreateCustomerRequest;
import com.paras.FinMate.common.Response;
import com.paras.FinMate.entities.Customer;
import com.paras.FinMate.repositories.CustomerRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepository;

    @SneakyThrows
    public Response createCustomer (CreateCustomerRequest createCustomerRequest) {
        Customer customer = Customer.builder()
                                    .email(createCustomerRequest.getEmail())
                                    .firstName(createCustomerRequest.getFirstName())
                                    .lastName(createCustomerRequest.getLastName())
                                    .phoneNumber(createCustomerRequest.getPhoneNumber())
                                    .build();
        customerRepository.save(customer);
        return Response.success("Customer created successfully", null);
    }
}
