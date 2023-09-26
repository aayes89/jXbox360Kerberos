package com.mycompany.xbox360s;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *
 * @author Slam
 */
public class CustomKerberosCallbackHandler implements CallbackHandler {

        private String username;
        private char[] password;

        public CustomKerberosCallbackHandler(String username, char[] password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    // Proporciona el nombre de usuario ingresado
                    NameCallback nameCallback = (NameCallback) callback;
                    nameCallback.setName(username);
                } else if (callback instanceof PasswordCallback) {
                    // Proporciona la contraseña ingresada
                    PasswordCallback passwordCallback = (PasswordCallback) callback;
                    passwordCallback.setPassword(password);
                } else {
                    throw new UnsupportedCallbackException(callback, "Callback no compatible");
                }
            }
        }
    }

