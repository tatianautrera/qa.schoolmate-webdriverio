import { $, expect } from '@wdio/globals'
import Page from './screen.js';

/**
 * sub page containing specific selectors and methods for a specific page
 */
class CreateUserScreen extends Page {
    /**
     * define selectors using getter methods
     */
    get titlePage() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tv_title")');
    }

    get inputName() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_name")');
    }

    get inputEmail() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_email")')
    }

    get inputPassword() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_password")')
    }

    get inputConfirmPassword() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/et_confirm_password")')
    }

    get btnCreateUser() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_register")');
    }

    get btnSubmit() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_create_account")')
    }

    messageError(field){
        return $(`//android.widget.LinearLayout[@resource-id="com.fsacchi.schoolmate:id/til_${field}"]//android.widget.TextView[@resource-id="com.fsacchi.schoolmate:id/textinput_error"]`)
    }

    get teste(){
        return $(`//android.widget.LinearLayout[@resource-id="com.fsacchi.schoolmate:id/til_name"]`)
    }

    /**
     * a method to encapsule automation code to interact with the page
     * e.g. to login using username and password
     */
    async openCreateUser() {
        await this.btnCreateUser.click();
    }

    async createUser(user) {
        await this.fillFieldsCreateUser(user)
        await this.btnSubmit.click()
    }
    async fillFieldsCreateUser(user) {
        await this.inputName.setValue(user.name);
        await this.inputEmail.setValue(user.email);
        await this.inputPassword.setValue(user.password);
        await driver.pressKeyCode(66);
        await this.inputConfirmPassword.setValue(user.confirmPassword);
    }

    /**
     * overwrite specific options to adapt it to page object
     */
    open() {
        return super.open('login');
    }
}

export default new CreateUserScreen();
