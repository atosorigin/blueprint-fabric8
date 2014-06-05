secure-soap: demonstrates a secure SOAP web service with Apache CXF
==========================

## What is it?

This quick start demonstrates how to create a secure SOAP Web service with Apache CXF and expose it through the OSGi HTTP Service.


## System requirements

Before building and running this quick start you need:

* Maven 3.0.4 or higher
* JDK 1.7
* Fabric8


## How to run this example

To build the quick start:

You can deploy and run this example at the console command line, as follows:

1. It is assumed that you have already created a fabric and are logged into a container called `root`.
1. Create a new child container and deploy the `example-quickstarts-secure.soap` profile in a single step, by entering the
 following command at the console:

        fabric:container-create-child --profile example-quickstarts-secure.soap root mychild

1. Wait for the new child container, `mychild`, to start up. Use the `fabric:container-list` command to check the status of the `mychild` container and wait until the `[provision status]` is shown as `success`.
1. Log into the `mychild` container using the `fabric:container-connect` command, as follows:

        fabric:container-connect mychild

1. View the container log using the `log:tail` command as follows:

        log:tail



### How to try this example

Login to the web console and click the APIs button on the Runtime plugin

    http://localhost:8181/hawtio/index.html#/fabric/api

This shows the SOAP services in the fabric.

You can see details of the SOAP service by clicking the WSDL under the APIs column. 

The WSDL for the SOAP service is the `Location` url and append `?wsdl`


### To run a Web client:

You can use an external tool such as SoapUI to test web services.

When using SoapUI with WS Security, then configure the request properties as follows:

* Username = admin
* Password = admin
* Authentication Type = Global HTTP Settings
* WSS-Password Type = PasswordText


## Undeploy this example

To stop and undeploy the example in fabric8:

1. Disconnect from the child container by typing Ctrl-D at the console prompt.
2. Stop and delete the child container by entering the following command at the console:

        fabric:container-stop mychild
        fabric:container-delete mychild