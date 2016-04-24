/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.util.ArrayList;

/**
 *
 * @author santen-suru
 */
public class User {
    // User-Password list
    private ArrayList<Pair<String,String>> _userlist = new ArrayList<>();
    
    User() {
        _userlist.add(new Pair("Admin", "Admin".hashCode()));
        _userlist.add(new Pair("Andi", "Andi".hashCode()));
        _userlist.add(new Pair("Budi", "Budi".hashCode()));
        _userlist.add(new Pair("Rudi", "Rudi".hashCode()));
        _userlist.add(new Pair("Luci", "Luci".hashCode()));
    }
    
    public ArrayList<Pair<String,String>> getUserList() {
        return _userlist;
    }
}
