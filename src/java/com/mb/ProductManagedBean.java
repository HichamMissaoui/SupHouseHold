/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mb;

import com.ejb.CommandFacade;
import com.ejb.ProductFacade;
import com.ejb.UserFacade;
import com.entities.Command;
import com.entities.Product;
import com.entities.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import static javax.faces.component.UIInput.isEmpty;
import javax.faces.context.FacesContext;

/**
 *
 * @author Hicham
 */
@Named(value = "productBean")
@SessionScoped
public class ProductManagedBean implements Serializable {

    @EJB
    ProductFacade productEjb;

    @EJB
    UserFacade userEjb;

    @EJB
    CommandFacade cmdEjb;

    private Product selectedProduct;
    private Product selectedCartProduct;
    private List<Product> cartList;
    
    private List<Product> filtredList;

    private Command cmd;

    private String cartLoginErrorMessage;

    /**
     * Creates a new instance of ProductManagedBean
     */
    public ProductManagedBean() {
        selectedProduct = new Product();
        cartList = new ArrayList<Product>();
        cmd = new Command();
    }

    public List<Product> getProductsList() {
        return productEjb.findAll();
    }

    public void addToCart() {
        if (!isEmpty(selectedProduct)) {
            cartList.add(selectedProduct);
        }

    }

    public void removeFromCart() {
        if (!isEmpty(selectedCartProduct)) {
            cartList.remove(selectedCartProduct);
        }
    }

    public double getCartTotalPrice() {
        double total = 0;
        for (Product product : cartList) {
            total += product.getPrice();
        }
        return total;
    }

    public int getUsersNumber() {
        return userEjb.count();
    }

    public String checkout() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getExternalContext().getSessionMap().get("firstname") == null) {
            cartLoginErrorMessage = "You need to login first to checkout !";
            return "cart.xhtml";
        }
        if (cartList.isEmpty()) {
            cartLoginErrorMessage = "You cart is empty !";
            return "cart.xhtml";
        }

        Date date = new Date();

        User user = userEjb.find((int) context.getExternalContext().getSessionMap().get("id"));
        cmd.setPrice(getCartTotalPrice());
        cmd.setUserId(user);
        cmd.setDate(date);
        
        user.getCommandList().add(cmd);

        cmdEjb.create(cmd);
        userEjb.edit(user);
        
        cartList.clear();
        cartLoginErrorMessage = "";
        return "confirmation.xhtml";
    }

    // GETTER ANS SETTERS
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Product getSelectedCartProduct() {
        return selectedCartProduct;
    }

    public void setSelectedCartProduct(Product selectedCartProduct) {
        this.selectedCartProduct = selectedCartProduct;
    }

    public List<Product> getCartList() {
        return cartList;
    }

    public void setCartList(List<Product> cartList) {
        this.cartList = cartList;
    }

    public String getCartLoginErrorMessage() {
        return cartLoginErrorMessage;
    }

    public void setCartLoginErrorMessage(String cartLoginErrorMessage) {
        this.cartLoginErrorMessage = cartLoginErrorMessage;
    }

    public Command getCmd() {
        return cmd;
    }

    public void setCmd(Command cmd) {
        this.cmd = cmd;
    }

    public List<Product> getFiltredList() {
        return filtredList;
    }

    public void setFiltredList(List<Product> filtredList) {
        this.filtredList = filtredList;
    }
    
    

}
