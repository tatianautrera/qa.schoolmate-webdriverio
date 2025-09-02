import FileScreen from '../pageobjects/file.screen.js';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import file from '../data/files/file.json';

describe('Edit upload file test', () => {
    let fileNameDelete
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await FileScreen.btnPermission.isDisplayed())
            await FileScreen.acceptAlert()
    })

    beforeEach(async () => {
        await FileScreen.accessFileScreen()
        await FileScreen.uploadNewFile(file.uploadFile)
    })

    after(async () => {
        await FileScreen.finishSession();
    })

    afterEach(async () => {
        await FileScreen.deleteFile(fileNameDelete)
    })

    it('When Edit a file, Should edit the file', async () => {
        await FileScreen.EditFile(file.uploadFile, file.EditFile)
        await FileScreen.assertNewFile(file.EditFile)
        fileNameDelete = file.EditFile.title
    })

    it('When not fill required fills, Should not edit the file', async () => {
        await FileScreen.EditFile(file.uploadFile, file.EditInputWithInvalidFields)
        await FileScreen.assertNotEnabled(FileScreen.btnSaveUpload)
        await FileScreen.btnCloseModal.click()
        fileNameDelete = file.uploadFile.title
    })
})