package com.mycompany.smart_home;

import com.mycompany.smart_home.ui.LoginFrame; 

public class Smart_home {

    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(() -> {

            LoginFrame login = new LoginFrame();
            
            login.setLocationRelativeTo(null);
            
            login.setVisible(true);
        });
        
    }
}