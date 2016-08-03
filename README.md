# Okta OAuth2 Java Resource Server Demo Project
This project is intended to be used with Okta w/ AppAuth OAuth2 [iOS](https://github.com/oktadeveloper/okta-openidconnect-appauth-sample-swift) and [Android](https://github.com/oktadeveloper/okta-openidconnect-appauth-sample-android) samples.

## Running the Sample with your Okta Organization

###Pre-requisites
This sample application was tested with an Okta org. If you do not have an Okta org, you can easily [sign up for a free Developer Okta org](https://www.okta.com/developer/signup/).

To test out the [custom claims/scopes](http://openid.net/specs/openid-connect-core-1_0.html#AdditionalClaims) ability with the returned `accessToken`, additionally configure the following:

1. Select the configured **OpenID Connect Application**
2. In the **Authorization Server** screen, click the **OAuth 2.0 Access Token** *Edit* button
3. Add the custom scope `gravatar`.
4. Add the custom claim *name* `user_email` and *value* `appuser.email`
5. Add the **gravatar** scope to your defined scopes:

###[Android](https://github.com/oktadeveloper/okta-openidconnect-appauth-sample-android)
```java
// OktaAppAuth.java

public static final String SCOPE = "openid profile email address phone groups offline_access gravatar";
```
###[iOS](https://github.com/oktadeveloper/okta-openidconnect-appauth-sample-swift)
```swift
// OktaAppAuth.swift

let request = OIDAuthorizationRequest(configuration: config!,
  clientId: self.appConfig.kClientID,
  scopes: [
      ...
      "gravatar",
  ],
```

## Setup
This document assumes you have `maven` and the [Spring Boot Maven Plugin](http://docs.spring.io/spring-boot/docs/1.4.0.RELEASE/maven-plugin/) installed.
Configure the **okta.issuer** and **okta.audience** in the `resources/application.properties` file:
```java
  // application.properties
  
  	okta.issuer:        https://example.oktapreview.com
	okta.audience:      79arVRKBcBEYMuMOXrYF
	okta.securedRoute:  /protected
	okta.accessScope:   gravatar
```

**Next**, add the file `/oauth2-okta-resource-server-0.0.1.jar` to your list of approved JARs.

For example: To add it to an [IntelliJ IDEA project](https://www.jetbrains.com/idea/) simply:

`Project Structure` -> `Modules` -> `Dependencies` -> `+` -> `JARs or directories...` -> `/path/to/jar/oauth2-okta-resource-server-0.0.1.jar`

Finally, `run` the Spring `Application.java` file how you see fit.


### Next, make the server available via HTTPS with [ngrok](https://ngrok.com/).

Assuming that your example code is listening on
`http://localhost:8080`, start ngrok with the following command:
```
$ ngrok http 8080
```

## Project Configuration
The file `TokenValidation.java` is the controller for the sample application. Following the token approval process, a [Principal](https://docs.oracle.com/javase/7/docs/api/java/security/Principal.html) is returned. For example, the custom claim `user_email` was returned and used to retrieve a [user's Gravatar](https://en.gravatar.com/).
```java
// TokenValidation.java

@RequestMapping(value = "/protected", method = RequestMethod.GET)
    public Map<String, String> resource(Principal principal) {
        Map<String, String> response = new HashMap();
        if (principal != null) {
            response.put("image", this.getGravatar(principal.getName()));
            response.put("name", principal.getName());
        } else { response.put("error", principal.getName()); }
        return response;
    }

    public String getGravatar(String email) {
        return "https://www.gravatar.com/avatar/" + MD5Util.md5Hex(email) + "?s=200&r=pg&d=retro";
    }
```
