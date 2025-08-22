import FileScreen from '../pageobjects/file.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import file from '../data/files/file.json';

describe('Upload file test', () => {
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await FileScreen.btnPermission.isDisplayed())
            await FileScreen.acceptAlert()
    })

    beforeEach(async () => {
        await FileScreen.accessFileScreen()
    })

    after(async () => {
        await FileScreen.deleteFile(file.uploadFile.title)
    })

    it('When upload a file, Should save the file', async () => {
        await FileScreen.uploadNewFile(file.uploadFile)
        await FileScreen.assertNewFile(file.uploadFile)
    })

    it('When not fill riquired fields, Not Should display save button', async () => {
        for (const upload of file.InputWithInvalidFields) {
            await FileScreen.uploadNewFile(upload)
            await FileScreen.assertNotEnabled(FileScreen.btnSaveUpload)
            await FileScreen.btnCloseModal.click()
        }
    })
})