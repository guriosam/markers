package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class BalenParenteses {
	  public static boolean verificarParen(String s) {
	    char c[] = s.toCharArray();
	    int count = 0;
	    for(int i = 0; i < c.length; i++){
	    	if(c[i] == '{'){
	    		count++;
	    	} else if(c[i] == '}'){
	    		count --;
	    	}
	    	
	    	if(count < 0){
	    		System.out.println("FALSE");
	    		return false;
	    	}
	    	
	    }
	    
	    if(count > 0){
	    	System.out.println("FALSE");
	    	return true;
	    }
	    
	    System.out.println("TRUE");
	    return true;
	    
	  }

	}