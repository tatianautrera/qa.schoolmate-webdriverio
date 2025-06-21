import { $ } from '@wdio/globals'
import Page from './screen.js';
import LoginScreen from './login.screen.js';

class ForgotPasswordScreen extends Page {
    
get txtForgotPassword(){
    return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tv_forgot_password")')
}

get inputEmail(){
    return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_email")')
}

get btnSendLink(){
    return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_send_link")')
}

async recoverPassword(email){
    await LoginScreen.openForgotPasswordScreen()
    await this.inputEmail.setValue(email)
    await this.btnSendLink.click()
}

}

export default new ForgotPasswordScreen();