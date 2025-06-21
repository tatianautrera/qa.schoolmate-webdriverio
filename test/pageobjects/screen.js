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
      get toastTitleSuccess () {
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/title")');
    }

    get toastMessage (){
        return $('android=new UiSelector().resourceId("com.fsacchi.schoolmate:id/message")')
    }

    async assertText (selector, text) {
        await expect(selector).toHaveText(text)
    }

    async assertDisplayed (selector) {
        await expect(selector).toBeDisplayed()
    }

    async acceptAlert(){
        await driver.acceptAlert();
    }

     async validValueField(selector, text){
        await expect(await selector.getValue()).toHaveText(text)
    }
}
