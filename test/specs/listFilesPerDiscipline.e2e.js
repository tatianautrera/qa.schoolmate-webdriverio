import DisciplineScreen from '../pageobjects/discipline.screen';
import FileScreen from '../pageobjects/file.screen';
import LoginScreen from '../pageobjects/login.screen.js';
import login from '../data/login/login.json';
import file from '../data/files/file.json';

describe('List files per discipline test', () => {
    before(async () => {
        await driver.startActivity("com.fsacchi.schoolmate", "com.fsacchi.schoolmate.core.features.splash.SplashActivity")
        await LoginScreen.login(login.ValidLogin[0])
        driver.pause(20)
        if (await FileScreen.btnPermission.isDisplayed())
            await FileScreen.acceptAlert()
        await FileScreen.accessFileScreen()
        await FileScreen.uploadNewFile(file.uploadFile)

    })

    beforeEach(async () => {
        await DisciplineScreen.accessAppointmentsScreen()
        await DisciplineScreen.accessDisciplineScreen()
    })

    after(async () => {
        await FileScreen.accessFileScreen()
        await FileScreen.deleteFile(file.uploadFile.title)
    })
    
    it('Given have a file for discipline, When access uploads, Should return all files for discipline', async () => {
        await DisciplineScreen.ListActivitiesDiscipline(file.uploadFile.Discipline)
        await DisciplineScreen.accessModalFiles()
        await FileScreen.assertNewFile(file.uploadFile)    
    })

    it('Given not have an activity for discipline When access activiy, Should return empty list', async () => {
        await DisciplineScreen.ListActivitiesDiscipline("Webdriver io web")
        await DisciplineScreen.accessModalFiles()
        await FileScreen.elementDisplayed(await FileScreen.accessElementByText("Você ainda não tem arquivos"))
    })
})