import { browser } from '@wdio/globals'

/**
* main page object containing all methods, selectors and functionality
* that is shared across all page objects
*/
export default class Page {
    /**
    * Opens a sub page of the page
    * @param path path of the sub page (e.g. /path/to/page.html)
    */
    get toastTitleSuccess() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/title")');
    }

    get toastMessage() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/message")')
    }

    get titleScreen() {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/tv_title")')
    }

    get btnYes(){
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/btn_positive")')
    }

    accessElementByText(text) {
        return $(`android=new UiSelector().text("${text}")`)
    }

    async assertText(selector, text) {
        await expect(selector).toHaveText(text)
    }

    async assertFieldText(fieldValue, text) {
        await expect(fieldValue).toEqual(text)
    }

    async acceptAlert() {
        await driver.acceptAlert();
    }

    async getDataCurrent() {
        let date = new Date()
        return date.toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
        });
    }
    async getDayCurrent(date) {
        return date.toString().substring(0, 2);
    }

    async assertEnabled(selector) {
        await expect(selector).toHaveAttribute("enabled", 'true') 
    }
    async assertNotEnabled(selector) {
        await expect(selector).toHaveAttribute("enabled", 'false') 
    }
}