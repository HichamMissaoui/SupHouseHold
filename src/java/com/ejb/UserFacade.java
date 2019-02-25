/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb;

import com.entities.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Hicham
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "SuphousePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User login(String username, String password) {
        User user = new User();
        List results = em.createNamedQuery("User.login")
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();

        if (!results.isEmpty()) {
            user = (User) results.get(0);
        }
        return user;
    }
    
}
