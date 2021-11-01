/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productentityseapplication;

import java.math.BigDecimal;
import java.util.List;
import ws.client.InputDataValidationException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.ProductEntity;
import ws.client.ProductEntityWebService_Service;
import ws.client.ProductNotFoundException_Exception;
import ws.client.ProductSkuCodeExistException_Exception;
import ws.client.UnknownPersistenceException_Exception;

/**
 *
 * @author irene
 */
public class ProductEntitySeApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("RetrieveAllProducts:");
        try {
            List<ProductEntity> listOfProducts = retrieveAllProducts("manager", "password");
            listOfProducts
                    .stream()
                    .forEach(System.out::println);
            System.out.println("Successfully retrieved!");
        } catch (InvalidLoginCredentialException_Exception ex) {
            System.out.println("Invalid login credentials!");
        }
        System.out.println("retrieveProductByProductId");
        try{
            ProductEntity product = retrieveProductByProductId("manager", "password",1L);
            System.out.println(product);
            System.out.println("Successfully retrieved!");
        } catch(InvalidLoginCredentialException_Exception |ProductNotFoundException_Exception ex ){
            System.out.println("error occured here.");
        }
        
        System.out.println("Creating new product");
        try{
            ProductEntity newProduct = createNewProduct("manager", "password", "PROD010", "Book",
                    "textbook", 100, 10, new BigDecimal(1000), "education");
            System.out.println("Successfully created new product with product id " + newProduct.getProductId());
        } catch(ProductSkuCodeExistException_Exception|
            UnknownPersistenceException_Exception| InputDataValidationException_Exception| InvalidLoginCredentialException_Exception ex){
            System.out.println("error occured here");
        }
    }

    private static java.util.List<ws.client.ProductEntity> retrieveAllProducts(java.lang.String username,
            java.lang.String password) throws InvalidLoginCredentialException_Exception {
        ws.client.ProductEntityWebService_Service service = new ProductEntityWebService_Service();
        ws.client.ProductEntityWebService port = service.getProductEntityWebServicePort();
        return port.retrieveAllProducts(username, password);
    }

    private static ProductEntity retrieveProductByProductId(java.lang.String username,
            java.lang.String password, java.lang.Long productId) throws InvalidLoginCredentialException_Exception,
            ProductNotFoundException_Exception {
        ws.client.ProductEntityWebService_Service service = new ProductEntityWebService_Service();
        ws.client.ProductEntityWebService port = service.getProductEntityWebServicePort();
        return port.retrieveProductByProductId(username, password, productId);
    }

    private static ProductEntity createNewProduct(java.lang.String username,
            java.lang.String password, java.lang.String skuCode, java.lang.String name,
            java.lang.String description,
            java.lang.Integer quantityOnHand, java.lang.Integer reorderQuantity, java.math.BigDecimal unitPrice,
            java.lang.String category) throws ProductSkuCodeExistException_Exception,
            UnknownPersistenceException_Exception, InputDataValidationException_Exception, InvalidLoginCredentialException_Exception {
        ws.client.ProductEntityWebService_Service service = new ProductEntityWebService_Service();
        ws.client.ProductEntityWebService port = service.getProductEntityWebServicePort();
        return port.createNewProduct(username, password, skuCode, name, description, quantityOnHand, reorderQuantity, unitPrice, category);
    }

}
