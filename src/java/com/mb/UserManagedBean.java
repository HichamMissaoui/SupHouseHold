/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mb;

import com.ejb.UserFacade;
import com.entities.Command;
import com.entities.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

/**
 *
 * @author Hicham
 */
@Named(value = "userBean")
@SessionScoped
public class UserManagedBean implements Serializable {

    @EJB
    UserFacade userEjb;

    private User user;
    private User userProfil;

    private String loginErrorMessage;
    private String passwordErrorMessage;

    private String confirmPassword;
    private String newPassword;
    private String oldPassword;
    private int passwordError;

    /**
     * Creates a new instance of UserManagedBean
     */
    public UserManagedBean() {
        user = new User();
        userProfil = new User();
        passwordError = 0;
    }

    public List<User> getUsersList() {
        return userEjb.findAll();
    }

    public String login() {
        user = userEjb.login(user.getUsername(), user.getPassword());
        if (user.getId() == null) {
            user = new User();
            loginErrorMessage = "Invalid username or password !";
            return "login.xhtml";
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getSessionMap().put("id", user.getId());
            context.getExternalContext().getSessionMap().put("firstname", user.getFirstname());
            context.getExternalContext().getSessionMap().put("lastname", user.getLastname());
            context.getExternalContext().getSessionMap().put("username", user.getUsername());
            context.getExternalContext().getSessionMap().put("email", user.getEmail());
            context.getExternalContext().getSessionMap().put("zip", user.getZip());
            return "index.xhtml";
        }

    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        return "index.xhtml";
    }

    public String register() {
        if (!user.getPassword().equals(confirmPassword)) {
            passwordErrorMessage = "Password and confirmation does not match !";
            return "register.xhtml";
        }
        userEjb.create(user);
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("id", user.getId());
        context.getExternalContext().getSessionMap().put("firstname", user.getFirstname());
        context.getExternalContext().getSessionMap().put("lastname", user.getLastname());
        context.getExternalContext().getSessionMap().put("username", user.getUsername());
        context.getExternalContext().getSessionMap().put("email", user.getEmail());
        context.getExternalContext().getSessionMap().put("zip", user.getZip());
        return "index.xhtml";
    }

    public String updatePersonalInfo() {
        FacesContext context = FacesContext.getCurrentInstance();
        int userId = (int) context.getExternalContext().getSessionMap().get("id");
        User userTemp = userEjb.find(userId);
        userTemp.setFirstname(userProfil.getFirstname());
        userTemp.setLastname(userProfil.getLastname());
        userTemp.setEmail(userProfil.getEmail());
        userTemp.setZip(userProfil.getZip());

        context.getExternalContext().getSessionMap().put("firstname", userTemp.getFirstname());
        context.getExternalContext().getSessionMap().put("lastname", userTemp.getLastname());
        context.getExternalContext().getSessionMap().put("email", userTemp.getEmail());
        context.getExternalContext().getSessionMap().put("zip", userTemp.getZip());

        userProfil = new User();

        userEjb.edit(userTemp);

        return "profile.xhtml";
    }

    public String updateSecurityInfo() {
        FacesContext context = FacesContext.getCurrentInstance();
        int userId = (int) context.getExternalContext().getSessionMap().get("id");
        User user = userEjb.find(userId);
        if (!user.getPassword().equals(oldPassword)) {
            passwordError = 2;
        } else if (!newPassword.equals(confirmPassword)) {
            passwordError = 3;
        } else {
            passwordError = 1;
            user.setPassword(newPassword);
            userEjb.edit(user);
        }
        oldPassword = "";
        confirmPassword = "";
        newPassword = "";      

        return "profile.xhtml";
    }

    public List<Command> getUserCommandList() {
        FacesContext context = FacesContext.getCurrentInstance();
        int userId = (int) context.getExternalContext().getSessionMap().get("id");
        user = userEjb.find(userId);
        user.getCommandList().size();
        return user.getCommandList();
    }

    // GETTERS AND SETTERS
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLoginErrorMessage() {
        return loginErrorMessage;
    }

    public void setLoginErrorMessage(String loginErrorMessage) {
        this.loginErrorMessage = loginErrorMessage;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPasswordErrorMessage() {
        return passwordErrorMessage;
    }

    public void setPasswordErrorMessage(String passwordErrorMessage) {
        this.passwordErrorMessage = passwordErrorMessage;
    }

    public User getUserProfil() {
        return userProfil;
    }

    public void setUserProfil(User userProfil) {
        this.userProfil = userProfil;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public int getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(int passwordError) {
        this.passwordError = passwordError;
    }

}
