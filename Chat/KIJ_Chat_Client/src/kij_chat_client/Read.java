/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/*import java.net.Socket;*/
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Base64;

/**
 *
 * @author santen-suru
 */
public class Read implements Runnable {
        
        private Scanner in;//MAKE SOCKET INSTANCE VARIABLE
        String input;
        boolean keepGoing = true;
        ArrayList<String> log;
	
	public Read(Scanner in, ArrayList<String> log)
	{
		this.in = in;
                this.log = log;
	}
    
        @Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
				if(this.in.hasNext()) {
                                                                   //IF THE SERVER SENT US SOMETHING
                                        input = this.in.nextLine();
                                        if (input.split(" ")[0].toLowerCase().equals("success")) {
                                            if (input.split(" ")[1].toLowerCase().equals("logout")) {
                                                keepGoing = false;
                                                System.out.println(input);//PRINT IT OUT
                                            } else if (input.split(" ")[1].toLowerCase().equals("login")) {
                                                EncryptionUtil encrypttext = new EncryptionUtil(input.split(" ")[2]);
                                                if(!encrypttext.areKeysPresent())
                                                    encrypttext.generateKey();
                                                log.clear();
                                                log.add("true");
                                                System.out.println(input);//PRINT IT OUT
                                            }
                                        }
                                        else if(input.split(" ")[0].toLowerCase().equals("pm"))
                                        {
                                            String source = input.split(" ")[1];
                                            String dest = input.split(" ")[2];
                                            String message = input.split(" ")[3];
                                            byte[] b = Base64.getDecoder().decode(message);
                                            EncryptionUtil decryptText = new EncryptionUtil(dest);
                                            String decryptedText = decryptText.decrypt(b, 0);
                                            System.out.println(source + ": " + decryptedText);
                                        }
                                        else if(input.split(" ")[0].toLowerCase().equals("bm"))
                                        {
                                            String source = input.split(" ")[1];
                                            String message = input.split(" ")[2];
                                            byte[] b = Base64.getDecoder().decode(message);
                                            EncryptionUtil decryptText = new EncryptionUtil(source);
                                            String decryptedText = decryptText.decrypt(b, 1);
                                            System.out.println(source + " <BROADCAST>: " + decryptedText);
                                        }
                                        else if(input.split(" ")[0].toLowerCase().equals("gm"))
                                        {
                                            String source = input.split(" ")[1];
                                            String groupName = input.split(" ")[2];
                                            String message = input.split(" ")[3];
                                            byte[] b = Base64.getDecoder().decode(message);
                                            EncryptionUtil decryptText = new EncryptionUtil(source);
                                            String decryptedText = decryptText.decrypt(b, 1);
                                            System.out.println(source + " @ " + groupName + " group: " + decryptedText);
                                        }
                                        else
                                        {
                                            System.out.println(input);
                                        }
                                        
                                }
                                
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}
}
