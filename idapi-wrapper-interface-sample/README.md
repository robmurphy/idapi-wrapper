## Synopsis

This is a project fork of Pierre's IDAPI Wrapper.  Everything except for 'idapi-wrapper-interface-sample' has been untouched.  This is a project that shows how to use the IDAPI Wrapper with iHub 3.1.x and extends Pierre's IDAPI wrapper example.

## Code Example

Sample Execute a Report

```Java
        EngineProperties myProps = new EngineProperties();
        IdapiUtil ihub = new IdapiUtilImpl();

        ihub.listAllFilesAndPermissions(myProps.getHost(),
                                        myProps.getUsername(),
                                        myProps.getPassword(),
                                        myProps.getVolume(),
                                        "/");
```

This has not been fully tested yet, just a handful of methods have been tested and know to be working.  In theory everything should be working fine, I will update this message after testing has been completed.

## API Reference

Please refer to Actuate Information Delivery API reference docs

## License

IDAPI-wrapper is licensed under the Eclipse Public License v1.0
http://www.eclipse.org/legal/epl-v10.html
