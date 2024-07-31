package com.cg.fms.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.fms.dao.ContractDao;
import com.cg.fms.dao.CustomerDao;
import com.cg.fms.entity.Contract;
import com.cg.fms.entity.Customer;
import com.cg.fms.exception.AdminException;
import com.cg.fms.exception.ContractException;
import com.cg.fms.exception.CustomerException;
import com.cg.fms.model.ContractModel;
@Service
public class ContractServiceImpl implements IContractService{
	
	@Autowired
	private ContractDao contractRepo;
	
	@Autowired
	private CustomerDao customerRepo;
	
	@Autowired
	private EMParser parser;
	
	public ContractServiceImpl() {
		/* default constructor */
	}
	
	public ContractServiceImpl(ContractDao contractRepo) {
		super();
		this.contractRepo = contractRepo;
		this.parser =new EMParser();
	}
	
	

	public ContractDao getContractRepo() {
		return contractRepo;
	}



	public void setContractRepo(ContractDao contractRepo) {
		this.contractRepo = contractRepo;
	}



	public EMParser getParser() {
		return parser;
	}



	public void setParser(EMParser parser) {
		this.parser = parser;
	}
	
	/* get the details of contract by contractnumber */
	@Override
	public ContractModel getContarctByContractNumber(String contractNumber) throws ContractException {
		if (!contractRepo.existsById(contractNumber))
			throw new ContractException("No Contract found for the given Id");
		return parser.parse(contractRepo.findById(contractNumber).get());
	}
	
	
	
	
	/* add the details of contract  */
	@Override
	public ContractModel addContract(ContractModel contract) throws ContractException{
		if(contract==null) {
			throw new ContractException("contract should not be null");
		}
		else if ( contract!= null) {
			if (contractRepo.existsById(contract.getContractNumber())) {
				throw new ContractException("Contract with this id already exists");
			}

			contract = parser.parse(contractRepo.save(parser.parse(contract)));
		}

		return contract;
	}
	
	@Override
	public ContractModel addByCustomer(ContractModel contract, String customerId) throws ContractException, CustomerException {
		if(customerId==null) {
			throw new CustomerException("Customer Id can not be null");
		}
		Customer customer=customerRepo.findById(customerId).orElse(null);
		if(customer==null) {
			throw new CustomerException("Customer does not exists");
		}
		Set<ContractModel> accounts=customer.getContracts().stream().map(parser::parse).collect(Collectors.toSet());
		if(accounts.contains(contract)) {
			throw new ContractException(contract.getContractNumber()+" is already Exists");
		}else {
			contract=parser.parse(contractRepo.save(parser.parse(contract)));
			customer.getContracts().add(parser.parse(contract));
			customer.setContracts(customer.getContracts());
			customerRepo.save(customer);
			
		}
		return contract;
	}

//	@Override
//	public Set<ContractModel> findAllByCustomerId(String customerId) throws CustomerException {
//		Customer customer=customerRepo.findById(customerId).orElse(null);
//		if(customerId==null) {
//			throw new CustomerException("Customer Id should not be null");
//		}else if(customer==null) {
//			throw new CustomerException("No Customer Exists");
//		}else if(customer.getContracts().isEmpty()) {
//			throw new CustomerException("No Accounts Exists");
//		}
//		return customer.getContracts().stream().map(parser::parse).collect(Collectors.toSet());
//	}
	/* update  details of contract by contractnumber */
	@Override
	public ContractModel updateContract(ContractModel contractModel) throws ContractException {
		if (contractModel != null) {
			if (contractRepo.existsById(contractModel.getContractNumber())) {
				contractModel = parser.parse(contractRepo.save(parser.parse(contractModel)));
			}
			else {
				throw new ContractException("Contract with this id doesnot  exists");
			}
			
		}
		return contractModel;
	}
	
	/* delete the details of contract by contractnumber */
	@Override
	public void deleteContract(String contractNumber) {
		if(contractNumber==null) {
			throw new ContractException("Contract number should not be null");
		}else if (!contractRepo.existsById(contractNumber)) {
			throw new ContractException("Contract Number"+contractNumber+" does not exists");
		}else {
			contractRepo.deleteById(contractNumber);
		}
	}
	
	/* get all the details of contract  */
	@Override
	public List<ContractModel> getAllContracts() {
		return contractRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}
	
	@Override
	public ContractModel findById(String contractNumber) throws ContractException {
		if(contractNumber==null) {
			throw new ContractException("Contract number should not be null");
		}else if (!contractRepo.existsById(contractNumber)) {
			throw new ContractException("Contract Number"+contractNumber+" does not exists");
		}else {
			return parser.parse(contractRepo.findById(contractNumber).orElse(null));
		}
	}
	
	@Override
	public boolean existsById(String contractNumber) throws ContractException {
		if(contractNumber==null) {
			throw new ContractException("Card Number should not be null");
		}
		return contractRepo.existsById(contractNumber);
	}

	@Override
	public List<ContractModel> findAllByCustomerId(String customerId){
		Optional<Customer> customerOptional = customerRepo.findById(customerId);	
		List<Contract> contracts = customerOptional.get().getContracts();	
		return contracts.stream().map(parser::parse).collect(Collectors.toList());
	}
	


//	



}
