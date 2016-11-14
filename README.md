## Synopsis

IDAPI-wrapper is a way to simplify the use of IDAPI (Information Delivery API) for use with Actuate iServer/iHub.  As well as simplify the API, it also allows you to chain tasks, together so you only authenticate 1 time, and re-use that authenticated session for other calls. This is achieved because everything is derived from the same BaseController which can use another BaseController as part of the constructor.
By chaining the API tasks like this, your code remains compact, and most tasks can now be completed in 2 lines of code.  No need to worry about Authentication tokens and Connection Handles.

## Code Example

Sample Execute a Report

```Java
ReportExecuter reportExecuter = new ReportExecuter("http://localhost:8000", "Administrator", "password", "Default Volume");
String id = reportExecuter.executeReport("/My Report.rptdesign", "/My Output.RPTDOCUMENT");
```

Sample Convert to PDF and download report output (use previously created reportExecuter to chain constructors, maintain AuthId)

```Java
BIRTContentViewer birtContentViewer = new BIRTContentViewer(reportExecuter);
birtContentViewer.viewToFile("/My Output.RPTDOCUMENT", "PDF", "Sample Output.pdf");
```

## Motivation

IDAPI (Information Delivery API) is a very powerful API for the Actuate iServer/iHub platform.  It includes many options and parameters, and as such can be very wordy.

IDAPI-wrapper was created to simplify most common tasks associated with using the API.  It handles some of the nuances such as AuthId and ConnectionHandle parameters.

This project was created and currently maintained by Actuate employees, based on the need to continously do the same IDAPI operations over and over again for different customer implementations.  From that need, and the will to write once use everywhere, IDAPI-wrapper was born.

Since IDAPI calls are often made as part of a software release process or build, we have included ANT tasks components for some of the wrapper calls.  This allows you to now script your entire release process using ANT, which will leverage the appropriate IDAPI operations to push and secure your artifacts onto the server.

IDAPI-wrapper can also be used as a best practices and reference implementation of proper use of IDAPI for your own projects.

## Installation

Include in your Java project.  All libraries (.jar) referenced by project must be in Java classpath

## API Reference

Please refer to Actuate Information Delivery API reference docs

## Tests

Tester class includes many test cases, and reference implementations.

## Contributors

Pierre Tessier - Actuate Corporation - Project Lead  
Rob Murphy - Actuate Corporation - Lead Developer

## License

IDAPI-wrapper is licensed under the Eclipse Public License v1.0
http://www.eclipse.org/legal/epl-v10.html
