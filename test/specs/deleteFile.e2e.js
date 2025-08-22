import FileScreen from '../pageobjects/file.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import file from '../data/files/file.json';

describe('Delete upload file test', () => {
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

    it('When open modal delete a file, Should enable confirm screen', async () => {
        await FileScreen.openModalDeleteFile(file.uploadFile.title)
        await FileScreen.assertText(await FileScreen.messageScreen,`Realmente deseja excluir o arquivo ${file.uploadFile.title}?`)
        await FileScreen.btnYes.click()
        
    })

    it('When delete a file, Should delete the file', async () => {
        await FileScreen.deleteFile(file.uploadFile.title)
        await FileScreen.elementNotDisplayed(await FileScreen.accessElementByText(file.uploadFile.title))
    })
})