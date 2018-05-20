# Sample Vaadin 10 full stack jee application

Goal of this sample is to showcase Vaadin 10 in a jee7 web application 
with security and persistence. The glue is CDI, and flow-cdi-addon.

'full stack' in this sample means:

- Vaadin 10 
- CDI (fow-cdi-addon)
- Apache Shiro for security ( instead of jaas )
- EJB
- JPA ( and Deltaspike Data module )

Since this sample is about integrating technologies, design and UX are ignored.
Though it should not be hard for a skilled frontend developer - someone not like me -, 
because polymer templates, and web components are used. 

## Running it

Login with root/root. 

And currently this is the only function. More coming soon. 

Database content to be flushed on startup!

### From CLI

Run with maven:
`mvn clean package tomee:run`

### Deploy war
You can also deploy the application. 

Manually, or by an IDE, after setting up    
- a JTA datasource with name 'jdbc/v10jeeapp'
- a Non JTA datasource with name 'jdbc/v10jeeappNonJta'

in your jee7 container.

   