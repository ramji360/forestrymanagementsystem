package com.cg.fms.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.fms.exception.ContractException;
import com.cg.fms.exception.OrderException;
import com.cg.fms.exception.ProductException;
import com.cg.fms.model.ContractModel;
import com.cg.fms.model.OrderModel;
import com.cg.fms.model.ProductModel;
import com.cg.fms.service.IOrderService;
@CrossOrigin("*")
@RestController
@RequestMapping(path="/orders")
public class OrderAPI {
	@Autowired
	private IOrderService orderService;
	
	/* Adding the Order details into the database */
	@PostMapping("/addorder")
	public ResponseEntity<OrderModel> createOrder(@RequestBody OrderModel order) throws OrderException {
		order = orderService.addOrder(order);
		return new ResponseEntity<>(order, HttpStatus.CREATED);
	}
	
	/* Update the Order details */
	@PutMapping("/updateorder/{orderNumber}")
	public ResponseEntity<OrderModel> updateOrder(@RequestBody OrderModel orderModel) throws OrderException {
		orderModel = orderService.updateOrder(orderModel);
		return new ResponseEntity<>(orderModel, HttpStatus.OK);
	}
	
	
	/* Display all the Orders details present in the database */
	@GetMapping("/getallorders")
	public ResponseEntity<List<OrderModel>> getAll() throws OrderException{
		return ResponseEntity.ok(orderService.getAllOrders());
	}
	
//	@GetMapping("/getOrderByCustomerId/{customerId}")
//	public ResponseEntity<List<OrderModel>> getOrderByCustomerId(@PathVariable("customerId") String customerId) throws OrderException{
//		return ResponseEntity.ok(orderService.getOrderByCustomerId(customerId));
//	}
	
	/* Display the particular Order details present in the database using orderNumber*/
	@GetMapping("/getOrder/{orderNumber}")
	public ResponseEntity<OrderModel> getOrder(@PathVariable("orderNumber") String orderNumber) throws OrderException{
		return ResponseEntity.ok(orderService.getOrder(orderNumber));
	}
	
	/* Delete the particular Order details present in the database using orderNumber*/
	@DeleteMapping("/deleteorder/{orderNumber}")
	public ResponseEntity<String> deleteOrder(@PathVariable("orderNumber") String orderNumber) throws OrderException {
		ResponseEntity<String> response = null;
		OrderModel order = orderService.getOrder(orderNumber);
		if (order == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			orderService.deleteOrder(orderNumber);
			response = new ResponseEntity<>("Order deleted successfully",HttpStatus.OK);
		}
		return response;
	}

	@PostMapping("/addproduct/{orderNumber}")
	public ResponseEntity<String> addProduct(@RequestBody ProductModel product,@PathVariable("orderNumber") String orderNumber) throws ProductException, OrderException{
		ResponseEntity<String> response=null;
		if(orderService.addProduct(product, orderNumber)) {
			response = new ResponseEntity<>("Product is Added",HttpStatus.CREATED);
		}else {
			response= new ResponseEntity<>("Product is not Added",HttpStatus.NOT_ACCEPTABLE);
		}
		return response;
	}
	
	@GetMapping("/getorderbycustomerid/{customerId}")
	public ResponseEntity<List<OrderModel>> findAllByCustomerId(@PathVariable(name = "customerId") String customerId)throws ContractException {
		ResponseEntity<OrderModel> responses = null;
		List<OrderModel> orders = orderService.findAllByCustomerId(customerId);

		return new ResponseEntity<>(orderService.findAllByCustomerId(customerId), HttpStatus.OK); 
		
	}

}