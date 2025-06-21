import { $ } from '@wdio/globals'
import Page from './screen.js';


class ForgotPasswordScreen extends Page {
    
get txtForgotPassword(){
    return $('android=')
}

}

export default new ForgotPasswordScreen();
