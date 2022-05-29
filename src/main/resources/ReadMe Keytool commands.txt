#Create self signed certificate for server:
#We have different ways to do this but in this demo we will use “keytool” to generate a certificate:

keytool -genkey -alias MyServer -keyalg RSA -validity 1825 -keystore "MyServer.jks" -storetype JKS -dname "CN=myserver.com,OU=NKS org,O=Nks,L=Bangalore,ST=Kar,C=Ind" -keypass password -storepass password

#Create public certificate file from server app certi:
#Now we will create public certi(.crt) from server jks file.
keytool -exportcert -alias MyServer -keystore MyServer.jks -file MyServer.cer

#Create self signed certificate for client:
keytool -genkey -alias MyClient  -keyalg RSA -validity 1825 -keystore "MyClient.jks" -storetype JKS -dname "CN=myclient.com,OU=NKS org,O=Nks,L=Bangalore,ST=Kar,C=Ind" -keypass password -storepass password

#Create public certificate file from client app certi:
#Now we will create public certi(.crt) from client jks file.
keytool -exportcert -alias MyClient -keystore MyClient.jks -file MyClient.cer

#We created public certificates and keystores for both client and server.

#Now we will establish trust between them by importing client certificate into server’s keystore and vice versa.

keytool -importcert -alias MyServer -keystore MyClient.jks -file MyServer.cer
 
 
keytool -importcert -alias MyClient -keystore MyServer.jks -file MyClient.cer

# start server and client application on different port if running locally to test. It should work.
# for not try calling server api through client application. 
# THe server hello api is only accessible through the client application as server certificate trust only the client certificate.
 #Any application who wants to access hello api get the public certificate of server and give its public certificate to server to import them in the jks file.
 
keytool -v -keystore MyServer.jks -list

keytool -keypasswd -keystore MyServer.jks -alias MyServer
Enter keystore password:  (enter OLDPASSWD)
New key password for <myserveralias>: (enter NEWPASSWD)
Re-enter new key password for <myserveralias>: (enter NEWPASSWD)


keytool -storepasswd -keystore MyServer.jks
Enter keystore password:   (enter OLDPASSWD)
New keystore password: (enter NEWPASSWD)
Re-enter new keystore password: (enter NEWPASSWD)

--convert jks file to pkcs12 file because browser understand pkcs12 format
keytool -importkeystore -srckeystore MyClient.jks -srcstoretype JKS -deststoretype PKCS12 -destkeystore MyClient.p12
keytool -importkeystore -srckeystore MyClient.jks -destkeystore MyClient.p12 -srcstoretype JKS -deststoretype PKCS12 -srcstorepass client-service -deststorepass nt-service -srcalias MyClient -destalias MyClient -srckeypass client-service -destkeypass client-service -noprompt
keytool -importkeystore -srckeystore MyClient.jks -destkeystore MyClient.p12 -srcstoretype JKS -deststoretype PKCS12 -deststorepass password

keytool \
  -importkeystore \
  -srckeystore MyClient.jks \
  -destkeystore MyClient.p12 \
  -srcstoretype JKS \
  -deststoretype PKCS12 \
  -srcstorepass mysecret \
  -deststorepass mysecret \
  -srcalias myalias \
  -destalias myalias \
  -srckeypass mykeypass \
  -destkeypass mykeypass \
  -noprompt

window process find command
netstat -ano | find "8080"

                     
taskkill /F /PID 15984

process to import 12 cert to browser
https://www.ibm.com/docs/en/elm/6.0.2?topic=dashboards-importing-certificates-configuring-browsers-report-builder-reports

Start Chrome.
From the control menu, which is near the right of the address bar, click Settings > Show advanced settings.
In the HTTPS/SSL section, click Manage Certificates.
On the Trusted Root Certification Authorities tab, click Import > Next.
Click Browse and select the .p12 file for the Report Builder; then, click Open > Next.
Enter the password that you received with the .p12 file and click Next.
Select Place all certificates in the following store.
Click Browse and select Trusted Root Certificate Authorities.
Click OK > Next > Finish; then click OK to close the other windows in the Certificate Import Wizard.


i