import { $ } from '@wdio/globals'
import Page from './screen.js';


class LoginScreen extends Page {
    
get inputEmail(){
    return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_email")')
}

get inputPassword(){
    return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_password")')
}

get checkSaveLogin(){
    return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/cb_save_login")')
}

get btnSubmit(){
    return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_enter")')
}

get linkForgotPassword(){
    return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tv_forgot_password")')
}

async login(user){
    await this.inputEmail.setValue(user.email)
    await this.inputPassword.setValue(user.password)
    if(user.saveLogin)
            await this.checkSaveLogin.click()
    await this.btnSubmit.click()
}
}

export default new LoginScreen();
