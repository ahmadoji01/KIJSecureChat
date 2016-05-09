/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;


import sun.security.x509.*;
import java.security.cert.*;
import java.security.*;
import java.math.BigInteger;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 *
 * @author michaelknight123
 */
public class DigitalCert 
{
    
    public void SignCertificate(String dn, int days, String algorithm)
    throws GeneralSecurityException, IOException
    {
        try 
        {
            String username = dn;
            String PRIVATE_KEY_FILE = "C:/keys/serverPrivate.key";
            String PUBLIC_KEY_FILE = "C:/keys/server/public" + dn + ".key";
            ObjectInputStream inputStream = null;
            
            inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
            PrivateKey privateKey = (PrivateKey) inputStream.readObject();

            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            PublicKey publicKey = (PublicKey) inputStream.readObject();
            
            PrivateKey privkey = privateKey;
            X509CertInfo info = new X509CertInfo();
            Date from = new Date();
            Date to = new Date(from.getTime() + days * 86400000l);
            CertificateValidity interval = new CertificateValidity(from, to);
            BigInteger sn = new BigInteger(64, new SecureRandom());
            dn = "CN=" + dn + ", OU=Testing, O=TestCompany";
            X500Name owner = new X500Name(dn);
            X500Name issuer = new X500Name("CN=ThisCACompany, OU=Testing, O=TestCACompany, C=ID");
            
            info.set(X509CertInfo.VALIDITY, interval);
            info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
            info.set(X509CertInfo.SUBJECT, owner);
            info.set(X509CertInfo.ISSUER, issuer);
            info.set(X509CertInfo.KEY, new CertificateX509Key(publicKey));
            info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
            AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
            info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

            // Sign the cert to identify the algorithm that's used.
            X509CertImpl cert = new X509CertImpl(info);
            cert.sign(privkey, algorithm);

            // Update the algorith, and resign.
            algo = (AlgorithmId)cert.get(X509CertImpl.SIG_ALG);
            info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
            cert = new X509CertImpl(info);
            cert.sign(privkey, algorithm);
            
            File file = new File("C:/keys/certificates/certificate" + username + ".pem");
            if (file.getParentFile() != null) 
            {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            byte[] buf = cert.getEncoded();
            
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(file));
            publicKeyOS.writeObject(buf);
            publicKeyOS.close();
            
            /*
            FileOutputStream os = new FileOutputStream(file);
            os.write(buf);
            os.close();

            Writer wr = new OutputStreamWriter(os, Charset.forName("UTF-8"));
            wr.write(new sun.misc.BASE64Encoder().encode(buf));
            wr.flush();
            */

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
    }
    
}
