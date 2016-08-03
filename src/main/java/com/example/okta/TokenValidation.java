/** Author: Jordan Melberg **/

/**
 * Copyright (c) 2015-2016, Okta, Inc. and/or its affiliates. All rights reserved.
 * The Okta software accompanied by this notice is provided pursuant to the Apache License, Version 2.0 (the "License.")
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.example.okta;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenValidation {

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
}

